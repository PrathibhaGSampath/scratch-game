package com.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScratchGameTest {
    private ScratchGame game;
    private ConfigLoader mockConfigLoader;
    private MatrixGenerator mockMatrixGenerator;
    private RewardCalculator mockRewardCalculator;

    @BeforeEach
    void setUp() {
        mockConfigLoader = mock(ConfigLoader.class);
        game = new ScratchGame();
        game.configLoader = mockConfigLoader;
    }

    @Test
    void testLoadConfigs() {
        Map<String, Object> mockConfigs = Map.of(
                "symbols", List.of(new Symbol("A", "standard", 1.0)),
                "standardSymbolProbabilities", List.of(new StandardSymbolProbability("A", 0.5)),
                "bonusSymbolProbabilities", List.of(new BonusSymbolProbability("5x", 0.2))
        );

        when(mockConfigLoader.loadConfigs()).thenReturn(mockConfigs);

        game.loadConfigs();

        assertNotNull(game.configs);
        assertEquals(mockConfigs, game.configs);
    }

    @Test
    void testPlayGame() {
        List<Symbol> symbols = List.of(new Symbol("A", "standard", 1.0));
        List<StandardSymbolProbability> standardProbabilities = List.of(new StandardSymbolProbability("A", 0.5));
        List<BonusSymbolProbability> bonusProbabilities = List.of(new BonusSymbolProbability("5x", 0.2));
        List<Combination> expectedCombinations = List.of(new Combination(List.of(new Symbol("A", "standard", 1.0))));

        Map<String, Object> mockConfigs = Map.of(
                "symbols", symbols,
                "standardSymbolProbabilities", standardProbabilities,
                "bonusSymbolProbabilities", bonusProbabilities
        );

        when(mockConfigLoader.loadConfigs()).thenReturn(mockConfigs);
        game.loadConfigs();

        MatrixGenerator mockMatrixGenerator = mock(MatrixGenerator.class);
        when(mockMatrixGenerator.generateCombination()).thenReturn(expectedCombinations);

        MatrixGenerator actualMatrixGenerator = new MatrixGenerator(symbols, standardProbabilities, bonusProbabilities);
        assertEquals(expectedCombinations, actualMatrixGenerator.generateCombination());
    }

    @Test
    void testFindWinningCombinations() {
        List<Symbol> symbols = List.of(new Symbol("A", "standard", 1.0));
        List<WinningCombination> winningCombination = List.of(new WinningCombination("same_symbol_3_times"));

        Map<String,
