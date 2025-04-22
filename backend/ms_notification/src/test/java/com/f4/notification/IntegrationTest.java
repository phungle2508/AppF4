package com.f4.notification;

import com.f4.notification.config.AsyncSyncConfiguration;
import com.f4.notification.config.EmbeddedKafka;
import com.f4.notification.config.EmbeddedSQL;
import com.f4.notification.config.JacksonConfiguration;
import com.f4.notification.config.TestSecurityConfiguration;
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
@SpringBootTest(
    classes = { MsNotificationApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class }
)
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
