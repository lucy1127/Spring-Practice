package com.example.project.service;


import com.example.project.controller.dto.request.AddBalanceRequest;
import com.example.project.model.HcmioRepository;
import com.example.project.model.entity.Hcmio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Service
public class HcmioService {

    @Autowired //將該元件初始化後自動帶入
    private HcmioRepository hcmioRepository;

    @Transactional
    public Hcmio createHcmio(AddBalanceRequest request){

        //hcmio
        Hcmio hcmio = new Hcmio();
        hcmio.setTradeDate(request.getTradeDate());
        hcmio.setBranchNo(request.getBranchNo());
        hcmio.setCustSeq(request.getCustSeq());
        hcmio.setDocSeq(createDocSeq());
        hcmio.setStock(request.getStock());
        hcmio.setBsType("B");
        hcmio.setBuyPrice(request.getBuyPrice());
        hcmio.setQty(request.getQty());


        int qty;
        double price,amt,fee,tax,netAmt = 0;
        price = request.getBuyPrice();
        qty = request.getQty();
        amt = Math.floor(price * qty);
        fee = Math.floor(amt * 0.001425);
        netAmt = -(amt+fee);

        hcmio.setAmt(amt);
        hcmio.setFee(fee);
        hcmio.setNetAmt(netAmt);

        LocalDate localDate = Instant.now().atZone(ZoneOffset.ofHours(+8)).toLocalDate();
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalTime localTime = Instant.now().atZone(ZoneOffset.ofHours(+8)).toLocalTime();
        String formattedTime = localTime.format(DateTimeFormatter.ofPattern("HHmmss"));

        hcmio.setModDate(formattedDate);
        hcmio.setModTime(formattedTime);
        hcmioRepository.save(hcmio);

        return hcmio;
    }
    private String createDocSeq(){
        Hcmio latestDocSeq = hcmioRepository.findFirstByOrderByDocSeqDesc();
        if(latestDocSeq == null) {
            return "AA000";
        }
        if(latestDocSeq.getDocSeq().equals("ZZ999")){
            return "Maximum storage space reached....";
        }

        int seq = docSeqToInt(String.valueOf(latestDocSeq.getDocSeq()));
        String newDocSeq = intToDocSeq(seq + 1);

        return newDocSeq;

    }
    private static int docSeqToInt(String docSeq) {
        String letter = docSeq.substring(0, 2);
        String number = docSeq.substring(2);

        return (letter.charAt(0) - 'A') * 26 * 1000 + (letter.charAt(1) - 'A') * 1000 +
                Integer.parseInt(number);

    }
    private static String intToDocSeq(int seq) {
        String number = String.format("%03d", seq % 1000);
        char firstLetter = (char) ((seq / 1000 / 26) + 'A');
        char secondLetter = (char) ((seq / 1000 % 26) + 'A');
        String letter = "" + firstLetter + secondLetter;

        return letter + number;
    }


}
