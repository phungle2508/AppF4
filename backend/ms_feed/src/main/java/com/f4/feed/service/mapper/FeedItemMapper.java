package com.f4.feed.service.mapper;

import com.f4.feed.domain.FeedItem;
import com.f4.feed.service.dto.FeedItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FeedItem} and its DTO {@link FeedItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface FeedItemMapper extends EntityMapper<FeedItemDTO, FeedItem> {}
