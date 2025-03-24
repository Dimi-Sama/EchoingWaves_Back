package com.shiki.echo_waves.repositories;

import com.shiki.echo_waves.models.Tirage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TirageRepository extends JpaRepository<Tirage, Integer> {
    List<Tirage> findByUserId(Integer userId);
    List<Tirage> findByBoxId(Integer boxId);
} 