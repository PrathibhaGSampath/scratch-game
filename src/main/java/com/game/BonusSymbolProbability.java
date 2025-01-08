package com.game;

import java.util.Map;
import java.util.HashMap;

public class BonusSymbolProbability {
    private String symbolKey;
    private Integer probability;

    public BonusSymbolProbability(String symbolKey, Integer probability) {
        this.symbolKey = symbolKey;
        this.probability = probability;
    }

    public String getSymbol() {
        return symbolKey;
    }

    public int getProbability() {
        return probability;
    }
}
