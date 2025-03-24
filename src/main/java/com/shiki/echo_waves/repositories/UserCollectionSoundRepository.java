package com.shiki.echo_waves.repositories;

import com.shiki.echo_waves.models.UserCollectionSound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserCollectionSoundRepository extends JpaRepository<UserCollectionSound, Integer> {
    List<UserCollectionSound> findByCollectionId(Integer collectionId);
    Optional<UserCollectionSound> findByCollectionIdAndSoundId(Integer collectionId, Integer soundId);
} 