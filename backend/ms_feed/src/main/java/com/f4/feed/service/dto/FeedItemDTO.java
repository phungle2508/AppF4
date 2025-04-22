package com.f4.feed.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.f4.feed.domain.FeedItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedItemDTO implements Serializable {

    @NotNull
    private UUID id;

    @NotNull
    private UUID userId;

    @NotNull
    private UUID reelId;

    @NotNull
    private Instant timestamp;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getReelId() {
        return reelId;
    }

    public void setReelId(UUID reelId) {
        this.reelId = reelId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedItemDTO)) {
            return false;
        }

        FeedItemDTO feedItemDTO = (FeedItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, feedItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedItemDTO{" +
            "id='" + getId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", reelId='" + getReelId() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }
}
