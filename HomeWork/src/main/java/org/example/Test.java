package org.example;

public class Test {
    public static void main(String []args){
        String letter = "AA";
        String number = "000";
        String combine = letter + number;
        int num = 0;

        for(int i =0;i<10;i++){
            num  = Integer.parseInt(number) ;
            num++;
        }
        
        number = String.valueOf(num);
        System.out.println(combine);

    }
}
