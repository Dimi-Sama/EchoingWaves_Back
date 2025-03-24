package com.shiki.echo_waves.repositories;

import com.shiki.echo_waves.models.Sound;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.shiki.echo_waves.models.SoundRarety;

@Repository
public interface SoundRepository extends JpaRepository<Sound, Integer> {
    List<Sound> findByType(String type);
    List<Sound> findByRarete(SoundRarety rarete);
} 