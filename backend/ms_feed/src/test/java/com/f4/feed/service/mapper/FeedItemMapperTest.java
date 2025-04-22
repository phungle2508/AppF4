package com.f4.feed.service.mapper;

import static com.f4.feed.domain.FeedItemAsserts.*;
import static com.f4.feed.domain.FeedItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeedItemMapperTest {

    private FeedItemMapper feedItemMapper;

    @BeforeEach
    void setUp() {
        feedItemMapper = new FeedItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFeedItemSample1();
        var actual = feedItemMapper.toEntity(feedItemMapper.toDto(expected));
        assertFeedItemAllPropertiesEquals(expected, actual);
    }
}
