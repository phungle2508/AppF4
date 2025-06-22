package com.f4.commentlike.kafka.util;

import com.f4.commentlike.avro.EventEnvelope;
import com.f4.commentlike.avro.LikeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.UUID;

public class AvroConverter {

    private static final Logger LOG = LoggerFactory.getLogger(AvroConverter.class);

    private AvroConverter() {
    }

    /**
     * Creates an Avro EventEnvelope for a Like event.
     *
     * @param eventName The event name.
     * @param likeDTO   The service LikeDTO to convert.
     * @return EventEnvelope containing the Avro LikeDTO payload.
     */
    public static EventEnvelope createLikeEvent(String eventName, com.f4.commentlike.service.dto.LikeDTO likeDTO) {
        LOG.debug("Creating Avro LikeDTO from service LikeDTO: {}", likeDTO);

        LikeDTO avroLikeDTO = LikeDTO.newBuilder()
                .setId(likeDTO.getId() != null ? likeDTO.getId().toString() : null)
                .setReelId(likeDTO.getParentId() != null ? likeDTO.getParentId().toString() : null)
                .setUserId(likeDTO.getUserId() != null ? likeDTO.getUserId().toString() : null)
                // .setCreatedAt(likeDTO.getCreatedAt() != null ?
                // likeDTO.getCreatedAt().toEpochMilli() : null)
                .build();

        LOG.debug("Created Avro LikeDTO: {}", avroLikeDTO);

        return EventEnvelope.newBuilder()
                .setEventName(eventName)
                .setPayload(avroLikeDTO)
                .build();
    }

    /**
     * Converts an Avro LikeDTO to the service LikeDTO.
     *
     * @param avroPayload The Avro LikeDTO.
     * @return The service LikeDTO or null if input is null.
     */
    public static com.f4.commentlike.service.dto.LikeDTO convertToServiceLikeDTO(LikeDTO avroPayload) {
        if (avroPayload == null) {
            LOG.warn("Attempted to convert a null Avro LikeDTO to service DTO.");
            return null;
        }
        LOG.debug("Converting Avro LikeDTO to service DTO: {}", avroPayload);

        com.f4.commentlike.service.dto.LikeDTO serviceDto = new com.f4.commentlike.service.dto.LikeDTO();

        try {
            if (avroPayload.getId() != null) {
                serviceDto.setId(UUID.fromString(avroPayload.getId()));
            }
            if (avroPayload.getReelId() != null) {
                serviceDto.setParentId(UUID.fromString(avroPayload.getReelId()));
            }
            if (avroPayload.getUserId() != null) {
                serviceDto.setUserId(UUID.fromString(avroPayload.getUserId()));
            }
            // if (avroPayload.getCreatedAt() != null) {
            // serviceDto.setCreatedAt(Instant.ofEpochMilli(avroPayload.getCreatedAt()));
            // }
            LOG.debug("Successfully converted to service LikeDTO: {}", serviceDto);
            return serviceDto;
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid UUID format in Avro payload", e);
            throw new RuntimeException("Failed to convert Avro LikeDTO: invalid UUID", e);
        } catch (Exception e) {
            LOG.error("Error converting Avro LikeDTO to service DTO", e);
            throw new RuntimeException("Failed to convert Avro LikeDTO", e);
        }
    }
}
