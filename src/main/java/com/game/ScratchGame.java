package com.game;

import java.util.Scanner;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class ScratchGame {
    private Map<String, Object> configs;
    private ConfigLoader configLoader;

    public ScratchGame() {
        this.configLoader = new ConfigLoader("src/main/resources/config.json");
    }

    public void loadConfigs() {
        this.configs = configLoader.loadConfigs();
    }

    public List<Combination> playGame() {
        List<Symbol> symbols = (List<Symbol>) configs.get("symbols");
        List<StandardSymbolProbability> standardProbabilities = (List<StandardSymbolProbability>) configs.get("standardSymbolProbabilities");
        List<BonusSymbolProbability> bonusProbabilities = (List<BonusSymbolProbability>) configs.get("bonusSymbolProbabilities");
        
        MatrixGenerator matrixGenerator = new MatrixGenerator(symbols, standardProbabilities, bonusProbabilities);
        return matrixGenerator.generateCombination();
    }

    public Map<String, List<WinningCombination>> findWinningCombinations(List<Combination> combinations) {
        List<Symbol> symbols = (List<Symbol>) configs.get("symbols");
        List<WinningCombination> winningCombination = (List<WinningCombination>) configs.get("winningCombinations");

        return WinningCombination.findWinningCombinations(combinations, winningCombination, symbols);
    }

    public Double calculateRewards(List<Combination> combinations, Map<String, List<WinningCombination>> winningCombinations, Double betAmount) {
        return new RewardCalculator(combinations, winningCombinations, betAmount).calculate();
    }

    public String getBonusSymbols(List<Combination> combinations){
        String result = "";
        for(Combination combination : combinations) {
            result += combination.hasBonusSymbolExceptMiss() ? combination.getBonusSymbolsExceptMiss() + ", " : "";
        }
        return result;
    }

    public void result(List<Combination> matrix, Double reward, Map<String, List<WinningCombination>> appliedWinningCombinations, String appliedBonusSymbol) {
         String result = new GameResult(matrix, reward, appliedWinningCombinations, appliedBonusSymbol).transformToCustomJson();

         if(reward == 0.0 ){
            System.out.println("LOST");
            System.out.println(result);
         } else {
            if (!appliedBonusSymbol.isEmpty()){
                System.out.println("Won with " + appliedBonusSymbol);
            }else{
                System.out.println("Won");
            }
            System.out.println(result);
         }
         
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your bet amount: ");
        double betAmount = scanner.nextDouble(); 

        ScratchGame game = new ScratchGame();
        game.loadConfigs();

        List<Combination> combinations = game.playGame();
        Map<String, List<WinningCombination>> winningCombinations = game.findWinningCombinations(combinations);

        Double reward = game.calculateRewards(combinations, winningCombinations, betAmount);
        String appliedBonusSymbol = game.getBonusSymbols(combinations);

        game.result(combinations, reward, winningCombinations, appliedBonusSymbol);

        scanner.close();
    }
}
