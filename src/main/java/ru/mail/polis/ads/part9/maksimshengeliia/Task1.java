package ru.mail.polis.ads.part9.maksimshengeliia;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/*
*   https://www.e-olymp.com/ru/submissions/6368014
* */
public class Task1 {

    enum Colors {
        WHITE, GREY, BLACK;
    }

    static List<List<Integer>> graph;
    static List<Integer> result = new ArrayList<>();
    static Colors[] colorGraph;
    static int vertexes, edges;


    static void dfs(int v) {
        colorGraph[v] = Colors.GREY;

        List<Integer> closeVertexes = graph.get(v);
        for (int cVertex : closeVertexes) {
            if (colorGraph[cVertex] == Colors.WHITE) {
                dfs(cVertex);
            } else if (colorGraph[cVertex] == Colors.GREY) {
                System.out.println("-1");
                Runtime.getRuntime().exit(0);
            }
        }
        colorGraph[v] = Colors.BLACK;
        result.add(v);
    }

    private static void solve(final FastScanner in, final PrintWriter out) {
        vertexes = in.nextInt();
        edges = in.nextInt();
        graph = new ArrayList<>();
        colorGraph = new Colors[vertexes + 1];

        for (int i = 0; i <= vertexes; i++) {
            graph.add(new ArrayList<>());
            colorGraph[i] = Colors.WHITE;
        }

        for (int i = 0; i < edges; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            graph.get(a).add(b);
        }

        for(int i = 1; i <= vertexes; i++) {
            if (colorGraph[i] == Colors.WHITE) {
                dfs(i);
            }
        }

        for(int i = result.size() - 1; i >= 0; i--) {
            out.print(result.get(i) + " ");
        }
        out.println();
    }

    private static class FastScanner {
        private final BufferedReader reader;
        private StringTokenizer tokenizer;

        FastScanner(final InputStream in) {
            reader = new BufferedReader(new InputStreamReader(in));
        }

        String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tokenizer.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }

    public static void main(final String[] arg) {
        final FastScanner in = new FastScanner(System.in);
        try (PrintWriter out = new PrintWriter(System.out)) {
            solve(in, out);
        }
    }
}
