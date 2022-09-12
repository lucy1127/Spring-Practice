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
@Table(name = "tcnud")
@IdClass(Tcnud.TcnudId.class) //複合主鍵
@JsonPropertyOrder({ "tradeDate", "branchNo", "custSeq", "docSeq", "stock","price","qty","remainQty","fee","cost","modDate","modTime","modUser"})
public class Tcnud implements Serializable {

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

    @Column(name="Stock")
    private String stock;//股票代號


    @Column(name="Price")
    private double buyPrice; //單價

    @Column(name="Qty")
    private int qty;//股數

    @Column(name="RemainQty")
    private int remainQty;//股數

    @Column(name="Fee")
    private double fee;//手續費

    @Column(name="Cost")
    private double cost;//成本

    @Column(name="ModDate")
    private String modDate;//異動日期

    @Column(name="ModTime")
    private String modTime;//異動時間

    @Column(name="ModUser")
    private String modUser = "Lucy";//異動人員

    public static class TcnudId implements Serializable {
        private static final long serialVersionUID = 1L;

        private String tradeDate;
        private String branchNo;
        private String custSeq;
        private String docSeq;

    }
}
