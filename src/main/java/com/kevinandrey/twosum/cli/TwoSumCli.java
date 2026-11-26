package com.kevinandrey.twosum.cli;

import com.kevinandrey.twosum.BruteForceTwoSum;
import com.kevinandrey.twosum.HashMapTwoSum;
import com.kevinandrey.twosum.IndexPair;
import com.kevinandrey.twosum.TwoPointersTwoSum;
import com.kevinandrey.twosum.TwoSum;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.random.RandomGenerator;

/** Runs the three strategies on the same array and prints their answers in Spanish. */
public final class TwoSumCli {

    static final int FOUND = 0;
    static final int NOT_FOUND = 1;
    static final int USAGE_ERROR = 2;

    static final int MAX_RANDOM_SIZE = 1_000_000;
    static final int RANDOM_BOUND = 99;

    private static final String RANDOM_FLAG = "--aleatorio";
    private static final List<TwoSum> STRATEGIES =
            List.of(new BruteForceTwoSum(), new TwoPointersTwoSum(), new HashMapTwoSum());

    private TwoSumCli() {
    }

    /**
     * Entry point.
     *
     * @param args {@code <objetivo> <valores...>} or {@code --aleatorio <n> <objetivo> [semilla]}
     */
    public static void main(String[] args) {
        int status = run(args, System.out, System.err);
        if (status != FOUND) {
            System.exit(status);
        }
    }

    static int run(String[] args, PrintStream out, PrintStream err) {
        try {
            Input input = parse(args);

            return report(input.values(), input.target(), out);
        } catch (IllegalArgumentException e) {
            err.println("error: " + e.getMessage());
            err.println("uso: java -jar two-sum.jar <objetivo> <valor> <valor> [...]");
            err.println("     java -jar two-sum.jar " + RANDOM_FLAG + " <n> <objetivo> [semilla]");

            return USAGE_ERROR;
        }
    }

    private record Input(int[] values, long target) {
    }

    private static Input parse(String[] args) {
        if (args.length > 0 && RANDOM_FLAG.equals(args[0])) {
            if (args.length < 3 || args.length > 4) {
                throw new IllegalArgumentException(RANDOM_FLAG + " requiere <n> <objetivo> [semilla]");
            }
            int size = parseInt(args[1], "n");
            if (size < 2 || size > MAX_RANDOM_SIZE) {
                throw new IllegalArgumentException("n debe estar entre 2 y " + MAX_RANDOM_SIZE);
            }
            RandomGenerator random = args.length == 4 ? new Random(parseLong(args[3], "semilla")) : new Random();

            return new Input(random.ints(size, 1, RANDOM_BOUND + 1).toArray(), parseLong(args[2], "objetivo"));
        }
        if (args.length < 3) {
            throw new IllegalArgumentException("se necesitan un objetivo y al menos dos valores");
        }
        int[] values = new int[args.length - 1];
        for (int index = 1; index < args.length; index++) {
            values[index - 1] = parseInt(args[index], "valor");
        }

        return new Input(values, parseLong(args[0], "objetivo"));
    }

    private static int report(int[] values, long target, PrintStream out) {
        out.println("Arreglo  : " + (values.length <= 20 ? Arrays.toString(values) : values.length + " valores"));
        out.println("Objetivo : " + target);
        boolean found = false;
        for (TwoSum strategy : STRATEGIES) {
            Optional<IndexPair> pair = strategy.find(values, target);
            found = pair.isPresent();
            String answer = pair
                    .map(p -> "índices " + p.first() + " y " + p.second()
                            + " (" + values[p.first()] + " + " + values[p.second()] + ")")
                    .orElse("sin solución");
            out.printf("%-28s %-36s %s%n", strategy.name(), "[" + strategy.complexity() + "]", answer);
        }

        return found ? FOUND : NOT_FOUND;
    }

    private static int parseInt(String text, String field) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(field + " no es un entero de 32 bits: " + text, e);
        }
    }

    private static long parseLong(String text, String field) {
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(field + " no es un entero: " + text, e);
        }
    }
}
