package com.f4.commentlike.service.mapper;

import com.f4.commentlike.domain.Like;
import com.f4.commentlike.service.dto.LikeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Like} and its DTO {@link LikeDTO}.
 */
@Mapper(componentModel = "spring")
public interface LikeMapper extends EntityMapper<LikeDTO, Like> {}
