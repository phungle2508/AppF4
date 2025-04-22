package com.f4.reel.web.rest;

import com.f4.reel.repository.ReelRepository;
import com.f4.reel.service.ReelService;
import com.f4.reel.service.dto.ReelDTO;
import com.f4.reel.web.rest.errors.BadRequestAlertException;
import com.f4.reel.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link com.f4.reel.domain.Reel}.
 */
@RestController
@RequestMapping("/api/reels")
public class ReelResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReelResource.class);

    private static final String ENTITY_NAME = "msReelReel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReelService reelService;

    private final ReelRepository reelRepository;

    public ReelResource(ReelService reelService, ReelRepository reelRepository) {
        this.reelService = reelService;
        this.reelRepository = reelRepository;
    }

    /**
     * {@code POST  /reels} : Create a new reel.
     *
     * @param reelDTO the reelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reelDTO, or with status {@code 400 (Bad Request)} if the reel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReelDTO> createReel(@Valid @RequestBody ReelDTO reelDTO) throws URISyntaxException {
        LOG.debug("REST request to save Reel : {}", reelDTO);
        if (reelDTO.getId() != null) {
            throw new BadRequestAlertException("A new reel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reelDTO = reelService.save(reelDTO);
        return ResponseEntity.created(new URI("/api/reels/" + reelDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reelDTO.getId().toString()))
            .body(reelDTO);
    }

    /**
     * {@code PUT  /reels/:id} : Updates an existing reel.
     *
     * @param id the id of the reelDTO to save.
     * @param reelDTO the reelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reelDTO,
     * or with status {@code 400 (Bad Request)} if the reelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReelDTO> updateReel(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ReelDTO reelDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Reel : {}, {}", id, reelDTO);
        if (reelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reelDTO = reelService.update(reelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reelDTO.getId().toString()))
            .body(reelDTO);
    }

    /**
     * {@code PATCH  /reels/:id} : Partial updates given fields of an existing reel, field will ignore if it is null
     *
     * @param id the id of the reelDTO to save.
     * @param reelDTO the reelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reelDTO,
     * or with status {@code 400 (Bad Request)} if the reelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReelDTO> partialUpdateReel(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ReelDTO reelDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Reel partially : {}, {}", id, reelDTO);
        if (reelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReelDTO> result = reelService.partialUpdate(reelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reels} : get all the reels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reels in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReelDTO>> getAllReels(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Reels");
        Page<ReelDTO> page = reelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reels/:id} : get the "id" reel.
     *
     * @param id the id of the reelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReelDTO> getReel(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get Reel : {}", id);
        Optional<ReelDTO> reelDTO = reelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reelDTO);
    }

    /**
     * {@code DELETE  /reels/:id} : delete the "id" reel.
     *
     * @param id the id of the reelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReel(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete Reel : {}", id);
        reelService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /reels/_search?query=:query} : search for the reel corresponding
     * to the query.
     *
     * @param query the query of the reel search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ReelDTO>> searchReels(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of Reels for query {}", query);
        try {
            Page<ReelDTO> page = reelService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
