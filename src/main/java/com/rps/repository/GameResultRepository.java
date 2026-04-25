package com.rps.repository;

import com.rps.model.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {

    long countByResult(String result);

    List<GameResult> findTop10ByOrderByPlayedAtDesc();

    @Query("SELECT g FROM GameResult g ORDER BY g.playedAt DESC")
    List<GameResult> findAllOrderByPlayedAtDesc();
}
