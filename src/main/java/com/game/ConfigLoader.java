package com.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ConfigLoader {

    private String configFilePath;

    public ConfigLoader(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    public Map<String, Object> loadConfigs() {
        Map<String, Object> result = new HashMap<>();
        List<StandardSymbolProbability> standardSymbolProbabilities = new ArrayList<>();
        List<BonusSymbolProbability> bonusSymbolProbabilities = new ArrayList<>();
        List<Symbol> symbols = new ArrayList<>();
        List<WinningCombination> winningCombinations = new ArrayList<>();
        
        try {
            // Read the JSON file content
            String content = Files.readString(Paths.get(configFilePath));
            JSONObject jsonObject = new JSONObject(content);

            // Parse symbols
            JSONObject symbolsJson = jsonObject.getJSONObject("symbols");

            for (String key : symbolsJson.keySet()) {
                JSONObject symbolJson = symbolsJson.getJSONObject(key);
                String type = symbolJson.getString("type");
                double rewardMultiplier = symbolJson.optDouble("reward_multiplier", 0.0);

                if ("standard".equals(type)) {
                    symbols.add(new Symbol(key, type, rewardMultiplier));
                } else if ("bonus".equals(type)) {
                    String impact = symbolJson.getString("impact");

                    switch (impact) {
                        case "multiply_reward":
                            symbols.add(new BonusSymbol(key, type, rewardMultiplier, impact));
                            break;

                        case "extra_bonus":
                            double extraBonus = symbolJson.getDouble("extra");
                            symbols.add(new BonusSymbol(key, type, extraBonus, impact));
                            break;

                        case "miss":
                            symbols.add(new BonusSymbol(key, type, rewardMultiplier, impact));
                            break;

                        default:
                            throw new IllegalArgumentException("Unknown impact type: " + impact);
                    }
                } else {
                    throw new IllegalArgumentException("Unknown symbol type: " + type);
                }
            }
            result.put("symbols", symbols);
            // Parse probabilities
            JSONObject probabilitiesJson = jsonObject.getJSONObject("probabilities");

            // Standard Symbols
            JSONArray standardSymbolsArray = probabilitiesJson.getJSONArray("standard_symbols");
            for (int i = 0; i < standardSymbolsArray.length(); i++) {
                JSONObject gridPosition = standardSymbolsArray.getJSONObject(i);
                int column = gridPosition.getInt("column");
                int row = gridPosition.getInt("row");
                JSONObject gridSymbols = gridPosition.getJSONObject("symbols");

                Map<String, Integer> symbolProbabilities = new HashMap<>();
                for (String symbolKey : gridSymbols.keySet()) {
                    int probability = gridSymbols.getInt(symbolKey);
                    symbolProbabilities.put(symbolKey, probability);
                }

                standardSymbolProbabilities.add(new StandardSymbolProbability(row, column, symbolProbabilities));
            }
            result.put("standardSymbolProbabilities", standardSymbolProbabilities);
            // Bonus Symbols
            JSONObject bonusSymbolsJson = probabilitiesJson.getJSONObject("bonus_symbols").getJSONObject("symbols");
            for (String bonusSymbolKey : bonusSymbolsJson.keySet()) {
                int probability = bonusSymbolsJson.getInt(bonusSymbolKey);
                bonusSymbolProbabilities.add(new BonusSymbolProbability(bonusSymbolKey, probability));
            }
                    
            result.put("bonusSymbolProbabilities", bonusSymbolProbabilities);

            // Parse winnning combinations
            JSONObject winCombinationsJSON = jsonObject.getJSONObject("win_combinations");

            Map<String, Integer> combinations = new HashMap<>();
            for (String key : winCombinationsJSON.keySet()) {
                JSONObject wcJson = winCombinationsJSON.getJSONObject(key);
                String group = wcJson.getString("group");
                int count = wcJson.optInt("count", 0);
                Double rewardMultiplier = wcJson.optDouble("reward_multiplier", 0.0);

                winningCombinations.add(new WinningCombination(key, rewardMultiplier, count, group));
            }

            result.put("winningCombinations", winningCombinations);

        } catch (IOException e) {
            System.err.println("Error reading the configuration file: " + e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            System.err.println("Error parsing the JSON content: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid configuration data: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
    
}
