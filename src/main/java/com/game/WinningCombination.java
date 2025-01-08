package com.game;

import java.util.*;

public class WinningCombination {
    private final String name;
    private final double rewardMultiplier;
    private final int count;
    private final String group;

    public WinningCombination(String name, double rewardMultiplier, int count, String group) {
        this.name = name;
        this.rewardMultiplier = rewardMultiplier;
        this.count = count;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public String getGroup() {
        return group;
    }

    public Double getRewardMultiplier() {
        return rewardMultiplier;
    }

    @Override
    public String toString() {
        return "WinCombination{" +
                "name='" + getName() + '\'' +
                ", group=" + getGroup() +
                ", count=" + getCount() +
                ", rewardMultiplier=" + getRewardMultiplier() +
                '}';
    }

    public static Map<String, List<WinningCombination>> findWinningCombinations(
            List<Combination> matrix,
            List<WinningCombination> combinations,
            List<Symbol> symbols
    ) {
        Map<String, List<WinningCombination>> winningCombinations = new HashMap<>();

        for (WinningCombination wc : combinations) {
            checkWinning(matrix, wc, winningCombinations, symbols);
        }

        return winningCombinations;
    }

    private static void checkWinning(List<Combination> matrix, WinningCombination wc, Map<String, List<WinningCombination>> result, List<Symbol> symbols) {
        List<String> matchedSymbols = new ArrayList<>();

        // Create and populate the symbolCountMap
        HashMap<Symbol, Integer> symbolCountMap = new HashMap<>();
        for (int row = 0; row < matrix.size(); row++) {
            Combination combination = matrix.get(row);
            for (int col = 0; col < combination.getSymbols().size(); col++) {
                Symbol symbol = combination.getSymbols().get(col);
                if (symbols.stream().anyMatch(s -> s.getName().equals(symbol.getName()))) {
                    symbolCountMap.put(symbol, symbolCountMap.getOrDefault(symbol, 0) + 1);
                }
            }
        }

        // Iterate through the matrix again, checking for winning combinations
        for (int row = 0; row < matrix.size(); row++) {
            Combination combination = matrix.get(row);
            for (int col = 0; col < combination.getSymbols().size(); col++) {
                Symbol symbol = combination.getSymbols().get(col);
                if (symbols.stream().anyMatch(s -> s.getName().equals(symbol.getName()))) {
                    int count = wc.getCount();
                    if (count == 0) {
                        count = matrix.size();
                    } 
                    if (countOccurrences(matrix, row, col, symbol, count, wc.getGroup(), symbolCountMap)) {
                        matchedSymbols.add(symbol.getName());
                    }
                }
            }
        }

        if (!matchedSymbols.isEmpty()) {
            result.putIfAbsent(matchedSymbols.get(0), new ArrayList<>());
            result.get(matchedSymbols.get(0)).add(wc);
        }
    }

    private static boolean countOccurrences(
            List<Combination> matrix,
            int row,
            int col,
            Symbol symbol,
            int count,
            String groupName,
            HashMap<Symbol, Integer> symbolCountMap
    ) {
        switch (groupName) {
            case "same_symbols":
                return symbolCountMap.getOrDefault(symbol, 0) == count;
            case "vertically_linear_symbols":
                return countVertical(matrix, row, col, symbol) == count;
            case "horizontally_linear_symbols":
                return countHorizontal(matrix, row, col, symbol) == count;
            case "ltr_diagonally_linear_symbols":
                return countDiagonalLeftToRight(matrix, row, col, symbol) == count;
            case "rtl_diagonally_linear_symbols":
                return countDiagonalRightToLeft(matrix, row, col, symbol) == count;
            default:
                return false;
        }
    }

    private static int countHorizontal(List<Combination> matrix, int row, int col, Symbol symbol) {
        int matched = 0;
        Combination combination = matrix.get(row);

        for (int c = col; c < combination.getSymbols().size(); c++) {
            if (combination.getSymbols().get(c).equals(symbol)) {
                matched++;
            } else {
                break;
            }
        }

        return matched;
    }

    private static int countVertical(List<Combination> matrix, int row, int col, Symbol symbol) {
        int matched = 0;

        for (int r = row; r < matrix.size(); r++) {
            Symbol currentSymbol = matrix.get(r).getSymbols().get(col);
            if (currentSymbol.equals(symbol)) {
                matched++;
            } else {
                break;
            }
        }

        return matched;
    }

    private static int countDiagonalLeftToRight(List<Combination> matrix, int row, int col, Symbol symbol) {
        int matched = 0;
        int r = row, c = col;

        while (r < matrix.size() && c < matrix.get(r).getSymbols().size()) {
            Symbol currentSymbol = matrix.get(r).getSymbols().get(c);
            if (currentSymbol.equals(symbol)) {
                matched++;
            } else {
                break;
            }
            r++;
            c++;
        }

        return matched;
    }

    private static int countDiagonalRightToLeft(List<Combination> matrix, int row, int col, Symbol symbol) {
        int matched = 0;
        int r = row, c = col;

        while (r < matrix.size() && c >= 0) {
            Symbol currentSymbol = matrix.get(r).getSymbols().get(c);
            if (currentSymbol.equals(symbol)) {
                matched++;
            } else {
                break;
            }
            r++;
            c--;
        }

        return matched;
    }
}
