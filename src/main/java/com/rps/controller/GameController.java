package com.rps.controller;

import com.rps.model.*;
import com.rps.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private GameService gameService;

    /**
     * POST /api/game/play
     * Body: { "move": "ROCK" }
     * Play a round against the computer.
     */
    @PostMapping("/play")
    public ResponseEntity<GameResponse> play(@RequestBody GameRequest request) {
        try {
            GameResponse response = gameService.playGame(request.getMove());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET /api/game/stats
     * Get total wins, losses, draws, and win rate.
     */
    @GetMapping("/stats")
    public ResponseEntity<GameStats> getStats() {
        return ResponseEntity.ok(gameService.getStats());
    }

    /**
     * GET /api/game/history
     * Get last 10 games.
     */
    @GetMapping("/history")
    public ResponseEntity<List<GameResult>> getHistory() {
        return ResponseEntity.ok(gameService.getHistory());
    }

    /**
     * GET /api/game/history/all
     * Get all game history.
     */
    @GetMapping("/history/all")
    public ResponseEntity<List<GameResult>> getAllHistory() {
        return ResponseEntity.ok(gameService.getAllHistory());
    }

    /**
     * DELETE /api/game/reset
     * Clear all game history and reset stats.
     */
    @DeleteMapping("/reset")
    public ResponseEntity<Map<String, String>> resetStats() {
        gameService.resetStats();
        return ResponseEntity.ok(Map.of("message", "Game stats reset successfully!"));
    }

    /**
     * GET /api/game/health
     * Simple health check endpoint.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "game", "Rock Paper Scissors"));
    }
}
