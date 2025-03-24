package com.shiki.echo_waves.repositories;

import com.shiki.echo_waves.models.UsersCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsersCollectionRepository extends JpaRepository<UsersCollection, Integer> {
    Optional<UsersCollection> findByUserId(Integer userId);
} 