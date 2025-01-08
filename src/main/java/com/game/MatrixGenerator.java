package com.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Map;

public class MatrixGenerator {
    private List<Symbol> symbols;
    private List<StandardSymbolProbability> standardProbabilities;
    private List<BonusSymbolProbability> bonusProbabilities;

    public MatrixGenerator(List<Symbol> symbols, List<StandardSymbolProbability> standardProbabilities, List<BonusSymbolProbability> bonusProbabilities) {
        this.symbols = symbols;
        this.standardProbabilities = standardProbabilities;
        this.bonusProbabilities = bonusProbabilities;
    }

    private String getRandomSymbolFromWeightedPool(List<StandardSymbolProbability> symbolProbabilities, int row, int col) {
        List<String> weightedPool = new ArrayList<>();
        StandardSymbolProbability probabilities = symbolProbabilities.stream()
            .filter(prob -> prob.getRow() == row && prob.getColumn() == col)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No probability found for row " + row + " and column " + col));

        for (Map.Entry<String, Integer> entry : probabilities.getProbabilities().entrySet()) {
            String symbol = entry.getKey();
            int weight = entry.getValue();

            for (int i = 0; i < weight; i++) {
                weightedPool.add(symbol);
            }
        }

        Random random = new Random();
        return weightedPool.get(random.nextInt(weightedPool.size()));
    }


    private String getRandomSymbolFromWeightedPool(List<BonusSymbolProbability> symbolProbabilities) {
        List<String> weightedPool = new ArrayList<>();

        for (BonusSymbolProbability entry : bonusProbabilities) {
            String symbol = entry.getSymbol();
            int weight = entry.getProbability();
            for (int i = 0; i < weight; i++) {
                weightedPool.add(symbol);
            }
        }

        Random random = new Random();
        return weightedPool.get(random.nextInt(weightedPool.size()));
    }

    public List<Combination> generateCombination() {
        Random random = new Random();
        List<Combination> combinations = new ArrayList<>();

        // Generate 3x3 matrix of combinations
        for (int row = 0; row < 3; row++) {
            Combination combination = new Combination();
            for (int col = 0; col < 3; col++) {
                String selectedSymbol;
                double randomDouble = random.nextDouble();
                if (randomDouble < 0.9) {
                    selectedSymbol = getRandomSymbolFromWeightedPool(standardProbabilities, row, col);
                } else {
                    selectedSymbol = getRandomSymbolFromWeightedPool(bonusProbabilities);
                }
                Symbol symbol = symbols.stream()
                    .filter(s -> s.getName().equals(selectedSymbol))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Symbol not found: " + selectedSymbol));

                combination.addSymbol(symbol);
            }
            combinations.add(combination);
        }

        return combinations;
    }
}
