package com.f4.commentlike.domain;

import java.util.UUID;

public class CommentTestSamples {

    public static Comment getCommentSample1() {
        return new Comment()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .reelId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .userId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static Comment getCommentSample2() {
        return new Comment()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .reelId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .userId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static Comment getCommentRandomSampleGenerator() {
        return new Comment().id(UUID.randomUUID()).reelId(UUID.randomUUID()).userId(UUID.randomUUID());
    }
}
