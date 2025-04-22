package com.f4.feed.web.rest;

import com.f4.feed.repository.FeedItemRepository;
import com.f4.feed.service.FeedItemService;
import com.f4.feed.service.dto.FeedItemDTO;
import com.f4.feed.web.rest.errors.BadRequestAlertException;
import com.f4.feed.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.f4.feed.domain.FeedItem}.
 */
@RestController
@RequestMapping("/api/feed-items")
public class FeedItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(FeedItemResource.class);

    private static final String ENTITY_NAME = "msFeedFeedItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeedItemService feedItemService;

    private final FeedItemRepository feedItemRepository;

    public FeedItemResource(FeedItemService feedItemService, FeedItemRepository feedItemRepository) {
        this.feedItemService = feedItemService;
        this.feedItemRepository = feedItemRepository;
    }

    /**
     * {@code POST  /feed-items} : Create a new feedItem.
     *
     * @param feedItemDTO the feedItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feedItemDTO, or with status {@code 400 (Bad Request)} if the feedItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FeedItemDTO> createFeedItem(@Valid @RequestBody FeedItemDTO feedItemDTO) throws URISyntaxException {
        LOG.debug("REST request to save FeedItem : {}", feedItemDTO);
        if (feedItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new feedItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        feedItemDTO = feedItemService.save(feedItemDTO);
        return ResponseEntity.created(new URI("/api/feed-items/" + feedItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, feedItemDTO.getId().toString()))
            .body(feedItemDTO);
    }

    /**
     * {@code PUT  /feed-items/:id} : Updates an existing feedItem.
     *
     * @param id the id of the feedItemDTO to save.
     * @param feedItemDTO the feedItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedItemDTO,
     * or with status {@code 400 (Bad Request)} if the feedItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feedItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FeedItemDTO> updateFeedItem(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody FeedItemDTO feedItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update FeedItem : {}, {}", id, feedItemDTO);
        if (feedItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        feedItemDTO = feedItemService.update(feedItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedItemDTO.getId().toString()))
            .body(feedItemDTO);
    }

    /**
     * {@code PATCH  /feed-items/:id} : Partial updates given fields of an existing feedItem, field will ignore if it is null
     *
     * @param id the id of the feedItemDTO to save.
     * @param feedItemDTO the feedItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedItemDTO,
     * or with status {@code 400 (Bad Request)} if the feedItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the feedItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the feedItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeedItemDTO> partialUpdateFeedItem(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody FeedItemDTO feedItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FeedItem partially : {}, {}", id, feedItemDTO);
        if (feedItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeedItemDTO> result = feedItemService.partialUpdate(feedItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /feed-items} : get all the feedItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feedItems in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FeedItemDTO>> getAllFeedItems(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of FeedItems");
        Page<FeedItemDTO> page = feedItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /feed-items/:id} : get the "id" feedItem.
     *
     * @param id the id of the feedItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feedItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FeedItemDTO> getFeedItem(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get FeedItem : {}", id);
        Optional<FeedItemDTO> feedItemDTO = feedItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(feedItemDTO);
    }

    /**
     * {@code DELETE  /feed-items/:id} : delete the "id" feedItem.
     *
     * @param id the id of the feedItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedItem(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete FeedItem : {}", id);
        feedItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /feed-items/_search?query=:query} : search for the feedItem corresponding
     * to the query.
     *
     * @param query the query of the feedItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<FeedItemDTO>> searchFeedItems(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of FeedItems for query {}", query);
        try {
            Page<FeedItemDTO> page = feedItemService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
