package com.f4.reel.kafka.util;

import com.f4.reel.avro.EventEnvelope;
import com.f4.reel.avro.ReelDTO;
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
    public static EventEnvelope createPostReelEvent(String eventName, UUID userId, String title, String videoUrl) {
        LOG.debug("Creating Avro ReelDTO for userId: {}, title: {}", userId, title);
        ReelDTO avroReelDTO = ReelDTO.newBuilder()
                .setVersion(1) // Defaulting version, or could be a parameter
                .setUserId(userId.toString()) // Avro schema expects string for userId
                .setTitle(title)
                .setVideoUrl(videoUrl)
                .setCreatedAt(Instant.now().toString()) // Avro schema expects string for createdAt
                .build();

        LOG.debug("Creating Avro EventEnvelope with eventName 'postReel' and payload: {}", avroReelDTO);
        return EventEnvelope.newBuilder()
                .setEventName(
                        eventName)
                .setPayload(avroReelDTO)
                .build();
    }

    /**
     * Converts an Avro ReelDTO to a service-specific ReelDTO.
     *
     * @param avroPayload The Avro ReelDTO.
     * @return The corresponding service ReelDTO, or null if avroPayload is null.
     */
    public static com.f4.reel.service.dto.ReelDTO convertToServiceReelDTO(ReelDTO avroPayload) {
        if (avroPayload == null) {
            LOG.warn("Attempted to convert a null Avro ReelDTO to service DTO.");
            return null;
        }
        LOG.debug("Converting Avro ReelDTO to service DTO: {}", avroPayload);
        com.f4.reel.service.dto.ReelDTO serviceDto = new com.f4.reel.service.dto.ReelDTO();
        try {

            if (avroPayload.getUserId() != null) {
                try {
                    serviceDto.setUserId(UUID.fromString(avroPayload.getUserId()));
                } catch (IllegalArgumentException e) {
                    LOG.error("Invalid UUID format for userId from Avro: {}", avroPayload.getUserId(), e);
                    // Optionally rethrow or handle as per application requirements
                    throw new IllegalArgumentException(
                            "Invalid UUID format in Avro payload for userId: " + avroPayload.getUserId(), e);
                }
            }
            if (avroPayload.getTitle() != null) {
                serviceDto.setTitle(avroPayload.getTitle());
            }
            if (avroPayload.getVideoUrl() != null) {
                serviceDto.setVideoUrl(avroPayload.getVideoUrl());
            }
            if (avroPayload.getCreatedAt() != null) {
                try {
                    serviceDto.setCreatedAt(Instant.parse(avroPayload.getCreatedAt()));
                } catch (Exception e) {
                    LOG.error("Invalid date format for createdAt from Avro: {}", avroPayload.getCreatedAt(), e);
                    // Optionally rethrow or handle
                    throw new IllegalArgumentException(
                            "Invalid date format in Avro payload for createdAt: " + avroPayload.getCreatedAt(), e);
                }
            }
            LOG.debug("Successfully converted Avro ReelDTO to service DTO: {}", serviceDto);
            return serviceDto;
        } catch (Exception e) {
            LOG.error("Error converting Avro ReelDTO to service DTO: {}", e.getMessage(), e);
            // Rethrow as a runtime exception that can be caught by the calling
            // service/consumer
            throw new RuntimeException("Failed to convert Avro ReelDTO to service DTO", e);
        }
    }
}