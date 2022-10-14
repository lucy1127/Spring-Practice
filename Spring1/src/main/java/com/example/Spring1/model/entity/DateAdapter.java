package com.example.Spring1.model.entity;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateAdapter extends XmlAdapter<String, LocalDateTime> {
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public LocalDateTime unmarshal(String s) throws Exception {
        return LocalDateTime.parse(s, dateFormat);    }

    @Override
    public String marshal(LocalDateTime localDateTime) throws Exception {
        return localDateTime.format(dateFormat);
    }
}

//    希望它以yyyy-MM-dd HH:mm:ss的方式進行轉換。
//    這個時候我們就可以使用XmlJavaTypeAdapter註解了，
//    它允許我們指定一個XmlAdapter類的子類，這樣對應的屬性在轉換為XML時會先通過指定的XmlAdapter進行轉換
//    ，轉換後的結果再進行XML轉換。除了可以作為Java對象轉換XML時的適配器外，
//    它也可以作為XML轉換Java的適配器。XmlAdapter中一共定義了兩個抽象方法，
//    marshal和unmarshal。marshal用於Java對象轉換XML，unmarshal用於XML轉換Java對象。
//    XmlAdapter上定義了兩個泛型，第一個是Java轉換XML時適配後返回的類型，第二個是原始的類型。
//    所以我們如果需要把我們的java.util.Date類型的屬性以yyyy-MM-dd HH:mm:ss格式進行輸出，我們需要定義如下這樣一個XmlAdapter類。