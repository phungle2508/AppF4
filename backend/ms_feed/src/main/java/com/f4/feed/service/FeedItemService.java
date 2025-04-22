package com.f4.feed.service;

import com.f4.feed.service.dto.FeedItemDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.f4.feed.domain.FeedItem}.
 */
public interface FeedItemService {
    /**
     * Save a feedItem.
     *
     * @param feedItemDTO the entity to save.
     * @return the persisted entity.
     */
    FeedItemDTO save(FeedItemDTO feedItemDTO);

    /**
     * Updates a feedItem.
     *
     * @param feedItemDTO the entity to update.
     * @return the persisted entity.
     */
    FeedItemDTO update(FeedItemDTO feedItemDTO);

    /**
     * Partially updates a feedItem.
     *
     * @param feedItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FeedItemDTO> partialUpdate(FeedItemDTO feedItemDTO);

    /**
     * Get all the feedItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FeedItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" feedItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FeedItemDTO> findOne(UUID id);

    /**
     * Delete the "id" feedItem.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the feedItem corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FeedItemDTO> search(String query, Pageable pageable);
}
