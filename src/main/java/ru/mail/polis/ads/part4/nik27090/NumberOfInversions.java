package ru.mail.polis.ads.part4.nik27090;

import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;


//задача: кол-во инверсий
//решение: https://www.e-olymp.com/ru/submissions/5962640

public class NumberOfInversions {
    static int inv =0;
    private NumberOfInversions() {
        // Should not be instantiated
    }

    private static void solve(final FastScanner in, final PrintWriter out) {
        int n=in.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i <n ; i++) {
            arr[i]=in.nextInt();
        }
        sortMerge(arr);
        out.println(inv);
    }

    private static int[] sortMerge(int[] arr){
        int len = arr.length;
        if (len<2){
            return arr;
        }
        int mid = len/2;
        return merge (sortMerge(Arrays.copyOfRange(arr,0,mid)), sortMerge(Arrays.copyOfRange(arr,mid,len)));
    }

    private static int[] merge(int[] arr1, int[] arr2){
        int len1=arr1.length, len2=arr2.length;
        int len=len1+len2;
        int a=0,b=0;
        int[] result =new int[len];
        for (int i = 0; i < len; i++) {
            if(b<len2 && a<len1){
                if( arr1[a] > arr2[b]) {
                    inv+=len1-a;
                    result[i] = arr2[b++];
                } else {

                    result[i] =arr1[a++];}
            } else if (b<len2){
                result[i]=arr2[b++];
            } else {
                result[i]=arr1[a++];
            }
        }
        return result;
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