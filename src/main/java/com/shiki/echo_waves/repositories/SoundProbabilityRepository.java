package com.shiki.echo_waves.repositories;

import com.shiki.echo_waves.models.SoundProbability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SoundProbabilityRepository extends JpaRepository<SoundProbability, Integer> {
    List<SoundProbability> findByBoxContentId(Integer boxContentId);
    List<SoundProbability> findBySoundId(Integer soundId);
} 