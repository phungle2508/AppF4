package com.f4.reel.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.f4.reel.domain.Reel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReelDTO implements Serializable {

    @NotNull
    private UUID id;

    @NotNull
    private UUID userId;

    @NotNull
    private String title;

    @NotNull
    private String videoUrl;

    @NotNull
    private Instant createdAt;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReelDTO)) {
            return false;
        }

        ReelDTO reelDTO = (ReelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReelDTO{" +
            "id='" + getId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", title='" + getTitle() + "'" +
            ", videoUrl='" + getVideoUrl() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
