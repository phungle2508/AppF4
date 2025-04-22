package com.f4.commentlike.web.rest;

import static com.f4.commentlike.domain.LikeAsserts.*;
import static com.f4.commentlike.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.f4.commentlike.IntegrationTest;
import com.f4.commentlike.domain.Like;
import com.f4.commentlike.repository.LikeRepository;
import com.f4.commentlike.service.dto.LikeDTO;
import com.f4.commentlike.service.mapper.LikeMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LikeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LikeResourceIT {

    private static final UUID DEFAULT_REEL_ID = UUID.randomUUID();
    private static final UUID UPDATED_REEL_ID = UUID.randomUUID();

    private static final UUID DEFAULT_USER_ID = UUID.randomUUID();
    private static final UUID UPDATED_USER_ID = UUID.randomUUID();

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/likes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLikeMockMvc;

    private Like like;

    private Like insertedLike;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Like createEntity() {
        return new Like().reelId(DEFAULT_REEL_ID).userId(DEFAULT_USER_ID).createdAt(DEFAULT_CREATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Like createUpdatedEntity() {
        return new Like().reelId(UPDATED_REEL_ID).userId(UPDATED_USER_ID).createdAt(UPDATED_CREATED_AT);
    }

    @BeforeEach
    void initTest() {
        like = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLike != null) {
            likeRepository.delete(insertedLike);
            insertedLike = null;
        }
    }

    @Test
    @Transactional
    void createLike() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Like
        LikeDTO likeDTO = likeMapper.toDto(like);
        var returnedLikeDTO = om.readValue(
            restLikeMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LikeDTO.class
        );

        // Validate the Like in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLike = likeMapper.toEntity(returnedLikeDTO);
        assertLikeUpdatableFieldsEquals(returnedLike, getPersistedLike(returnedLike));

        insertedLike = returnedLike;
    }

    @Test
    @Transactional
    void createLikeWithExistingId() throws Exception {
        // Create the Like with an existing ID
        insertedLike = likeRepository.saveAndFlush(like);
        LikeDTO likeDTO = likeMapper.toDto(like);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLikeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Like in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReelIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        like.setReelId(null);

        // Create the Like, which fails.
        LikeDTO likeDTO = likeMapper.toDto(like);

        restLikeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        like.setUserId(null);

        // Create the Like, which fails.
        LikeDTO likeDTO = likeMapper.toDto(like);

        restLikeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        like.setCreatedAt(null);

        // Create the Like, which fails.
        LikeDTO likeDTO = likeMapper.toDto(like);

        restLikeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLikes() throws Exception {
        // Initialize the database
        insertedLike = likeRepository.saveAndFlush(like);

        // Get all the likeList
        restLikeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(like.getId().toString())))
            .andExpect(jsonPath("$.[*].reelId").value(hasItem(DEFAULT_REEL_ID.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getLike() throws Exception {
        // Initialize the database
        insertedLike = likeRepository.saveAndFlush(like);

        // Get the like
        restLikeMockMvc
            .perform(get(ENTITY_API_URL_ID, like.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(like.getId().toString()))
            .andExpect(jsonPath("$.reelId").value(DEFAULT_REEL_ID.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLike() throws Exception {
        // Get the like
        restLikeMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLike() throws Exception {
        // Initialize the database
        insertedLike = likeRepository.saveAndFlush(like);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the like
        Like updatedLike = likeRepository.findById(like.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLike are not directly saved in db
        em.detach(updatedLike);
        updatedLike.reelId(UPDATED_REEL_ID).userId(UPDATED_USER_ID).createdAt(UPDATED_CREATED_AT);
        LikeDTO likeDTO = likeMapper.toDto(updatedLike);

        restLikeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(likeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Like in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLikeToMatchAllProperties(updatedLike);
    }

    @Test
    @Transactional
    void putNonExistingLike() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        like.setId(UUID.randomUUID());

        // Create the Like
        LikeDTO likeDTO = likeMapper.toDto(like);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(likeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Like in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLike() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        like.setId(UUID.randomUUID());

        // Create the Like
        LikeDTO likeDTO = likeMapper.toDto(like);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(likeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Like in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLike() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        like.setId(UUID.randomUUID());

        // Create the Like
        LikeDTO likeDTO = likeMapper.toDto(like);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Like in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLikeWithPatch() throws Exception {
        // Initialize the database
        insertedLike = likeRepository.saveAndFlush(like);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the like using partial update
        Like partialUpdatedLike = new Like();
        partialUpdatedLike.setId(like.getId());

        partialUpdatedLike.reelId(UPDATED_REEL_ID).userId(UPDATED_USER_ID).createdAt(UPDATED_CREATED_AT);

        restLikeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLike.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLike))
            )
            .andExpect(status().isOk());

        // Validate the Like in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLikeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLike, like), getPersistedLike(like));
    }

    @Test
    @Transactional
    void fullUpdateLikeWithPatch() throws Exception {
        // Initialize the database
        insertedLike = likeRepository.saveAndFlush(like);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the like using partial update
        Like partialUpdatedLike = new Like();
        partialUpdatedLike.setId(like.getId());

        partialUpdatedLike.reelId(UPDATED_REEL_ID).userId(UPDATED_USER_ID).createdAt(UPDATED_CREATED_AT);

        restLikeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLike.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLike))
            )
            .andExpect(status().isOk());

        // Validate the Like in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLikeUpdatableFieldsEquals(partialUpdatedLike, getPersistedLike(partialUpdatedLike));
    }

    @Test
    @Transactional
    void patchNonExistingLike() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        like.setId(UUID.randomUUID());

        // Create the Like
        LikeDTO likeDTO = likeMapper.toDto(like);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, likeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(likeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Like in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLike() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        like.setId(UUID.randomUUID());

        // Create the Like
        LikeDTO likeDTO = likeMapper.toDto(like);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(likeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Like in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLike() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        like.setId(UUID.randomUUID());

        // Create the Like
        LikeDTO likeDTO = likeMapper.toDto(like);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(likeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Like in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLike() throws Exception {
        // Initialize the database
        insertedLike = likeRepository.saveAndFlush(like);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the like
        restLikeMockMvc
            .perform(delete(ENTITY_API_URL_ID, like.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return likeRepository.count();
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

    protected Like getPersistedLike(Like like) {
        return likeRepository.findById(like.getId()).orElseThrow();
    }

    protected void assertPersistedLikeToMatchAllProperties(Like expectedLike) {
        assertLikeAllPropertiesEquals(expectedLike, getPersistedLike(expectedLike));
    }

    protected void assertPersistedLikeToMatchUpdatableProperties(Like expectedLike) {
        assertLikeAllUpdatablePropertiesEquals(expectedLike, getPersistedLike(expectedLike));
    }
}
