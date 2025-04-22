package com.f4.reel.domain;

import java.util.UUID;

public class ReelTestSamples {

    public static Reel getReelSample1() {
        return new Reel()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .userId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .title("title1")
            .videoUrl("videoUrl1");
    }

    public static Reel getReelSample2() {
        return new Reel()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .userId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .title("title2")
            .videoUrl("videoUrl2");
    }

    public static Reel getReelRandomSampleGenerator() {
        return new Reel()
            .id(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .title(UUID.randomUUID().toString())
            .videoUrl(UUID.randomUUID().toString());
    }
}
