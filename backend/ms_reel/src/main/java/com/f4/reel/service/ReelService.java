package com.f4.reel.service;

import com.f4.reel.service.dto.ReelDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.f4.reel.domain.Reel}.
 */
public interface ReelService {
    /**
     * Save a reel.
     *
     * @param reelDTO the entity to save.
     * @return the persisted entity.
     */
    ReelDTO save(ReelDTO reelDTO);

    /**
     * Updates a reel.
     *
     * @param reelDTO the entity to update.
     * @return the persisted entity.
     */
    ReelDTO update(ReelDTO reelDTO);

    /**
     * Partially updates a reel.
     *
     * @param reelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReelDTO> partialUpdate(ReelDTO reelDTO);

    /**
     * Get all the reels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReelDTO> findAll(Pageable pageable);

    /**
     * Get the "id" reel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReelDTO> findOne(UUID id);

    /**
     * Delete the "id" reel.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the reel corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReelDTO> search(String query, Pageable pageable);
}
