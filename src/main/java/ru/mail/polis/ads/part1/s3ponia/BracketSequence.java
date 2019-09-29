package ru.mail.polis.ads.part1.s3ponia;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * https://www.e-olymp.com/ru/submissions/5737606
 */
public final class BracketSequence {
    private BracketSequence() {
    }

    private static char getMatchCharByOpen(final char ch) {
        if (ch == '(') {
            return ')';
        }
        if (ch == '[') {
            return ']';
        }
        return '\0';
    }

    private static String getFixedBracket(final char bracket) {
        if (bracket == ')' || bracket == '(') {
            return "()";
        } else {
            return "[]";
        }
    }


    private static String print(final int[][] arrayRestore, final String input, final int e, final int b) {
        if (b > e) {
            return "";
        }
        if (b == e) {
            return getFixedBracket(input.charAt(b));
        } else if (arrayRestore[b][e] == -1) {
            return input.charAt(b) + print(arrayRestore, input, e - 1, b + 1) + input.charAt(e);
        } else {
            return print(arrayRestore, input, arrayRestore[b][e], b)
                    +
                    print(arrayRestore, input, e, arrayRestore[b][e] + 1);
        }
    }

    private static class Pair {
        private final int first;
        private final int second;

        private Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        public static Pair valueOf(final int first, final int second) {
            return new Pair(first, second);
        }
    }

    private static Pair getSmallestCorrectionForPart(final int[][] arraySmallestCorrection, final int i, final int j) {
        int restore = -1;
        int minCorrection = arraySmallestCorrection[i][j];

        for (int k = i; k < j; k++) {
            final int temp = arraySmallestCorrection[i][k] + arraySmallestCorrection[k + 1][j];
            if (minCorrection == -1 || temp < minCorrection) {
                minCorrection = temp;
                restore = k;
            }
        }

        return Pair.valueOf(minCorrection, restore);
    }

    private static String getFixedBracketSequence(final String input) {
        // arraySmallestCorrection[i][j] -
        // smallest size for part of the input from i to j
        int[][] arraySmallestCorrection = new int[input.length()][input.length()];
        // arrayRestore[i][j] = -1 -
        // input[i] == input[j] and part between i and j is fixed
        // arrayRestore[i][j] >= 0 -
        // part between i and arrayRestore[i][j] and part between arrayRestore[i][j]+1 and j are fixed
        int[][] arrayRestore = new int[input.length()][input.length()];

        for (int i = arraySmallestCorrection.length - 1; i >= 0; i--) {
            for (int j = i; j < arraySmallestCorrection[0].length; j++) {
                arrayRestore[i][j] = -1;
                arraySmallestCorrection[i][j] = -1;

                if (i == j) {
                    arraySmallestCorrection[i][j] = 1;
                    continue;
                }

                if (getMatchCharByOpen(input.charAt(i)) == input.charAt(j)) {
                    arraySmallestCorrection[i][j] = arraySmallestCorrection[i + 1][j - 1];
                }

                Pair pairSolution = getSmallestCorrectionForPart(arraySmallestCorrection, i, j);
                arraySmallestCorrection[i][j] = pairSolution.first;
                arrayRestore[i][j] = pairSolution.second;
            }
        }

        return print(arrayRestore, input, input.length() - 1, 0);
    }

    public static void main(final String[] arg) {
        final Scanner in = new Scanner(System.in);
        final PrintWriter out = new PrintWriter(System.out);

        out.println(getFixedBracketSequence(in.nextLine()));
        out.flush();
    }
}