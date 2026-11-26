package com.kevinandrey.twosum;

import java.util.Arrays;
import java.util.Optional;

/**
 * Sorts the positions by value and moves two pointers inward: O(n log n) time and O(n) memory
 * for the sorted positions, since the input array must keep its original order.
 *
 * <p>If the smallest plus the largest remaining value is too small, the smallest can never be part
 * of a solution and is discarded; if it is too large, the largest is discarded.
 */
public final class TwoPointersTwoSum implements TwoSum {

    @Override
    public Optional<IndexPair> find(int[] values, long target) {
        long[] sorted = new long[values.length];
        for (int index = 0; index < values.length; index++) {
            sorted[index] = (long) values[index] << Integer.SIZE | index;
        }
        Arrays.sort(sorted);
        int low = 0;
        int high = sorted.length - 1;
        while (low < high) {
            long sum = valueOf(sorted[low]) + valueOf(sorted[high]);
            if (sum == target) {
                return Optional.of(IndexPair.of(indexOf(sorted[low]), indexOf(sorted[high])));
            }
            if (sum < target) {
                low++;
            } else {
                high--;
            }
        }
        return Optional.empty();
    }

    /** The value sits in the high 32 bits, so sorting the packed longs sorts by value. */
    private static long valueOf(long packed) {
        return packed >> Integer.SIZE;
    }

    private static int indexOf(long packed) {
        return (int) packed;
    }

    @Override
    public String name() {
        return "Ordenamiento y dos punteros";
    }

    @Override
    public String complexity() {
        return "O(n log n) tiempo, O(n) memoria";
    }
}
