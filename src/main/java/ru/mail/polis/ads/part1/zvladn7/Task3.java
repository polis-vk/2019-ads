package ru.mail.polis.ads.part1.zvladn7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;


public class Task3 {
  static char[] chars;
  static String rowString;
  static String theShortestString;
  static String[][] boxed;

  private Task3() {
  }

  public static String minLenString(final String str1, final String str2) {
    return str1.length() < str2.length() ? str1 : str2;
  }

  public static boolean isRepeated(final int left, final int right, final int lenPeriod) {
    boolean isRepeatedly = true;
    for (int i = left + lenPeriod; i <= right; ++i) {
      if (chars[i - lenPeriod] != chars[i]) {
        isRepeatedly = false;
        break;
      }
    }
    return isRepeatedly;
  }

  public static void partlyBoxing(final int left, final int right) {
    for (int rightBorder = left; rightBorder < right; ++rightBorder) {
      final int leftBorder = rightBorder + 1;
      final String tempString = boxed[left][rightBorder] + boxed[leftBorder][right];
      theShortestString = minLenString(tempString, theShortestString);
    }
  }

  public static void checkPeriodic(final int left, final int right, final int length) {
    for (int lenPeriod = 1; lenPeriod < length; ++lenPeriod) {
      if (length % lenPeriod == 0) {
        if (isRepeated(left, right, lenPeriod)) {
          final String tempString = length / lenPeriod + "(" + boxed[left][left + lenPeriod - 1] + ")";
          theShortestString = minLenString(tempString, theShortestString);
        }
      }
    }
  }

  public static void findTheShortest(final int left, final int right, final int length) {
    partlyBoxing(left, right);
    checkPeriodic(left, right, length);
  }


  public static void boxing() {
    for (int length = 1; length <= chars.length; ++length) {
      for (int left = 0; length + left - 1 < chars.length; ++left) {
        final int right = length + left - 1;
        theShortestString = rowString.substring(left, left + length);
        if (length > 4) {
          findTheShortest(left, right, length);
        }
        boxed[left][right] = theShortestString;
      }
    }
  }



  public static void solve(final BufferedReader in, final PrintWriter out) {
    try {
      rowString = in.readLine();
      chars = rowString.toCharArray();
      boxed = new String[chars.length][chars.length];
      boxing();
      out.println(boxed[0][chars.length - 1]);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    try (PrintWriter out = new PrintWriter(System.out)) {
      solve(in, out);
    }
  }
}
