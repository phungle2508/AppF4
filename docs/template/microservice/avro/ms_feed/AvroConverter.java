package com.f4.feed.kafka.util;

import com.f4.feed.avro.EventEnvelope;
import com.f4.feed.avro.FeedItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.UUID;

public class AvroConverter {

    private static final Logger LOG = LoggerFactory.getLogger(AvroConverter.class);

    // Private constructor to prevent instantiation of utility class
    private AvroConverter() {
    }

    /**
     * Creates an Avro EventEnvelope for a 'postReel' event.
     * 
     * @param eventName
     *
     * @param userId    The UUID of the user.
     * @param title     The title of the reel.
     * @param videoUrl  The video URL of the reel.
     * @return The created Avro EventEnvelope.
     */
    public static EventEnvelope createFeedItemEvent(String eventName, UUID id, UUID userId, UUID reelId) {
        FeedItemDTO avroFeedItemDTO = FeedItemDTO.newBuilder()
                .setId(id != null ? id.toString() : null)
                .setUserId(userId != null ? userId.toString() : null)
                .setReelId(reelId != null ? reelId.toString() : null)
                .build();

        return EventEnvelope.newBuilder()
                .setEventName(eventName)
                .setPayload(avroFeedItemDTO)
                .build();
    }

    /**
     * Converts an Avro FeedItemDTO to a service-specific FeedItemDTO.
     *
     * @param avroPayload The Avro FeedItemDTO.
     * @return The corresponding service FeedItemDTO, or null if avroPayload is
     *         null.
     */
    public static com.f4.feed.service.dto.FeedItemDTO convertToServiceFeedItemDTO(FeedItemDTO avroPayload) {
        if (avroPayload == null) {
            LOG.warn("Attempted to convert a null Avro FeedItemDTO to service DTO.");
            return null;
        }
        com.f4.feed.service.dto.FeedItemDTO serviceDto = new com.f4.feed.service.dto.FeedItemDTO();
        try {
            if (avroPayload.getId() != null) {
                serviceDto.setId(UUID.fromString(avroPayload.getId()));
            }
            if (avroPayload.getUserId() != null) {
                serviceDto.setUserId(UUID.fromString(avroPayload.getUserId()));
            }

            return serviceDto;
        } catch (Exception e) {
            LOG.error("Error converting Avro FeedItemDTO to service DTO", e);
            throw new RuntimeException("Failed to convert Avro FeedItemDTO to service DTO", e);
        }
    }

}