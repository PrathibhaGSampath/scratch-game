package com.game;

import java.util.Map;

public class StandardSymbolProbability {
    private int row;
    private int column;
    private Map<String, Integer> probabilities; // Map to hold symbol probabilities

    public StandardSymbolProbability(int row, int column, Map<String, Integer> probabilities) {
        this.row = row;
        this.column = column;
        this.probabilities = probabilities;
    }

    // Getters
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Map<String, Integer> getProbabilities() {
        return probabilities;
    }
}
