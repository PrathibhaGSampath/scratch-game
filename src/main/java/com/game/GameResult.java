package com.game;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameResult {
    private List<Combination> matrix;
    private double reward;
    private Map<String, List<WinningCombination>> appliedWinningCombinations;
    private String appliedBonusSymbol;

    public GameResult(List<Combination> matrix, double reward, Map<String, List<WinningCombination>> appliedWinningCombinations, String appliedBonusSymbol) {
        this.matrix = matrix;
        this.reward = reward;
        this.appliedWinningCombinations = appliedWinningCombinations;
        this.appliedBonusSymbol = appliedBonusSymbol;
    }

    public String transformToCustomJson() {
        List<List<String>> transformedMatrix = matrix.stream()
                .map(combination -> combination.getSymbols().stream()
                        .map(symbol -> {
                            return symbol.getName(); 
                        })
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        Map<String, List<String>> transformedWinningCombinations = appliedWinningCombinations.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(WinningCombination::getName)
                                .collect(Collectors.toList())
                ));

        return String.format(
                "{\n" +
                        "    \"matrix\": %s,\n" +
                        "    \"reward\": %.0f,\n" +
                        "    \"applied_winning_combinations\": %s,\n" +
                        "    \"applied_bonus_symbol\": \"%s\"\n" +
                        "}",
                transformedMatrix, reward, transformedWinningCombinations, appliedBonusSymbol
        );
    }
}
