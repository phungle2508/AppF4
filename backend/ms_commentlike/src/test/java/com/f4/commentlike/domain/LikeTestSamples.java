package com.f4.commentlike.domain;

import java.util.UUID;

public class LikeTestSamples {

    public static Like getLikeSample1() {
        return new Like()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .reelId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .userId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static Like getLikeSample2() {
        return new Like()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .reelId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .userId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static Like getLikeRandomSampleGenerator() {
        return new Like().id(UUID.randomUUID()).reelId(UUID.randomUUID()).userId(UUID.randomUUID());
    }
}
