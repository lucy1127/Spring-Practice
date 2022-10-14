package com.example.Spring1.service;

import com.example.Spring1.controller.dto.request.*;
import com.example.Spring1.controller.error.MgniNotFoundException;
import com.example.Spring1.model.CashiRepository;
import com.example.Spring1.model.MgniRepository;
import com.example.Spring1.model.entity.Cashi;
import com.example.Spring1.model.entity.Mgni;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MgniService {

    @Autowired
    MgniRepository mgniRepository;

    @Autowired
    CashiRepository cashiRepository;

    //    private final SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    private final Date nowdate = new Date();
    private BigDecimal amt = BigDecimal.ZERO;

    public List<Mgni> getData() {
        return mgniRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public Mgni createSettlementMargin(CreateMgniRequest request) {
        if (request.getKacType().equals("1")) {
            amt = totalPrice(request);
        } else {
            amt = request.getAmt();
        }
        Mgni mgni = Mgni.builder()
                .id("MGI" + localDate() + localTime())
                .type(request.getType())
                .memberCode(request.getMemberCode())
                .kacType(request.getKacType())
                .bankCode(request.getBankCode())
                .currency(request.getCurrency())
                .pvType(request.getPvType())
                .bicaccNo(request.getAccount())
                .iType("1")
                .reason(request.getReason())
                .amt(amt.setScale(2))
                .contactName(request.getContactName())
                .contactPhone(request.getContactPhone())
                .status("0")
                .build();

        mgniRepository.save(mgni);

//        if (request.getAccountList() != null && request.getKacType().equals("1")) {
//            List<String> disAccList = request.getAccountList().stream().map(e ->e.getAccNo()).distinct().collect(Collectors.toList());
//           for(String disAcc:disAccList){
//               BigDecimal price = BigDecimal.ZERO;
//               for (Account acc : request.getAccountList()) {
//                   if(disAcc.equals(acc.getAccNo())){
//                       price = price.add(acc.getPrice());
//                   }
//               }
//               createCashi(mgni.getId(), disAcc, request.getCurrency(), price);
//           }
//
//        }
        disCashi(mgni.getId(),request);
        return mgniRepository.findMgni(mgni.getId());
    }

    private void disCashi(String id, CreateMgniRequest request) {
        if (request.getAccountList() != null && request.getKacType().equals("1")) {
            List<String> disAccList = request.getAccountList().stream().map(e -> e.getAccNo()).distinct().collect(Collectors.toList());
            for (String disAcc : disAccList) {
                BigDecimal price = BigDecimal.ZERO;
                for (Account acc : request.getAccountList()) {
                    if (disAcc.equals(acc.getAccNo())) {
                        price = price.add(acc.getPrice());
                    }
                }
                createCashi(id, disAcc, request.getCurrency(), price);
            }
        }
    }

    private void createCashi(String id, String accNo, String currency, BigDecimal price) {

        Cashi cashi = Cashi.builder()
                .id(id)
                .account(accNo)
                .currency(currency)
                .amt(price.setScale(2))
                .build();

        cashiRepository.save(cashi);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mgni updateData(CreateMgniRequest request) {

        cashiRepository.deleteCashi(request.getId());
        Mgni mgni = mgniRepository.findMgni(request.getId());

        if (null == mgni) {
            throw new MgniNotFoundException("This id doesn't exist....");
        }


        if (request.getKacType().equals("1")) {
            amt = totalPrice(request);
        } else {
            amt = request.getAmt();
        }


        mgni.setType(request.getType());
        mgni.setMemberCode(request.getMemberCode());
        mgni.setKacType(request.getKacType());
        mgni.setBankCode(request.getBankCode());
        mgni.setCurrency(request.getCurrency());
        mgni.setPvType(request.getPvType());
        mgni.setBicaccNo(request.getAccount());
        mgni.setIType("1");
        mgni.setReason(request.getReason());
        mgni.setAmt(amt);
        mgni.setContactName(request.getContactName());
        mgni.setContactPhone(request.getContactPhone());
        mgni.setStatus("0");

        disCashi(mgni.getId(),request);
        mgniRepository.save(mgni);
        mgni.setCashiList(cashiRepository.findCashi(request.getId()));

        return mgniRepository.findMgni(mgni.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteData(DeleteRequest request) {
        if (null == mgniRepository.findMgni(request.getId())) {
            throw new MgniNotFoundException("This id doesn't exist....");
        }
        mgniRepository.delete(mgniRepository.findMgni(request.getId()));
//        cashiRepository.deleteCashi(request.getId());
    }

    public List<Cashi> getCashi(CashiRequest request,int page,int size) {
        Specification<Cashi> spec = new Specification<Cashi>() {
            @Override //            代表Criteria查詢的根物件 ,代表一個specific的頂層查詢物件,用來構建CritiaQuery的構建器物件Predicate
            public Predicate toPredicate(Root<Cashi> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<>();

                if (request.getId() != null) {
                    predicates.add(builder.equal(root.get("id"), request.getId()));
                }
                if (request.getAccount() != null) {
                    predicates.add(builder.equal(root.get("account"), request.getAccount()));
                }
                if (request.getCurrency() != null) {
                    predicates.add(builder.equal(root.get("currency"), request.getCurrency()));
                }
                query.orderBy(builder.asc(root.get("account"))); //排序
                return builder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Pageable pageable = PageRequest.of(page, size);
        Page<Cashi> cashiList = cashiRepository.findAll(spec, pageable);
        return cashiList.getContent();
    }

    public List<Mgni> getMgni(MgniRequest request,int page,int size) {
        Specification<Mgni> spec = new Specification<Mgni>() {
            @Override
            public Predicate toPredicate(Root<Mgni> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<>();

                if (request.getId() != null) {
                    predicates.add(builder.equal(root.get("id"), request.getId()));
                }
                if (request.getMemberCode() != null) {
                    predicates.add(builder.equal(root.get("memberCode"), request.getMemberCode()));
                }
                if (request.getBankCode() != null) {
                    predicates.add(builder.equal(root.get("bankCode"), request.getBankCode()));
                }
                if (request.getBicaccNo() != null) {
                    predicates.add(builder.equal(root.get("bicaccNo"), request.getBicaccNo()));
                }
                if (request.getContactName() != null) {
                    predicates.add(builder.equal(root.get("contactName"), request.getContactName()));
                }
                if (request.getDate() != null) {
                    LocalDateTime dateTime = LocalDate.parse(request.getDate(), DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay();
                    predicates.add(builder.between(root.get("time"), dateTime, LocalDateTime.now()));
                }
                query.orderBy(builder.asc(root.get("id")));
                return builder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Pageable pageable = PageRequest.of(page, size);
        Page<Mgni> mgniList = mgniRepository.findAll(spec, pageable);
        return mgniList.getContent();
    }

    public BigDecimal totalPrice(CreateMgniRequest request) {

        BigDecimal totalprice = BigDecimal.ZERO;
        for (Account acc : request.getAccountList()) {
            totalprice = totalprice.add(acc.getPrice());
        }
        return totalprice;
    }

    public static String localDate() {
        LocalDate localDate = Instant.now().atZone(ZoneOffset.ofHours(+8)).toLocalDate();
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return formattedDate;
    }

    public static String localTime() {
        LocalTime localTime = Instant.now().atZone(ZoneOffset.ofHours(+8)).toLocalTime();
        String formattedTime = localTime.format(DateTimeFormatter.ofPattern("HHmmssSSS"));
        return formattedTime;
    }


}
