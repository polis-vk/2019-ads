package ru.mail.polis.ads.part2.bogdanmendli;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class FirstTask {

    private static int[][] paths;
    private static Deque<Character> result;

    private static void solve(final Scanner in) {
        final int m = in.nextInt();
        final int n = in.nextInt();
        result = new ArrayDeque<>();
        paths = new int[m][n];
        int[][] grains = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                grains[i][j] = in.nextInt();
            }
        }
        paths[m - 1][0] = grains[m - 1][0];
        for (int i = 1; i < n; i++) {
            paths[m - 1][i] = grains[m - 1][i] + paths[m - 1][i - 1];
        }
        for (int i = m - 2; i >= 0; i--) {
            paths[i][0] = grains[i][0] + paths[i + 1][0];
        }

        for (int i = m - 2; i >=0 ; i--) {
            for (int j = 1; j < n; j++) {
                if (paths[i][j - 1] > paths[i + 1][j]) {
                    paths[i][j] = grains[i][j] + paths[i][j - 1];
                    continue;
                }
                paths[i][j] = grains[i][j] + paths[i + 1][j];
            }
        }
        restorePath(m, n);
        print();
    }

    private static void restorePath(final int m, final int n) {
        int right = n - 1;
        int front = 0;
        while (right != 0 && front != m - 1) {
            if (paths[front][right - 1] > paths[front + 1][right]) {
                result.push('R');
                right--;
                continue;
            }
            result.push('F');
            front++;
        }
        if (front == m - 1) {
            while (right != 0) {
                right--;
                result.push('R');
            }
        } else {
            while (front != m - 1) {
                front++;
                result.push('F');
            }
        }
    }

    public static void print() {
        while (!result.isEmpty()) {
            System.out.print(result.pop());
        }
    }

    public static void main(String[] args) {
        solve(new Scanner(System.in));
    }
}
