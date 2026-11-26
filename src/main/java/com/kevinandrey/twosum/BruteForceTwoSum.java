package com.kevinandrey.twosum;

import java.util.Optional;

/**
 * Checks every pair i &lt; j: O(n^2) time and O(1) memory. Returns the lexicographically smallest
 * pair, which makes it the reference the other strategies are tested against.
 */
public final class BruteForceTwoSum implements TwoSum {

    @Override
    public Optional<IndexPair> find(int[] values, long target) {
        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                if ((long) values[i] + values[j] == target) {
                    return Optional.of(new IndexPair(i, j));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public String name() {
        return "Fuerza bruta";
    }

    @Override
    public String complexity() {
        return "O(n²) tiempo, O(1) memoria";
    }
}
