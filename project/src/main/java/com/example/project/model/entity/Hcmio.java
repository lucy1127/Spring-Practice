package com.example.project.model.entity;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hcmio")
@IdClass(Hcmio.HcmioId.class) //複合主鍵
@JsonPropertyOrder({ "tradeDate", "branchNo", "custSeq", "docSeq", "stock","bsType","price","qty","amt","fee","tax","stinTax","netAmt","modDate","modTime","modUser"})
public class Hcmio implements Serializable {
    private static final long serialVersionUID = 1L; //???

    @Id
    @Column(name="TradeDate")
    private String tradeDate; //交易日

    @Id
    @Column(name="BranchNo")
    private String branchNo = "F62Z"; //分公司代號

    @Id
    @Column(name="CustSeq")
    private String custSeq = "04"; //客戶帳號

    @Id
    @Column(name="DocSeq")
    private String docSeq; //委託書號

   //@ManyToOne(cascade = CascadeType.ALL)//有所有關聯操作的權限
    @Column(name="Stock")
    private String stock;//股票代號

    @Column(name="BsType")
    private String bsType;//買賣型態 B買S賣

    @Column(name="Price")
    private Double price; //單價

    @Column(name="Qty")
    private Integer qty;//股數

    @Column(name="Amt")
    private double amt;//價金

    @Column(name="Fee")
    private double fee;//手續費

    @Column(name="Tax")
    private double tax;//交易稅

    @Column(name="StinTax")
    private Double stinTax;//證所稅

    @Column(name="NetAmt")
    private double netAmt;//淨收付金額

    @Column(name="ModDate")
    private String modDate;//異動日期

    @Column(name="ModTime")
    private String modTime;//異動時間

    @Column(name="ModUser")
    private String modUser = "Lucy";//異動人員

    public static class HcmioId implements Serializable {
        private static final long serialVersionUID = 1L;

        private String tradeDate;
        private String branchNo;
        private String custSeq;
        private String docSeq;

    }

}
