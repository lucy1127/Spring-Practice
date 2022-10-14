package com.example.Spring1.model.entity;


import lombok.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cashi")
@IdClass(Cashi.CashiId.class)
@XmlRootElement(name = "CashiResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class Cashi implements Serializable {

    @Id
    @Column(name = "mgni_id") //申請主檔
    private String id;

    @Id
    @Column(name = "acc_no")//存入結算帳戶帳號
    private String account;

    @Id
    @Column(name = "ccy")//幣別
    private String currency;

    @Column(name = "amt")//金額
    private BigDecimal amt;

    public static class CashiId implements Serializable{
        private String id;
        private String account;
        private String currency;
    }

}
