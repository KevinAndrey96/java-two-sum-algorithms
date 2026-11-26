package com.kevinandrey.twosum.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TwoSumCliTest {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();

    private int run(String... args) {
        return TwoSumCli.run(
                args,
                new PrintStream(out, true, StandardCharsets.UTF_8),
                new PrintStream(err, true, StandardCharsets.UTF_8));
    }

    private String output() {
        return out.toString(StandardCharsets.UTF_8);
    }

    @Test
    void printsEveryStrategyAnswer() {
        assertEquals(TwoSumCli.FOUND, run("9", "2", "7", "11", "15"));
        assertTrue(output().contains("Arreglo  : [2, 7, 11, 15]"));
        assertTrue(output().contains("Objetivo : 9"));
        assertEquals(3, output().split("índices 0 y 1 \\(2 \\+ 7\\)", -1).length - 1);
    }

    @Test
    void reportsWhenThereIsNoSolution() {
        assertEquals(TwoSumCli.NOT_FOUND, run("100", "1", "2", "3"));
        assertEquals(3, output().split("sin solución", -1).length - 1);
    }

    @Test
    void seededRandomArraysAreReproducible() {
        run("--aleatorio", "10", "50", "42");
        String first = output();
        out.reset();
        run("--aleatorio", "10", "50", "42");

        assertEquals(first, output());
        assertTrue(first.startsWith("Arreglo  : ["));
    }

    @Test
    void unseededRandomArraysWork() {
        int status = run("--aleatorio", "50", "100");

        assertTrue(status == TwoSumCli.FOUND || status == TwoSumCli.NOT_FOUND);
    }

    @Test
    void largeArraysAreSummarized() {
        run("--aleatorio", "1000", "-1", "7");

        assertTrue(output().contains("Arreglo  : 1000 valores"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "9 2", "nueve 2 7", "9 2 siete", "9 2 9999999999",
        "--aleatorio 10", "--aleatorio 1 5", "--aleatorio 2000000 5", "--aleatorio 10 5 x", "--aleatorio 10 5 1 2"})
    void rejectsInvalidArguments(String line) {
        String[] args = line.isEmpty() ? new String[0] : line.split(" ");

        assertEquals(TwoSumCli.USAGE_ERROR, run(args));
        assertTrue(err.toString(StandardCharsets.UTF_8).startsWith("error: "));
        assertEquals("", output());
    }
}
