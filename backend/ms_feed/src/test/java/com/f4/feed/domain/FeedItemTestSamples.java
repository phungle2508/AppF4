package com.f4.feed.domain;

import java.util.UUID;

public class FeedItemTestSamples {

    public static FeedItem getFeedItemSample1() {
        return new FeedItem()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .userId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .reelId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static FeedItem getFeedItemSample2() {
        return new FeedItem()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .userId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .reelId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static FeedItem getFeedItemRandomSampleGenerator() {
        return new FeedItem().id(UUID.randomUUID()).userId(UUID.randomUUID()).reelId(UUID.randomUUID());
    }
}
