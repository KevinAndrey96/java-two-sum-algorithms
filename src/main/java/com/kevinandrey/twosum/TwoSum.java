package com.kevinandrey.twosum;

import java.util.Optional;

/**
 * Finds two distinct positions i &lt; j with {@code values[i] + values[j] == target}.
 *
 * <p>Sums are computed in {@code long}, so values near {@link Integer#MAX_VALUE} never overflow.
 * When several pairs qualify, each strategy documents which one it returns.
 */
public sealed interface TwoSum permits BruteForceTwoSum, TwoPointersTwoSum, HashMapTwoSum {

    /**
     * Searches for a pair adding up to the target.
     *
     * @param values the array, never modified
     * @param target the expected sum
     * @return the pair, or empty if none exists
     */
    Optional<IndexPair> find(int[] values, long target);

    /**
     * Spanish name shown by the command line interface.
     *
     * @return the strategy name
     */
    String name();

    /**
     * Asymptotic cost in time and extra memory.
     *
     * @return a short description such as "O(n) tiempo, O(n) memoria"
     */
    String complexity();
}
