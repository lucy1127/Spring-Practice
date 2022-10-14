package com.example.Spring1.model.entity;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;



@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mgni")
@XmlRootElement(name = "MgniResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@EntityListeners(AuditingEntityListener.class)
public class Mgni implements Serializable {

    @Id
    @Column(name = "id") //申請ID
    private String id;  //MGI+yyyymmdd+hhmmssfff

    @CreatedDate
    @Column(name = "time") //存入日期
    @XmlJavaTypeAdapter(DateAdapter.class)
    private LocalDateTime time;

    @Column(name = "type") //存入類型
    private String type; //1:入金

    @Column(name = "cm_no") //結算會員代號
    private String memberCode;

    @Column(name = "kac_type")//存入保管專戶別
    private String kacType; //1:結算保證金 2:交割結算基金專戶

    @Column(name = "bank_no")//存入結算銀行代碼
    private String bankCode;

    @Column(name = "ccy")//存入幣別
    private String currency;

    @Column(name = "pv_type")//存入方式
    private String pvType;//1:虛擬帳戶 2:實體帳戶

    @Column(name = "bicacc_no") //實體帳號/虛擬帳號
    private String bicaccNo;// 存放實體帳號或是虛擬帳號

    @Column(name = "i_type") //存入類別
    private String iType; //1:開業 2:續繳 3:其他 4:額外分擔金額

    @Column(name = "p_reason") //存入實體帳號原因
    private String reason;

    @Column(name = "amt")//總存入金額
    private BigDecimal amt;

    @Column(name = "ct_name")//聯絡人姓名
    private String contactName;

    @Column(name = "ct_tel")//聯絡人電話
    private String contactPhone;

    @Column(name = "status") //申請狀態
    private String status;

    @LastModifiedDate //更新時間
    @XmlJavaTypeAdapter(DateAdapter.class)
    @Column(name = "u_time")
    private LocalDateTime updateTime;

    @OneToMany(fetch =  FetchType.EAGER,mappedBy = "id",cascade = CascadeType.ALL) //FetchType.EAGER表示一併載入所有屬性所對應的資料
    //@JoinColumn(name = "mgni_id")//	CascadeType.ALL 無論儲存、合併、 更新或移除，一併對被參考物件作出對應動作。
    private List<Cashi> cashiList;
}
