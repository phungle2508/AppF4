package com.f4.commentlike.kafka.handler.events;

import org.springframework.stereotype.Component;

import com.f4.commentlike.kafka.handler.EventHandler;
import com.f4.commentlike.service.LikeService;
import com.f4.commentlike.service.dto.LikeDTO;

@Component
public class PostReelHandler implements EventHandler<LikeDTO> {
    private final LikeService svc;

    public PostReelHandler(LikeService svc) {
        this.svc = svc;
    }

    public String getEventName() {
        return "postReel";
    }

    public void handle(LikeDTO dto) {
        svc.save(dto);
    }
}
