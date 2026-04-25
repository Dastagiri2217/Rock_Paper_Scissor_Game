package com.rps.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameResponse {
    private String playerMove;
    private String computerMove;
    private String result;      // WIN, LOSE, DRAW
    private String message;     // Human-readable result message
    private Long gameId;
}
