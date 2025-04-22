package com.f4.commentlike.service.mapper;

import static com.f4.commentlike.domain.LikeAsserts.*;
import static com.f4.commentlike.domain.LikeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LikeMapperTest {

    private LikeMapper likeMapper;

    @BeforeEach
    void setUp() {
        likeMapper = new LikeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLikeSample1();
        var actual = likeMapper.toEntity(likeMapper.toDto(expected));
        assertLikeAllPropertiesEquals(expected, actual);
    }
}
