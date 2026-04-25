package com.rps.service;

import com.rps.model.*;
import com.rps.repository.GameResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class GameService {

    @Autowired
    private GameResultRepository gameResultRepository;

    private static final String[] MOVES = {"ROCK", "PAPER", "SCISSORS"};
    private final Random random = new Random();

    /**
     * Play a round: determine computer move, evaluate result, persist, return response.
     */
    public GameResponse playGame(String playerMove) {
        playerMove = playerMove.toUpperCase().trim();
        validateMove(playerMove);

        String computerMove = getComputerMove();
        String result = determineResult(playerMove, computerMove);
        String message = buildMessage(playerMove, computerMove, result);

        // Persist result to DB
        GameResult gameResult = new GameResult();
        gameResult.setPlayerMove(playerMove);
        gameResult.setComputerMove(computerMove);
        gameResult.setResult(result);
        GameResult saved = gameResultRepository.save(gameResult);

        return new GameResponse(playerMove, computerMove, result, message, saved.getId());
    }

    /**
     * Get aggregated game statistics.
     */
    public GameStats getStats() {
        long total = gameResultRepository.count();
        long wins = gameResultRepository.countByResult("WIN");
        long losses = gameResultRepository.countByResult("LOSE");
        long draws = gameResultRepository.countByResult("DRAW");
        double winRate = total > 0 ? Math.round((wins * 100.0 / total) * 10.0) / 10.0 : 0.0;

        return new GameStats(total, wins, losses, draws, winRate);
    }

    /**
     * Get last 10 game history entries.
     */
    public List<GameResult> getHistory() {
        return gameResultRepository.findTop10ByOrderByPlayedAtDesc();
    }

    /**
     * Get full game history.
     */
    public List<GameResult> getAllHistory() {
        return gameResultRepository.findAllOrderByPlayedAtDesc();
    }

    /**
     * Reset / delete all game results.
     */
    public void resetStats() {
        gameResultRepository.deleteAll();
    }

    // ---- Private helpers ----

    private String getComputerMove() {
        return MOVES[random.nextInt(MOVES.length)];
    }

    private void validateMove(String move) {
        if (!move.equals("ROCK") && !move.equals("PAPER") && !move.equals("SCISSORS")) {
            throw new IllegalArgumentException("Invalid move: " + move + ". Must be ROCK, PAPER, or SCISSORS.");
        }
    }

    private String determineResult(String player, String computer) {
        if (player.equals(computer)) return "DRAW";

        boolean playerWins = (player.equals("ROCK") && computer.equals("SCISSORS"))
                || (player.equals("PAPER") && computer.equals("ROCK"))
                || (player.equals("SCISSORS") && computer.equals("PAPER"));

        return playerWins ? "WIN" : "LOSE";
    }

    private String buildMessage(String player, String computer, String result) {
        String verb = switch (player) {
            case "ROCK" -> computer.equals("SCISSORS") ? "Rock crushes Scissors!" : computer.equals("PAPER") ? "Paper covers Rock!" : "It's a tie!";
            case "PAPER" -> computer.equals("ROCK") ? "Paper covers Rock!" : computer.equals("SCISSORS") ? "Scissors cuts Paper!" : "It's a tie!";
            case "SCISSORS" -> computer.equals("PAPER") ? "Scissors cuts Paper!" : computer.equals("ROCK") ? "Rock crushes Scissors!" : "It's a tie!";
            default -> "";
        };

        return switch (result) {
            case "WIN" -> "🎉 You Win! " + verb;
            case "LOSE" -> "😞 You Lose! " + verb;
            case "DRAW" -> "🤝 It's a Draw! " + verb;
            default -> "";
        };
    }
}
