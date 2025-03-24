package com.shiki.echo_waves.repositories;

import com.shiki.echo_waves.models.BoxContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BoxContentRepository extends JpaRepository<BoxContent, Integer> {
    List<BoxContent> findByBoxId(Integer boxId);
} 