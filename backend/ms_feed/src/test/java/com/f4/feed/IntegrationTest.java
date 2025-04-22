package com.f4.feed;

import com.f4.feed.config.AsyncSyncConfiguration;
import com.f4.feed.config.EmbeddedElasticsearch;
import com.f4.feed.config.EmbeddedKafka;
import com.f4.feed.config.EmbeddedSQL;
import com.f4.feed.config.JacksonConfiguration;
import com.f4.feed.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { MsFeedApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class })
@EmbeddedElasticsearch
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
