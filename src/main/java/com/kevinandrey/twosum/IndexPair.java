package com.kevinandrey.twosum;

/**
 * Two distinct positions of an array, always in increasing order.
 *
 * @param first the smaller index
 * @param second the larger index
 */
public record IndexPair(int first, int second) {

    /** Validates that the indices are distinct, non-negative and ordered. */
    public IndexPair {
        if (first < 0 || second <= first) {
            throw new IllegalArgumentException("indices must satisfy 0 <= first < second");
        }
    }

    /**
     * Builds the pair from two distinct indices in any order.
     *
     * @param a one index
     * @param b another index
     * @return the ordered pair
     */
    public static IndexPair of(int a, int b) {
        return new IndexPair(Math.min(a, b), Math.max(a, b));
    }
}
