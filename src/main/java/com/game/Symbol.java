package com.game;

public class Symbol {
    private String name;
    private String type;
    private Double rewardMultiplier;

    public Symbol(String name, String type, Double rewardMultiplier) {
        this.name = name;
        this.type = type;
        this.rewardMultiplier = rewardMultiplier;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Double getRewardMultiplier() {
        return rewardMultiplier;
    }

    @Override
    public String toString() {
        return "Symbol{name='" + name + "', type='" + type + "', rewardMultiplier=" + rewardMultiplier + "}";
    }
}

