package com.f4.commentlike.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.f4.commentlike.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class LikeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LikeDTO.class);
        LikeDTO likeDTO1 = new LikeDTO();
        likeDTO1.setId(UUID.randomUUID());
        LikeDTO likeDTO2 = new LikeDTO();
        assertThat(likeDTO1).isNotEqualTo(likeDTO2);
        likeDTO2.setId(likeDTO1.getId());
        assertThat(likeDTO1).isEqualTo(likeDTO2);
        likeDTO2.setId(UUID.randomUUID());
        assertThat(likeDTO1).isNotEqualTo(likeDTO2);
        likeDTO1.setId(null);
        assertThat(likeDTO1).isNotEqualTo(likeDTO2);
    }
}
