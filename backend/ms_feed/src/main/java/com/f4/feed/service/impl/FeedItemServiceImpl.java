package com.f4.feed.service.impl;

import com.f4.feed.domain.FeedItem;
import com.f4.feed.repository.FeedItemRepository;
import com.f4.feed.repository.search.FeedItemSearchRepository;
import com.f4.feed.service.FeedItemService;
import com.f4.feed.service.dto.FeedItemDTO;
import com.f4.feed.service.mapper.FeedItemMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.f4.feed.domain.FeedItem}.
 */
@Service
@Transactional
public class FeedItemServiceImpl implements FeedItemService {

    private static final Logger LOG = LoggerFactory.getLogger(FeedItemServiceImpl.class);

    private final FeedItemRepository feedItemRepository;

    private final FeedItemMapper feedItemMapper;

    private final FeedItemSearchRepository feedItemSearchRepository;

    public FeedItemServiceImpl(
        FeedItemRepository feedItemRepository,
        FeedItemMapper feedItemMapper,
        FeedItemSearchRepository feedItemSearchRepository
    ) {
        this.feedItemRepository = feedItemRepository;
        this.feedItemMapper = feedItemMapper;
        this.feedItemSearchRepository = feedItemSearchRepository;
    }

    @Override
    public FeedItemDTO save(FeedItemDTO feedItemDTO) {
        LOG.debug("Request to save FeedItem : {}", feedItemDTO);
        FeedItem feedItem = feedItemMapper.toEntity(feedItemDTO);
        feedItem = feedItemRepository.save(feedItem);
        feedItemSearchRepository.index(feedItem);
        return feedItemMapper.toDto(feedItem);
    }

    @Override
    public FeedItemDTO update(FeedItemDTO feedItemDTO) {
        LOG.debug("Request to update FeedItem : {}", feedItemDTO);
        FeedItem feedItem = feedItemMapper.toEntity(feedItemDTO);
        feedItem = feedItemRepository.save(feedItem);
        feedItemSearchRepository.index(feedItem);
        return feedItemMapper.toDto(feedItem);
    }

    @Override
    public Optional<FeedItemDTO> partialUpdate(FeedItemDTO feedItemDTO) {
        LOG.debug("Request to partially update FeedItem : {}", feedItemDTO);

        return feedItemRepository
            .findById(feedItemDTO.getId())
            .map(existingFeedItem -> {
                feedItemMapper.partialUpdate(existingFeedItem, feedItemDTO);

                return existingFeedItem;
            })
            .map(feedItemRepository::save)
            .map(savedFeedItem -> {
                feedItemSearchRepository.index(savedFeedItem);
                return savedFeedItem;
            })
            .map(feedItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedItemDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all FeedItems");
        return feedItemRepository.findAll(pageable).map(feedItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FeedItemDTO> findOne(UUID id) {
        LOG.debug("Request to get FeedItem : {}", id);
        return feedItemRepository.findById(id).map(feedItemMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete FeedItem : {}", id);
        feedItemRepository.deleteById(id);
        feedItemSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedItemDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of FeedItems for query {}", query);
        return feedItemSearchRepository.search(query, pageable).map(feedItemMapper::toDto);
    }
}
