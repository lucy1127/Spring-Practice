package com.example.project.service;


import com.example.project.controller.dto.request.AddBalanceRequest;
import com.example.project.controller.dto.request.SettlementRequest;
import com.example.project.controller.dto.request.UnrealizedRequest;
import com.example.project.controller.dto.resopnse.UnrealizedDetail;
import com.example.project.controller.dto.resopnse.UnrealizedProfit;
import com.example.project.controller.error.MstmbNotFoundException;
import com.example.project.controller.error.TcnudNotFoundException;
import com.example.project.model.HolidayRepository;
import com.example.project.model.MstmbRepository;
import com.example.project.model.TcnudRepository;
import com.example.project.model.entity.Hcmio;
import com.example.project.model.entity.Mstmb;
import com.example.project.model.entity.Tcnud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EnableCaching
@Service
public class TransactionService {

    @Autowired
    private HcmioService hcmioService;
    @Autowired
    private TcnudService tcnudService;
    @Autowired
    private TcnudRepository tcnudRepository;
    @Autowired
    private MstmbRepository mstmbRepository;
    @Autowired
    private HolidayRepository holidayRepository;
    @Transactional(rollbackFor = Exception.class)
    public List<UnrealizedDetail> addBalance(AddBalanceRequest request) {

        Mstmb dataInMstmb = mstmbRepository.findByStock(request.getStock());
        if (dataInMstmb == null) {
            throw new MstmbNotFoundException();
        }

        Hcmio hcmio = hcmioService.createHcmio(request);
        Tcnud tcnud = tcnudService.createTchud(hcmio);

        List<UnrealizedDetail> results = new ArrayList<>();
        results.add(new UnrealizedDetail(
                tcnud.getTradeDate(),
                tcnud.getBranchNo(),
                tcnud.getCustSeq(),
                tcnud.getDocSeq(),
                tcnud.getStock(),
                dataInMstmb.getStockName(),
                tcnud.getBuyPrice(),
                dataInMstmb.getNowPrice(),
                tcnud.getQty(),
                tcnud.getRemainQty(),
                tcnud.getFee(),
                tcnud.getCost(),
                getMarketValue(dataInMstmb.getNowPrice(), tcnud.getQty()),
                getUnrealProfit(dataInMstmb.getNowPrice(), tcnud.getQty(), tcnud.getCost()),
                String.format("%.2f", getProfitability(getUnrealProfit(dataInMstmb.getNowPrice(), tcnud.getQty(), tcnud.getCost()), tcnud.getCost())) + "%"
        ));

        return results;
    }

    public List<UnrealizedDetail> getDetailList(String branchNo, String custSeq, Double minProfit, Double maxProfit) {
        List<Tcnud> dataList = tcnudRepository.findByBranchNoAndCustSeq(branchNo, custSeq);
        if (dataList.isEmpty()) {
            throw new TcnudNotFoundException();
        }

        return tcnudListToDetail(dataList, minProfit, maxProfit);
    }

    public List<UnrealizedDetail> getDetailList(String branchNo, String custSeq, String stock, Double minProfit, Double maxProfit) {
        List<Tcnud> dataList = tcnudRepository.findByBranchNoAndCustSeqAndStock(branchNo, custSeq, stock);
        if (dataList.isEmpty()) {
            throw new TcnudNotFoundException();
        }

        return tcnudListToDetail(dataList, minProfit, maxProfit);
    }

    private List<UnrealizedDetail> tcnudListToDetail(List<Tcnud> dataList, Double minProfit, Double maxProfit) {
        List<UnrealizedDetail> resultList = new ArrayList<>();
        for (Tcnud data : dataList) {
            Mstmb dataInMstmb = mstmbRepository.findByStock(data.getStock());
            double profit = getProfitability(getUnrealProfit(dataInMstmb.getNowPrice(), data.getQty(), data.getCost()), data.getCost());
            UnrealizedDetail unrealizedDetail = new UnrealizedDetail(
                    data.getTradeDate(),
                    data.getBranchNo(),
                    data.getCustSeq(),
                    data.getDocSeq(),
                    data.getStock(),
                    dataInMstmb.getStockName(),
                    data.getBuyPrice(),
                    dataInMstmb.getNowPrice(),
                    data.getQty(),
                    data.getRemainQty(),
                    data.getFee(),
                    data.getCost(),
                    getMarketValue(dataInMstmb.getNowPrice(), data.getQty()),
                    getUnrealProfit(dataInMstmb.getNowPrice(), data.getQty(), data.getCost()),
                    String.format("%.2f", getProfitability(getUnrealProfit(dataInMstmb.getNowPrice(), data.getQty(), data.getCost()), data.getCost())) + "%"
            );

            if (minProfit == null && maxProfit == null ) {
                resultList.add(unrealizedDetail);
                continue;
            }

            if (minProfit == null && maxProfit != null) {
                if (profit <= maxProfit) resultList.add(unrealizedDetail);
                continue;
            }

            if (minProfit != null && maxProfit == null) {
                if (profit >= minProfit) resultList.add(unrealizedDetail);
                continue;
            }

            if (profit >= minProfit && profit <= maxProfit) {
                resultList.add(unrealizedDetail);
            }


        }

        return resultList;
    }

