package com.kevinandrey.twosum;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * One pass that remembers the first position of every value seen: O(n) expected time and O(n)
 * memory. Returns the pair with the smallest second index, as soon as it appears.
 */
public final class HashMapTwoSum implements TwoSum {

    @Override
    public Optional<IndexPair> find(int[] values, long target) {
        Map<Long, Integer> firstPosition = new HashMap<>();
        for (int index = 0; index < values.length; index++) {
            Integer complement = firstPosition.get(target - values[index]);
            if (complement != null) {
                return Optional.of(new IndexPair(complement, index));
            }
            firstPosition.putIfAbsent((long) values[index], index);
        }
        return Optional.empty();
    }

    @Override
    public String name() {
        return "Tabla hash";
    }

    @Override
    public String complexity() {
        return "O(n) tiempo esperado, O(n) memoria";
    }
}
