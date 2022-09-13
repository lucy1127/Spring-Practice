package org.example;

public class Test {

    private static int docSeqToInt(String docSeq) { //ex. DB989
        String letter = docSeq.substring(0, 2); // 0 --> 指定位置 2 --> 指定長度  DB
        String number = docSeq.substring(2); // 2 --> 指定位置        989

        //charAt() 方法用于返回指定索引处的字符。索引范围为从 0 到 length() - 1。

        return (letter.charAt(0) - 'A') * 26 * 1000 + (letter.charAt(1) - 'A') * 1000 + //轉為數字
                Integer.parseInt(number);

    }

    private static String intToDocSeq(int seq) {
        String number = String.format("%03d", seq % 1000);
        char firstLetter = (char) ((seq / 1000 / 26) + 'A');
        System.out.println((seq / 1000 / 26));
        char secondLetter = (char) ((seq / 1000 % 26) + 'A');
        System.out.println((seq / 1000 % 26));
        String letter = "" + firstLetter + secondLetter;

        return letter + number;
    }

    public static void main(String[] args) {
        String docSeq = "AB999";

        int seq = docSeqToInt(docSeq);
        String newDocSeq = intToDocSeq(seq + 1);
        System.out.println(newDocSeq);
    }
}
