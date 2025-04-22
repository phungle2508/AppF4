package com.f4.reel.service.impl;

import com.f4.reel.domain.Reel;
import com.f4.reel.repository.ReelRepository;
import com.f4.reel.repository.search.ReelSearchRepository;
import com.f4.reel.service.ReelService;
import com.f4.reel.service.dto.ReelDTO;
import com.f4.reel.service.mapper.ReelMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.f4.reel.domain.Reel}.
 */
@Service
@Transactional
public class ReelServiceImpl implements ReelService {

    private static final Logger LOG = LoggerFactory.getLogger(ReelServiceImpl.class);

    private final ReelRepository reelRepository;

    private final ReelMapper reelMapper;

    private final ReelSearchRepository reelSearchRepository;

    public ReelServiceImpl(ReelRepository reelRepository, ReelMapper reelMapper, ReelSearchRepository reelSearchRepository) {
        this.reelRepository = reelRepository;
        this.reelMapper = reelMapper;
        this.reelSearchRepository = reelSearchRepository;
    }

    @Override
    public ReelDTO save(ReelDTO reelDTO) {
        LOG.debug("Request to save Reel : {}", reelDTO);
        Reel reel = reelMapper.toEntity(reelDTO);
        reel = reelRepository.save(reel);
        reelSearchRepository.index(reel);
        return reelMapper.toDto(reel);
    }

    @Override
    public ReelDTO update(ReelDTO reelDTO) {
        LOG.debug("Request to update Reel : {}", reelDTO);
        Reel reel = reelMapper.toEntity(reelDTO);
        reel = reelRepository.save(reel);
        reelSearchRepository.index(reel);
        return reelMapper.toDto(reel);
    }

    @Override
    public Optional<ReelDTO> partialUpdate(ReelDTO reelDTO) {
        LOG.debug("Request to partially update Reel : {}", reelDTO);

        return reelRepository
            .findById(reelDTO.getId())
            .map(existingReel -> {
                reelMapper.partialUpdate(existingReel, reelDTO);

                return existingReel;
            })
            .map(reelRepository::save)
            .map(savedReel -> {
                reelSearchRepository.index(savedReel);
                return savedReel;
            })
            .map(reelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReelDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Reels");
        return reelRepository.findAll(pageable).map(reelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReelDTO> findOne(UUID id) {
        LOG.debug("Request to get Reel : {}", id);
        return reelRepository.findById(id).map(reelMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Reel : {}", id);
        reelRepository.deleteById(id);
        reelSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReelDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Reels for query {}", query);
        return reelSearchRepository.search(query, pageable).map(reelMapper::toDto);
    }
}
