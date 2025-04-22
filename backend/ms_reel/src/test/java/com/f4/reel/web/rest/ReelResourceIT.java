package com.f4.reel.web.rest;

import static com.f4.reel.domain.ReelAsserts.*;
import static com.f4.reel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.f4.reel.IntegrationTest;
import com.f4.reel.domain.Reel;
import com.f4.reel.repository.ReelRepository;
import com.f4.reel.repository.search.ReelSearchRepository;
import com.f4.reel.service.dto.ReelDTO;
import com.f4.reel.service.mapper.ReelMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReelResourceIT {

    private static final UUID DEFAULT_USER_ID = UUID.randomUUID();
    private static final UUID UPDATED_USER_ID = UUID.randomUUID();

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_VIDEO_URL = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/reels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/reels/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReelRepository reelRepository;

    @Autowired
    private ReelMapper reelMapper;

    @Autowired
    private ReelSearchRepository reelSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReelMockMvc;

    private Reel reel;

    private Reel insertedReel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reel createEntity() {
        return new Reel().userId(DEFAULT_USER_ID).title(DEFAULT_TITLE).videoUrl(DEFAULT_VIDEO_URL).createdAt(DEFAULT_CREATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reel createUpdatedEntity() {
        return new Reel().userId(UPDATED_USER_ID).title(UPDATED_TITLE).videoUrl(UPDATED_VIDEO_URL).createdAt(UPDATED_CREATED_AT);
    }

    @BeforeEach
    void initTest() {
        reel = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReel != null) {
            reelRepository.delete(insertedReel);
            reelSearchRepository.delete(insertedReel);
            insertedReel = null;
        }
    }

    @Test
    @Transactional
    void createReel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reelSearchRepository.findAll());
        // Create the Reel
        ReelDTO reelDTO = reelMapper.toDto(reel);
        var returnedReelDTO = om.readValue(
            restReelMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reelDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReelDTO.class
        );

        // Validate the Reel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReel = reelMapper.toEntity(returnedReelDTO);
        assertReelUpdatableFieldsEquals(returnedReel, getPersistedReel(returnedReel));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(reelSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedReel = returnedReel;
    }

    @Test
    @Transactional
    void createReelWithExistingId() throws Exception {
        // Create the Reel with an existing ID
        insertedReel = reelRepository.saveAndFlush(reel);
        ReelDTO reelDTO = reelMapper.toDto(reel);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reelSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restReelMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reelSearchRepository.findAll());
        // set the field null
        reel.setUserId(null);

        // Create the Reel, which fails.
        ReelDTO reelDTO = reelMapper.toDto(reel);

        restReelMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reelSearchRepository.findAll());
        // set the field null
        reel.setTitle(null);

        // Create the Reel, which fails.
        ReelDTO reelDTO = reelMapper.toDto(reel);

        restReelMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkVideoUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reelSearchRepository.findAll());
        // set the field null
        reel.setVideoUrl(null);

        // Create the Reel, which fails.
        ReelDTO reelDTO = reelMapper.toDto(reel);

        restReelMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reelSearchRepository.findAll());
        // set the field null
        reel.setCreatedAt(null);

        // Create the Reel, which fails.
        ReelDTO reelDTO = reelMapper.toDto(reel);

        restReelMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllReels() throws Exception {
        // Initialize the database
        insertedReel = reelRepository.saveAndFlush(reel);

        // Get all the reelList
        restReelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reel.getId().toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].videoUrl").value(hasItem(DEFAULT_VIDEO_URL)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getReel() throws Exception {
        // Initialize the database
        insertedReel = reelRepository.saveAndFlush(reel);

        // Get the reel
        restReelMockMvc
            .perform(get(ENTITY_API_URL_ID, reel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reel.getId().toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.videoUrl").value(DEFAULT_VIDEO_URL))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReel() throws Exception {
        // Get the reel
        restReelMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReel() throws Exception {
        // Initialize the database
        insertedReel = reelRepository.saveAndFlush(reel);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        reelSearchRepository.save(reel);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reelSearchRepository.findAll());

        // Update the reel
        Reel updatedReel = reelRepository.findById(reel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReel are not directly saved in db
        em.detach(updatedReel);
        updatedReel.userId(UPDATED_USER_ID).title(UPDATED_TITLE).videoUrl(UPDATED_VIDEO_URL).createdAt(UPDATED_CREATED_AT);
        ReelDTO reelDTO = reelMapper.toDto(updatedReel);

        restReelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reelDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reelDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReelToMatchAllProperties(updatedReel);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(reelSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Reel> reelSearchList = Streamable.of(reelSearchRepository.findAll()).toList();
                Reel testReelSearch = reelSearchList.get(searchDatabaseSizeAfter - 1);

                assertReelAllPropertiesEquals(testReelSearch, updatedReel);
            });
    }

    @Test
    @Transactional
    void putNonExistingReel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reelSearchRepository.findAll());
        reel.setId(UUID.randomUUID());

        // Create the Reel
        ReelDTO reelDTO = reelMapper.toDto(reel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reelDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchReel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reelSearchRepository.findAll());
        reel.setId(UUID.randomUUID());

        // Create the Reel
        ReelDTO reelDTO = reelMapper.toDto(reel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reelSearchRepository.findAll());
        reel.setId(UUID.randomUUID());

        // Create the Reel
        ReelDTO reelDTO = reelMapper.toDto(reel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReelMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateReelWithPatch() throws Exception {
        // Initialize the database
        insertedReel = reelRepository.saveAndFlush(reel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reel using partial update
        Reel partialUpdatedReel = new Reel();
        partialUpdatedReel.setId(reel.getId());

        partialUpdatedReel.userId(UPDATED_USER_ID);

        restReelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReel.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReel))
            )
            .andExpect(status().isOk());

        // Validate the Reel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReelUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedReel, reel), getPersistedReel(reel));
    }

    @Test
    @Transactional
    void fullUpdateReelWithPatch() throws Exception {
        // Initialize the database
        insertedReel = reelRepository.saveAndFlush(reel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reel using partial update
        Reel partialUpdatedReel = new Reel();
        partialUpdatedReel.setId(reel.getId());

        partialUpdatedReel.userId(UPDATED_USER_ID).title(UPDATED_TITLE).videoUrl(UPDATED_VIDEO_URL).createdAt(UPDATED_CREATED_AT);

        restReelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReel.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReel))
            )
            .andExpect(status().isOk());

        // Validate the Reel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReelUpdatableFieldsEquals(partialUpdatedReel, getPersistedReel(partialUpdatedReel));
    }

    @Test
    @Transactional
    void patchNonExistingReel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reelSearchRepository.findAll());
        reel.setId(UUID.randomUUID());

        // Create the Reel
        ReelDTO reelDTO = reelMapper.toDto(reel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reelDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reelSearchRepository.findAll());
        reel.setId(UUID.randomUUID());

        // Create the Reel
        ReelDTO reelDTO = reelMapper.toDto(reel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reelSearchRepository.findAll());
        reel.setId(UUID.randomUUID());

        // Create the Reel
        ReelDTO reelDTO = reelMapper.toDto(reel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReelMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteReel() throws Exception {
        // Initialize the database
        insertedReel = reelRepository.saveAndFlush(reel);
        reelRepository.save(reel);
        reelSearchRepository.save(reel);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(reelSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the reel
        restReelMockMvc
            .perform(delete(ENTITY_API_URL_ID, reel.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(reelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchReel() throws Exception {
        // Initialize the database
        insertedReel = reelRepository.saveAndFlush(reel);
        reelSearchRepository.save(reel);

        // Search the reel
        restReelMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + reel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reel.getId().toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].videoUrl").value(hasItem(DEFAULT_VIDEO_URL)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    protected long getRepositoryCount() {
        return reelRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Reel getPersistedReel(Reel reel) {
        return reelRepository.findById(reel.getId()).orElseThrow();
    }

    protected void assertPersistedReelToMatchAllProperties(Reel expectedReel) {
        assertReelAllPropertiesEquals(expectedReel, getPersistedReel(expectedReel));
    }

    protected void assertPersistedReelToMatchUpdatableProperties(Reel expectedReel) {
        assertReelAllUpdatablePropertiesEquals(expectedReel, getPersistedReel(expectedReel));
    }
}
