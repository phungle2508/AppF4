package com.f4.feed.web.rest;

import static com.f4.feed.domain.FeedItemAsserts.*;
import static com.f4.feed.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.f4.feed.IntegrationTest;
import com.f4.feed.domain.FeedItem;
import com.f4.feed.repository.FeedItemRepository;
import com.f4.feed.repository.search.FeedItemSearchRepository;
import com.f4.feed.service.dto.FeedItemDTO;
import com.f4.feed.service.mapper.FeedItemMapper;
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
 * Integration tests for the {@link FeedItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeedItemResourceIT {

    private static final UUID DEFAULT_USER_ID = UUID.randomUUID();
    private static final UUID UPDATED_USER_ID = UUID.randomUUID();

    private static final UUID DEFAULT_REEL_ID = UUID.randomUUID();
    private static final UUID UPDATED_REEL_ID = UUID.randomUUID();

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/feed-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/feed-items/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeedItemRepository feedItemRepository;

    @Autowired
    private FeedItemMapper feedItemMapper;

    @Autowired
    private FeedItemSearchRepository feedItemSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeedItemMockMvc;

    private FeedItem feedItem;

    private FeedItem insertedFeedItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedItem createEntity() {
        return new FeedItem().userId(DEFAULT_USER_ID).reelId(DEFAULT_REEL_ID).timestamp(DEFAULT_TIMESTAMP);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedItem createUpdatedEntity() {
        return new FeedItem().userId(UPDATED_USER_ID).reelId(UPDATED_REEL_ID).timestamp(UPDATED_TIMESTAMP);
    }

    @BeforeEach
    void initTest() {
        feedItem = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFeedItem != null) {
            feedItemRepository.delete(insertedFeedItem);
            feedItemSearchRepository.delete(insertedFeedItem);
            insertedFeedItem = null;
        }
    }

    @Test
    @Transactional
    void createFeedItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        // Create the FeedItem
        FeedItemDTO feedItemDTO = feedItemMapper.toDto(feedItem);
        var returnedFeedItemDTO = om.readValue(
            restFeedItemMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedItemDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FeedItemDTO.class
        );

        // Validate the FeedItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFeedItem = feedItemMapper.toEntity(returnedFeedItemDTO);
        assertFeedItemUpdatableFieldsEquals(returnedFeedItem, getPersistedFeedItem(returnedFeedItem));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedFeedItem = returnedFeedItem;
    }

    @Test
    @Transactional
    void createFeedItemWithExistingId() throws Exception {
        // Create the FeedItem with an existing ID
        insertedFeedItem = feedItemRepository.saveAndFlush(feedItem);
        FeedItemDTO feedItemDTO = feedItemMapper.toDto(feedItem);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedItemSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedItemMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FeedItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        // set the field null
        feedItem.setUserId(null);

        // Create the FeedItem, which fails.
        FeedItemDTO feedItemDTO = feedItemMapper.toDto(feedItem);

        restFeedItemMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkReelIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        // set the field null
        feedItem.setReelId(null);

        // Create the FeedItem, which fails.
        FeedItemDTO feedItemDTO = feedItemMapper.toDto(feedItem);

        restFeedItemMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        // set the field null
        feedItem.setTimestamp(null);

        // Create the FeedItem, which fails.
        FeedItemDTO feedItemDTO = feedItemMapper.toDto(feedItem);

        restFeedItemMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllFeedItems() throws Exception {
        // Initialize the database
        insertedFeedItem = feedItemRepository.saveAndFlush(feedItem);

        // Get all the feedItemList
        restFeedItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedItem.getId().toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].reelId").value(hasItem(DEFAULT_REEL_ID.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    void getFeedItem() throws Exception {
        // Initialize the database
        insertedFeedItem = feedItemRepository.saveAndFlush(feedItem);

        // Get the feedItem
        restFeedItemMockMvc
            .perform(get(ENTITY_API_URL_ID, feedItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feedItem.getId().toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.reelId").value(DEFAULT_REEL_ID.toString()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFeedItem() throws Exception {
        // Get the feedItem
        restFeedItemMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeedItem() throws Exception {
        // Initialize the database
        insertedFeedItem = feedItemRepository.saveAndFlush(feedItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedItemSearchRepository.save(feedItem);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedItemSearchRepository.findAll());

        // Update the feedItem
        FeedItem updatedFeedItem = feedItemRepository.findById(feedItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeedItem are not directly saved in db
        em.detach(updatedFeedItem);
        updatedFeedItem.userId(UPDATED_USER_ID).reelId(UPDATED_REEL_ID).timestamp(UPDATED_TIMESTAMP);
        FeedItemDTO feedItemDTO = feedItemMapper.toDto(updatedFeedItem);

        restFeedItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the FeedItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeedItemToMatchAllProperties(updatedFeedItem);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<FeedItem> feedItemSearchList = Streamable.of(feedItemSearchRepository.findAll()).toList();
                FeedItem testFeedItemSearch = feedItemSearchList.get(searchDatabaseSizeAfter - 1);

                assertFeedItemAllPropertiesEquals(testFeedItemSearch, updatedFeedItem);
            });
    }

    @Test
    @Transactional
    void putNonExistingFeedItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        feedItem.setId(UUID.randomUUID());

        // Create the FeedItem
        FeedItemDTO feedItemDTO = feedItemMapper.toDto(feedItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeedItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        feedItem.setId(UUID.randomUUID());

        // Create the FeedItem
        FeedItemDTO feedItemDTO = feedItemMapper.toDto(feedItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeedItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        feedItem.setId(UUID.randomUUID());

        // Create the FeedItem
        FeedItemDTO feedItemDTO = feedItemMapper.toDto(feedItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedItemMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateFeedItemWithPatch() throws Exception {
        // Initialize the database
        insertedFeedItem = feedItemRepository.saveAndFlush(feedItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedItem using partial update
        FeedItem partialUpdatedFeedItem = new FeedItem();
        partialUpdatedFeedItem.setId(feedItem.getId());

        partialUpdatedFeedItem.userId(UPDATED_USER_ID);

        restFeedItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedItem))
            )
            .andExpect(status().isOk());

        // Validate the FeedItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedItemUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFeedItem, feedItem), getPersistedFeedItem(feedItem));
    }

    @Test
    @Transactional
    void fullUpdateFeedItemWithPatch() throws Exception {
        // Initialize the database
        insertedFeedItem = feedItemRepository.saveAndFlush(feedItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedItem using partial update
        FeedItem partialUpdatedFeedItem = new FeedItem();
        partialUpdatedFeedItem.setId(feedItem.getId());

        partialUpdatedFeedItem.userId(UPDATED_USER_ID).reelId(UPDATED_REEL_ID).timestamp(UPDATED_TIMESTAMP);

        restFeedItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedItem))
            )
            .andExpect(status().isOk());

        // Validate the FeedItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedItemUpdatableFieldsEquals(partialUpdatedFeedItem, getPersistedFeedItem(partialUpdatedFeedItem));
    }

    @Test
    @Transactional
    void patchNonExistingFeedItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        feedItem.setId(UUID.randomUUID());

        // Create the FeedItem
        FeedItemDTO feedItemDTO = feedItemMapper.toDto(feedItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feedItemDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeedItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        feedItem.setId(UUID.randomUUID());

        // Create the FeedItem
        FeedItemDTO feedItemDTO = feedItemMapper.toDto(feedItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeedItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        feedItem.setId(UUID.randomUUID());

        // Create the FeedItem
        FeedItemDTO feedItemDTO = feedItemMapper.toDto(feedItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedItemMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(feedItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteFeedItem() throws Exception {
        // Initialize the database
        insertedFeedItem = feedItemRepository.saveAndFlush(feedItem);
        feedItemRepository.save(feedItem);
        feedItemSearchRepository.save(feedItem);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the feedItem
        restFeedItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, feedItem.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedItemSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchFeedItem() throws Exception {
        // Initialize the database
        insertedFeedItem = feedItemRepository.saveAndFlush(feedItem);
        feedItemSearchRepository.save(feedItem);

        // Search the feedItem
        restFeedItemMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + feedItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedItem.getId().toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].reelId").value(hasItem(DEFAULT_REEL_ID.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }

    protected long getRepositoryCount() {
        return feedItemRepository.count();
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

    protected FeedItem getPersistedFeedItem(FeedItem feedItem) {
        return feedItemRepository.findById(feedItem.getId()).orElseThrow();
    }

    protected void assertPersistedFeedItemToMatchAllProperties(FeedItem expectedFeedItem) {
        assertFeedItemAllPropertiesEquals(expectedFeedItem, getPersistedFeedItem(expectedFeedItem));
    }

    protected void assertPersistedFeedItemToMatchUpdatableProperties(FeedItem expectedFeedItem) {
        assertFeedItemAllUpdatablePropertiesEquals(expectedFeedItem, getPersistedFeedItem(expectedFeedItem));
    }
}
