package com.game;

import java.util.List;
import java.util.Map;

public class RewardCalculator {
    private final List<Combination> combinations;
    private final Map<String, List<WinningCombination>> winningCombinationsMap; // Symbol to List of WinningCombinations
    private final double betAmount;

    public RewardCalculator(List<Combination> combinations, Map<String, List<WinningCombination>> winningCombinationsMap, double betAmount) {
        this.combinations = combinations;
        this.winningCombinationsMap = winningCombinationsMap;
        this.betAmount = betAmount;
    }

    public double calculate() {
        double totalReward = 0.0;
        if (winningCombinationsMap == null || winningCombinationsMap.isEmpty()) {
            return totalReward;
        }

        for (Combination combination : combinations) {
            for (Symbol symbol : combination.getSymbols()) {
                if (!"bonus".equalsIgnoreCase(symbol.getType())) {
                    double symbolReward = symbol.getRewardMultiplier();
                    List<WinningCombination> winningCombinations = winningCombinationsMap.get(symbol.getName());
                    double winRewards = 1.0;
                    if (winningCombinations != null && !winningCombinations.isEmpty()) {
                        for (WinningCombination wc : winningCombinations) {
                            winRewards *= wc.getRewardMultiplier();
                        }
                    }
                    totalReward += betAmount * symbolReward * winRewards;
                }
            }
            if (combination.hasBonusSymbol()) {
                double bonusMultiplier = combination.getBonusMultiplier();
                totalReward += bonusMultiplier;
            }
        }

        return totalReward;
    }
}
