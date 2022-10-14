package com.example.Spring1.controller.dto.response;


import com.example.Spring1.controller.dto.request.Account;
import com.example.Spring1.model.entity.Mgni;
import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MgniResponse")
public class MgniResponse {
    private List<Mgni> mgniList;
    private String message;
}
