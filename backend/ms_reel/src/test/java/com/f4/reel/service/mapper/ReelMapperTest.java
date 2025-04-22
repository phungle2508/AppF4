package com.f4.reel.service.mapper;

import static com.f4.reel.domain.ReelAsserts.*;
import static com.f4.reel.domain.ReelTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReelMapperTest {

    private ReelMapper reelMapper;

    @BeforeEach
    void setUp() {
        reelMapper = new ReelMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReelSample1();
        var actual = reelMapper.toEntity(reelMapper.toDto(expected));
        assertReelAllPropertiesEquals(expected, actual);
    }
}
