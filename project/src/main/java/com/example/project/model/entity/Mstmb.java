package com.example.project.model.entity;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mstmb")
@JsonPropertyOrder({ "stock","stockName","marketType","curPrice","refPrice","currency","modDate","modTime","modUser"})
public class Mstmb {

    @Id
    @Column(name="Stock")
    private String stock;

    @Column(name="StockName")
    private String stockName;

    @Column(name="MarketType")
    private String marketType;

    @Column(name="CurPrice")
    private Double nowPrice;

    @Column(name="RefPrice")
    private Double refPrice;

    @Column(name="Currency")
    private String currency = "台幣";

    @Column(name="ModDate")
    private String modDate;

    @Column(name="ModTime")
    private String modTime;

    @Column(name="ModUser")
    private String modUser = "Lucy";
}
