package com.f4.reel.domain;

import static com.f4.reel.domain.ReelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.f4.reel.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reel.class);
        Reel reel1 = getReelSample1();
        Reel reel2 = new Reel();
        assertThat(reel1).isNotEqualTo(reel2);

        reel2.setId(reel1.getId());
        assertThat(reel1).isEqualTo(reel2);

        reel2 = getReelSample2();
        assertThat(reel1).isNotEqualTo(reel2);
    }
}
