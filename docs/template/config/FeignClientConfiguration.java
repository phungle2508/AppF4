package com.f4.reel.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.f4.reel.client.api.*;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.context.annotation.Lazy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Configuration
public class FeignClientConfiguration {

    private final LoadBalancerClient loadBalancerClient;

    public FeignClientConfiguration(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    @Bean
    public RequestInterceptor authRequestInterceptor() {
        return requestTemplate -> {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth instanceof JwtAuthenticationToken jwt) {
                requestTemplate.header("Authorization", "Bearer " + jwt.getToken().getTokenValue());
            }
        };
    }

    @Bean
    public ObjectMapper feignObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
    public Encoder feignEncoder(ObjectMapper objectMapper) {
        return new JacksonEncoder(objectMapper);
    }

    @Bean
    public Decoder feignDecoder(ObjectMapper objectMapper) {
        return new JacksonDecoder(objectMapper);
    }

    // === Lazy Feign Beans ===

    @Bean
    @Lazy
    public UserResourceApi userResourceApi(RequestInterceptor auth, Encoder enc, Decoder dec) {
        return lazyFeignFor("msuser", UserResourceApi.class, auth, enc, dec);
    }

    @Bean
    @Lazy
    public FeedItemResourceApi feedItemResourceApi(RequestInterceptor auth, Encoder enc, Decoder dec) {
        return lazyFeignFor("msfeed", FeedItemResourceApi.class, auth, enc, dec);
    }

    @Bean
    @Lazy
    public ReelResourceApi reelResourceApi(RequestInterceptor auth, Encoder enc, Decoder dec) {
        return lazyFeignFor("msreel", ReelResourceApi.class, auth, enc, dec);
    }

    @Bean
    @Lazy
    public NotificationResourceApi notificationResourceApi(RequestInterceptor auth, Encoder enc, Decoder dec) {
        return lazyFeignFor("msnotification", NotificationResourceApi.class, auth, enc, dec);
    }

    @Bean
    @Lazy
    public MsNotificationKafkaResourceApi kafkaNotificationApi(RequestInterceptor auth, Encoder enc, Decoder dec) {
        return lazyFeignFor("msnotification", MsNotificationKafkaResourceApi.class, auth, enc, dec);
    }

    @Bean
    @Lazy
    public KeycloakUserResourceApi keycloakUserApi(RequestInterceptor auth, Encoder enc, Decoder dec) {
        return lazyFeignFor("msuser", KeycloakUserResourceApi.class, auth, enc, dec);
    }

    @Bean
    @Lazy
    public CommentResourceApi commentResourceApi(RequestInterceptor auth, Encoder enc, Decoder dec) {
        return lazyFeignFor("mscommentlike", CommentResourceApi.class, auth, enc, dec);
    }

    @Bean
    @Lazy
    public LikeResourceApi likeResourceApi(RequestInterceptor auth, Encoder enc, Decoder dec) {
        return lazyFeignFor("mscommentlike", LikeResourceApi.class, auth, enc, dec);
    }

    // === Lazy proxy factory ===
    private <T> T lazyFeignFor(String serviceName, Class<T> clazz, RequestInterceptor auth, Encoder enc, Decoder dec) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[] { clazz },
                new LazyFeignHandler<>(serviceName, clazz, auth, enc, dec));
    }

    // === Lazy Feign client handler ===
    private class LazyFeignHandler<T> implements InvocationHandler {
        private final String serviceName;
        private final Class<T> clazz;
        private final RequestInterceptor auth;
        private final Encoder encoder;
        private final Decoder decoder;
        private T feignClient;

        LazyFeignHandler(String serviceName, Class<T> clazz, RequestInterceptor auth, Encoder encoder,
                Decoder decoder) {
            this.serviceName = serviceName;
            this.clazz = clazz;
            this.auth = auth;
            this.encoder = encoder;
            this.decoder = decoder;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // Handle Object methods without triggering remote discovery
            switch (method.getName()) {
                case "hashCode":
                    return System.identityHashCode(proxy);
                case "toString":
                    return "LazyFeignProxy(" + clazz.getSimpleName() + ")";
                case "equals":
                    return proxy == args[0];
            }

            // Lazy init the Feign client
            if (feignClient == null) {
                var instance = loadBalancerClient.choose(serviceName);
                if (instance == null) {
                    throw new IllegalStateException("Service [" + serviceName + "] not found via discovery");
                }
                String baseUrl = instance.getUri().toString();
                feignClient = Feign.builder()
                        .encoder(encoder)
                        .decoder(decoder)
                        .requestInterceptor(auth)
                        .logger(new Slf4jLogger(clazz))
                        .logLevel(Logger.Level.FULL)
                        .target(clazz, baseUrl);
            }

            return method.invoke(feignClient, args);
        }
    }
}