    public List<UnrealizedDetail> getDetail(UnrealizedRequest request) throws TcnudNotFoundException {
        if (request.getStock() == null) {
            return getDetailList(request.getBranchNo(), request.getCustSeq(), request.getMinProfit(), request.getMaxProfit());
        } else {
            return getDetailList(request.getBranchNo(), request.getCustSeq(), request.getStock(), request.getMinProfit(), request.getMaxProfit());
        }
    }

    public List<UnrealizedProfit> sumProfit(UnrealizedRequest request) throws TcnudNotFoundException {
        Map<String, List<UnrealizedDetail>> detailMap = getDetail(request).stream().collect(
                Collectors.groupingBy(UnrealizedDetail::getStock)
        );

        List<UnrealizedProfit> results = new ArrayList<>();

        for (Map.Entry<String, List<UnrealizedDetail>> entry : detailMap.entrySet()) {
            String stock = entry.getKey();
            List<UnrealizedDetail> stockDetail = entry.getValue();

            Mstmb dataInMstmb = mstmbRepository.findByStock(stock);


            double sumFee = 0, sumCost = 0, sumMarketValue = 0, sumUnrealProfit = 0;
            int sumRemainQty = 0;

            for (UnrealizedDetail data : stockDetail) {
                sumRemainQty += data.getRemainQty();
                sumFee += data.getFee();
                sumCost += data.getCost();
                sumMarketValue += data.getMarketValue();
                sumUnrealProfit += data.getUnrealProfit();
            }

            UnrealizedProfit result = new UnrealizedProfit();

            result.setStock(dataInMstmb.getStock());
            result.setStockName(dataInMstmb.getStockName());
            result.setNowPrice(dataInMstmb.getNowPrice());
            result.setSumRemainQty(sumRemainQty);
            result.setSumFee(sumFee);
            result.setSumCost(sumCost);
            result.setSumMarketValue(sumMarketValue);
            result.setSumUnrealProfit(sumUnrealProfit);
            result.setDetailList(stockDetail);
            result.setSumProfitability(String.format("%.2f", getProfitability(result.getSumUnrealProfit(), result.getSumCost())) + "%");

            results.add(result);
        }

        return results;
    }

    public String todaySettlement(SettlementRequest request) {

        Calendar today = Calendar.getInstance();
        if (1 == today.get(Calendar.DAY_OF_WEEK) || 7 == today.get(Calendar.DAY_OF_WEEK)) {
            return "今天是假日，無需付交割金";
        }
        Calendar target = Calendar.getInstance();
        target.add(Calendar.DATE, -2);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        while (0 != today.compareTo(target)) {
            today.add(Calendar.DATE, -1);
            if (null != holidayRepository.findByHoliday(sdf.format(today.getTime())) || 1 == today.get(Calendar.DAY_OF_WEEK) || 7 == today.get(Calendar.DAY_OF_WEEK)) {
                target.add(Calendar.DATE, -1);
            }
        }
        Double deliveryFee = tcnudRepository.getDeliveryFee(request.getBranchNo(), request.getCustSeq(), sdf.format(target.getTime()));

        if (null == deliveryFee) {
            return "今日無交割金需付";
        }

        return "今日需付交割金為 : " + deliveryFee;
    }

    public Double getMarketValue(double price, int qty) {
        double marketValue, fee, amt, tax;
        amt = Math.floor(price * qty);
        fee = Math.floor(amt * 0.001425);
        tax = Math.floor(amt * 0.003);
        marketValue = (price * qty) - fee - tax;

        return marketValue;
    }

    public Double getUnrealProfit(double price, int qty, double cost) {
        double unrealProfit, fee, amt, tax;
        amt = Math.floor(price * qty);
        fee = Math.floor(amt * 0.001425);
        tax = Math.floor(amt * 0.003);
        unrealProfit = (amt - fee - tax) - cost;
        return unrealProfit;
    }

    public double getProfitability(double unrealProfit, double cost) {
        double profitability;
        profitability = (unrealProfit / cost) * 100;

        return profitability;
    }

}
