package com.example.project.model.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;



@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Symbol {

    @XmlAttribute
    String id;
    @XmlAttribute
    double dealprice;
    @XmlAttribute
    String shortname;
    @XmlAttribute(name = "mtype")
    String mType;
}
