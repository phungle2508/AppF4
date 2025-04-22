package com.f4.commentlike;

import com.f4.commentlike.config.AsyncSyncConfiguration;
import com.f4.commentlike.config.EmbeddedKafka;
import com.f4.commentlike.config.EmbeddedSQL;
import com.f4.commentlike.config.JacksonConfiguration;
import com.f4.commentlike.config.TestSecurityConfiguration;
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
    classes = { MsCommentlikeApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class }
)
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
