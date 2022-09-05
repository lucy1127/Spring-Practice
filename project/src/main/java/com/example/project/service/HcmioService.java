package com.example.project.service;


import com.example.project.controller.dto.request.CreateHcmioRequest;
import com.example.project.model.HcmioRepository;
import com.example.project.model.TcnudRepository;
import com.example.project.model.entity.Hcmio;
import com.example.project.model.entity.Tcnud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class HcmioService {

    @Autowired //將該元件初始化後自動帶入
    private HcmioRepository hcmioRepository;
    @Autowired
    private TcnudRepository tcnudRepository;
    @Autowired
    private TcnudService tcnudService;

    public List<Hcmio> getHcmioList(){
        return hcmioRepository.findAll();
    }

    public List<Hcmio> getDataByDocSeq(String date,String branch,String custSeq,String doc){
        return hcmioRepository.find(date,branch,custSeq,doc);
    }
    public String createHcmio(CreateHcmioRequest request){

        if(request.getTradeDate().isEmpty() || request.getDocSeq().isEmpty() || request.getStock().isEmpty()
                ||request.getBsType().isEmpty() || request.getPrice().isNaN()||request.getQty() < 0){
            return "Something wrong...or lost... Check it again...";
        }
        //hcmio
        Hcmio hcmio = new Hcmio();
        hcmio.setTradeDate(request.getTradeDate());
        hcmio.setDocSeq(request.getDocSeq());
        hcmio.setStock(request.getStock());
        hcmio.setBsType(request.getBsType());
        hcmio.setPrice(request.getPrice());
        hcmio.setQty(request.getQty());

        double amt,fee,tax,netAmt;
        amt = Math.floor(request.getPrice() * request.getQty());
        fee = Math.floor(amt * 0.001425);

        if(request.getBsType().equals("B")){
            netAmt = -(amt+fee);
        }else{
            tax = Math.floor(amt * 0.003);
            hcmio.setTax(tax);

            netAmt = amt-fee-tax;
        }

        //hcmio
        hcmio.setAmt(amt);
        hcmio.setFee(fee);
        hcmio.setNetAmt(netAmt);

        LocalDate localDate = Instant.now().atZone(ZoneOffset.ofHours(+8)).toLocalDate();
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalTime localTime = Instant.now().atZone(ZoneOffset.ofHours(+8)).toLocalTime();
        String formattedTime = localTime.format(DateTimeFormatter.ofPattern("HHmmss"));

        hcmio.setModDate(formattedDate);
        hcmio.setModTime(formattedTime);


        if(null != tcnudRepository.findByStock(request.getStock())){ //找到相同的股票
            if(request.getBsType().equals("S") || request.getBsType().equals("s")){
                Tcnud check = tcnudRepository.findByStock(request.getStock());
                if(check.getRemainQty() < request.getQty()){
                    return "RemainQty is not enough to sold...";
                }
            }

            this.tcnudService.upDateTchud(request.getBsType(),request.getStock(),request.getPrice(),request.getQty());

        }else{
            if(request.getBsType().equals("S") || request.getBsType().equals("s")){
                return "You don't have this stock...";
            }
            this.tcnudService.createTchud(hcmio);
        }

        hcmioRepository.save(hcmio);
        return "Create Success";
    }

    @Transactional
    public String deleteHcmio(String docSeq){
        hcmioRepository.deleteByDocSeq(docSeq);
        return "Delete Success";
    }

}
