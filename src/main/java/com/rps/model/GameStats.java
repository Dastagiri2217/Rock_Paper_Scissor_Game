package com.rps.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStats {
    private long totalGames;
    private long wins;
    private long losses;
    private long draws;
    private double winRate;
}
