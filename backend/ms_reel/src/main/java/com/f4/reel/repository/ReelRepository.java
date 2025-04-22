package com.f4.reel.repository;

import com.f4.reel.domain.Reel;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Reel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReelRepository extends JpaRepository<Reel, UUID> {}
