package com.game;

public class BonusSymbol extends Symbol {
    private String impact;

    public BonusSymbol(String name, String type, Double rewardMultiplier, String impact) {
        super(name, type, rewardMultiplier);
        this.impact = impact;
    }

    public String getImpact(){
        return impact;
    }

    @Override
    public String toString() {
        return "BonusSymbol{" +
                "name='" + getName() + '\'' +
                ", type='" + getType() + '\'' +
                ", impact='" + getImpact() + '\'' +
                ", rewardMultiplier=" + getRewardMultiplier() +
                '}';
    }
}
