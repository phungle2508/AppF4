package com.f4.commentlike.service.mapper;

import com.f4.commentlike.domain.Comment;
import com.f4.commentlike.service.dto.CommentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {}
