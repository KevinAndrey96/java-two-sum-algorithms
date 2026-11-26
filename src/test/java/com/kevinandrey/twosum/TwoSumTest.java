package com.kevinandrey.twosum;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TwoSumTest {

    static Stream<TwoSum> strategies() {
        return Stream.of(new BruteForceTwoSum(), new TwoPointersTwoSum(), new HashMapTwoSum());
    }

    @ParameterizedTest
    @MethodSource("strategies")
    void solvesTheClassicExample(TwoSum strategy) {
        assertEquals(Optional.of(new IndexPair(0, 1)), strategy.find(new int[] {2, 7, 11, 15}, 9));
    }

    @ParameterizedTest
    @MethodSource("strategies")
    void neverUsesTheSamePositionTwice(TwoSum strategy) {
        assertEquals(Optional.empty(), strategy.find(new int[] {3}, 6));
        assertEquals(Optional.of(new IndexPair(1, 2)), strategy.find(new int[] {3, 2, 4}, 6));
    }

    @ParameterizedTest
    @MethodSource("strategies")
    void equalValuesInDifferentPositionsCount(TwoSum strategy) {
        assertEquals(Optional.of(new IndexPair(0, 1)), strategy.find(new int[] {3, 3}, 6));
    }

    @ParameterizedTest
    @MethodSource("strategies")
    void reportsMissingPairs(TwoSum strategy) {
        assertEquals(Optional.empty(), strategy.find(new int[] {}, 0));
        assertEquals(Optional.empty(), strategy.find(new int[] {1, 2, 4, 8}, 7 + 8));
    }

    @ParameterizedTest
    @MethodSource("strategies")
    void handlesNegativeValues(TwoSum strategy) {
        assertEquals(Optional.of(new IndexPair(1, 3)), strategy.find(new int[] {5, -4, 9, -6}, -10));
    }

    @ParameterizedTest
    @MethodSource("strategies")
    void sumsNeverOverflow(TwoSum strategy) {
        int[] extremes = {Integer.MAX_VALUE, 1, Integer.MAX_VALUE};

        assertEquals(Optional.empty(), strategy.find(extremes, Integer.MIN_VALUE));
        assertEquals(Optional.of(new IndexPair(0, 2)), strategy.find(extremes, 2L * Integer.MAX_VALUE));
        assertEquals(Optional.of(new IndexPair(0, 1)),
                strategy.find(new int[] {Integer.MIN_VALUE, Integer.MIN_VALUE}, 2L * Integer.MIN_VALUE));
    }

    @ParameterizedTest
    @MethodSource("strategies")
    void leavesTheInputUntouched(TwoSum strategy) {
        int[] values = {9, 1, 8, 2, 7};
        strategy.find(values, 3);

        assertArrayEquals(new int[] {9, 1, 8, 2, 7}, values);
    }

    @ParameterizedTest
    @MethodSource("strategies")
    void agreesWithBruteForceOnRandomArrays(TwoSum strategy) {
        Random random = new Random(20190906);
        TwoSum reference = new BruteForceTwoSum();
        for (int round = 0; round < 3_000; round++) {
            int[] values = random.ints(random.nextInt(25), -30, 31).toArray();
            long target = random.nextInt(121) - 60;

            Optional<IndexPair> pair = strategy.find(values, target);

            assertEquals(reference.find(values, target).isPresent(), pair.isPresent());
            pair.ifPresent(p -> assertEquals(target, (long) values[p.first()] + values[p.second()]));
        }
    }

    @Test
    void bruteForceReturnsTheLexicographicallySmallestPair() {
        assertEquals(Optional.of(new IndexPair(0, 3)),
                new BruteForceTwoSum().find(new int[] {1, 4, 2, 5, 3}, 6));
    }

    @Test
    void hashMapReturnsThePairThatCompletesFirst() {
        assertEquals(Optional.of(new IndexPair(1, 2)),
                new HashMapTwoSum().find(new int[] {1, 4, 2, 5, 3}, 6));
    }

    @Test
    void strategiesDescribeThemselves() {
        strategies().forEach(strategy -> {
            assertTrue(strategy.name().length() > 3);
            assertTrue(strategy.complexity().startsWith("O("));
        });
    }

    @Test
    void indexPairsAreOrderedAndDistinct() {
        assertEquals(new IndexPair(2, 5), IndexPair.of(5, 2));
        assertThrows(IllegalArgumentException.class, () -> new IndexPair(3, 3));
        assertThrows(IllegalArgumentException.class, () -> new IndexPair(4, 1));
        assertThrows(IllegalArgumentException.class, () -> new IndexPair(-1, 1));
    }
}
