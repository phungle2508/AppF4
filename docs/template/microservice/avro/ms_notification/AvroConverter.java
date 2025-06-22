package com.f4.notification.kafka.util;

import com.f4.notification.avro.EventEnvelope;
import com.f4.notification.avro.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public static EventEnvelope createNotificationEvent(String eventName,
            com.f4.notification.service.dto.NotificationDTO dto) {
        NotificationDTO avroNotificationDTO = NotificationDTO.newBuilder()
                .setId(dto.getId() != null ? dto.getId().toString() : null)
                .setRecipientId(dto.getRecipientId() != null ? dto.getRecipientId().toString() : null)
                .setType(dto.getType())
                .setMessage(dto.getMessage())
                .setIsRead(dto.getIsRead())
                .build();

        return EventEnvelope.newBuilder()
                .setEventName(eventName)
                .setPayload(avroNotificationDTO)
                .build();
    }

    /**
     * Converts an Avro NotificationDTO to a service-specific NotificationDTO.
     *
     * @param avroPayload The Avro NotificationDTO.
     * @return The corresponding service NotificationDTO, or null if avroPayload is
     *         null.
     */
    public static com.f4.notification.service.dto.NotificationDTO convertToServiceNotificationDTO(
            NotificationDTO avroPayload) {
        if (avroPayload == null)
            return null;

        com.f4.notification.service.dto.NotificationDTO dto = new com.f4.notification.service.dto.NotificationDTO();
        dto.setId(UUID.fromString(avroPayload.getId()));
        dto.setRecipientId(UUID.fromString(avroPayload.getRecipientId()));
        dto.setType(avroPayload.getType());
        dto.setMessage(avroPayload.getMessage());
        dto.setIsRead(avroPayload.getIsRead());
        return dto;
    }

}
