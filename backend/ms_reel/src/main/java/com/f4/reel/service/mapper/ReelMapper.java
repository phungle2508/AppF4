package com.f4.reel.service.mapper;

import com.f4.reel.domain.Reel;
import com.f4.reel.service.dto.ReelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reel} and its DTO {@link ReelDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReelMapper extends EntityMapper<ReelDTO, Reel> {}
