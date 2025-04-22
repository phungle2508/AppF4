package com.f4.reel.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.f4.reel.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReelDTO.class);
        ReelDTO reelDTO1 = new ReelDTO();
        reelDTO1.setId(UUID.randomUUID());
        ReelDTO reelDTO2 = new ReelDTO();
        assertThat(reelDTO1).isNotEqualTo(reelDTO2);
        reelDTO2.setId(reelDTO1.getId());
        assertThat(reelDTO1).isEqualTo(reelDTO2);
        reelDTO2.setId(UUID.randomUUID());
        assertThat(reelDTO1).isNotEqualTo(reelDTO2);
        reelDTO1.setId(null);
        assertThat(reelDTO1).isNotEqualTo(reelDTO2);
    }
}
