package com.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Combination {
    private List<Symbol> symbols;

    public Combination() {
        this.symbols = new ArrayList<>();
    }

    public void addSymbol(Symbol symbol) {
        symbols.add(symbol);
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }

    public boolean hasBonusSymbol() {
        return symbols.stream().anyMatch(symbol -> "bonus".equalsIgnoreCase(symbol.getType()));
    }

    public Double getBonusMultiplier() {
        return symbols.stream()
                .filter(symbol -> "bonus".equalsIgnoreCase(symbol.getType()))
                .mapToDouble(Symbol::getRewardMultiplier)
                .sum();
    }

    public boolean hasBonusSymbolExceptMiss() {
        return symbols.stream()
                    .filter(symbol -> symbol instanceof BonusSymbol)
                    .map(symbol -> (BonusSymbol) symbol)
                    .anyMatch(bonusSymbol -> "bonus".equalsIgnoreCase(bonusSymbol.getType()) && !"miss".equalsIgnoreCase(bonusSymbol.getImpact()));
    }

    public String getBonusSymbolsExceptMiss() {
        return symbols.stream()
                    .filter(symbol -> symbol instanceof BonusSymbol)
                    .map(symbol -> (BonusSymbol) symbol)
                    .filter(bonusSymbol -> "bonus".equalsIgnoreCase(bonusSymbol.getType()) && !"miss".equalsIgnoreCase(bonusSymbol.getImpact()))
                    .map(BonusSymbol::getName)
                    .collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return "Combination{" +
                "symbols=" + symbols +
                '}';
    }
}
