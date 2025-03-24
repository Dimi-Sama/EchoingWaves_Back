package com.shiki.echo_waves.repositories;

import com.shiki.echo_waves.models.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BoxRepository extends JpaRepository<Box, Integer> {
    List<Box> findByHidden(Boolean hidden);
    List<Box> findByNomContainingIgnoreCase(String nom);
} 