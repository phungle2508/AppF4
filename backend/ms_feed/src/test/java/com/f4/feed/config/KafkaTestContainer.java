package com.f4.feed.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class KafkaTestContainer implements InitializingBean, DisposableBean {

    private KafkaContainer kafkaContainer;
    private static final Logger LOG = LoggerFactory.getLogger(KafkaTestContainer.class);

    @Override
    public void destroy() {
        if (null != kafkaContainer && kafkaContainer.isRunning()) {
            kafkaContainer.close();
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (null == kafkaContainer) {
            kafkaContainer = new KafkaContainer(DockerImageName.parse("apache/kafka-native:4.0.0"))
                .withLogConsumer(new Slf4jLogConsumer(LOG))
                .withEnv("KAFKA_LISTENERS", "PLAINTEXT://:9092,BROKER://:9093,CONTROLLER://:9094")
                .withReuse(true);
        }
        if (!kafkaContainer.isRunning()) {
            kafkaContainer.start();
        }
    }

    public KafkaContainer getKafkaContainer() {
        return kafkaContainer;
    }
}
