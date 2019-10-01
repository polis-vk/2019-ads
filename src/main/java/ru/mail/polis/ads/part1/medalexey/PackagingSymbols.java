package ru.mail.polis.ads.part1.medalexey;

import java.io.PrintWriter;
import java.util.Scanner;


/**
 * Название задачи: упаковка символов
 * Тестирование: https://www.e-olymp.com/ru/submissions/5738646
 */
public final class PackagingSymbols {

    private static String[][] matrix;
    private static String input;

    private PackagingSymbols() {

    }

    private static void solve(final Scanner in, final PrintWriter out) {
        input = in.nextLine();
        matrix = new String[input.length()][input.length()];

        // перебираем все возможные длины подстрок входной строки
        for (int len = 1; len <= input.length(); len++) {
            // проходим по всем таким подстрокам
            for (int left = 0; left + len - 1 < input.length(); left++) {
                //определяем конец текущей подстроки
                final int right = left + len -1;
                // определяем текущую подстроку
                String minSubstring = input.substring(left, right+1);

                // если длина подстроки меньше 4, то её нет смысла упаковывать
                if(len > 4) {
                    // поиск минимальной подстроки данной подстроки
                    minSubstring = getMinSubstring(minSubstring, left, right);

                    //упаковна строки
                    minSubstring = packString(minSubstring, len, left, right);
                }

                matrix[left][right] = minSubstring;
            }
        }

        out.println(matrix[0][input.length()-1]);
    }


    // упаковка строки, если она периодична
    private static String packString(final String minSubstring,
                                     final int len,
                                     final int left,
                                     final int right) {

        String result = minSubstring;
        for (int period = 1; period < len; period++) {
            if (len%period == 0 && isPeriodic(left, period, right)) {
                    final String tmp = len/period + "(" + matrix[left][left + period - 1] + ")";
                    result = tmp.length() < result.length() ? tmp : result;
            }
        }

        return result;
    }


    // поиск минимальной подстроки данной подстроки
    private static String getMinSubstring(final String curMinSubstring,
                                          final int left, final int right) {

        String result= curMinSubstring;
        for (int right1 = left; right1 < right; right1++) {
            final int left2 = right1 + 1;
            final String tmp = matrix[left][right1] + matrix[left2][right];

            if (tmp.length() < result.length()) {
                result = tmp;
            }
        }

        return result;
    }


    // проверка периодичности строки
    private static boolean isPeriodic(final int left, final int period, final int right) {

        boolean isPeriodic = true;
        for (int i = left + period; i <= right; i++) {
            if (input.charAt(i) != input.charAt(i - period)) {
                isPeriodic = false;
                break;
            }
        }

        return isPeriodic;
    }


    public static void main(final String[] arg) {
        final Scanner in = new Scanner(System.in);
        final PrintWriter out = new PrintWriter(System.out);
        solve(in, out);
        out.close();
        in.close();
    }

}
