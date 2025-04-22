package com.f4.feed.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.f4.feed.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class FeedItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeedItemDTO.class);
        FeedItemDTO feedItemDTO1 = new FeedItemDTO();
        feedItemDTO1.setId(UUID.randomUUID());
        FeedItemDTO feedItemDTO2 = new FeedItemDTO();
        assertThat(feedItemDTO1).isNotEqualTo(feedItemDTO2);
        feedItemDTO2.setId(feedItemDTO1.getId());
        assertThat(feedItemDTO1).isEqualTo(feedItemDTO2);
        feedItemDTO2.setId(UUID.randomUUID());
        assertThat(feedItemDTO1).isNotEqualTo(feedItemDTO2);
        feedItemDTO1.setId(null);
        assertThat(feedItemDTO1).isNotEqualTo(feedItemDTO2);
    }
}
