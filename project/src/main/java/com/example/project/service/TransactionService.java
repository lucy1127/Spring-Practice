package com.example.project.service;


import com.example.project.controller.dto.request.AddBalanceRequest;
import com.example.project.controller.dto.request.UnrealizedRequest;
import com.example.project.controller.dto.resopnse.SumUnrealizedProfit;
import com.example.project.controller.dto.resopnse.UnrealizedDetail;
import com.example.project.controller.dto.resopnse.UnrealizedDetailResponse;
import com.example.project.controller.dto.resopnse.UnrealizedProfit;
import com.example.project.controller.error.MstmbNotFoundException;
import com.example.project.controller.error.TcnudNotFoundException;
import com.example.project.model.MstmbRepository;
import com.example.project.model.TcnudRepository;
import com.example.project.model.entity.Hcmio;
import com.example.project.model.entity.Mstmb;
import com.example.project.model.entity.Tcnud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

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
                getProfitability(getUnrealProfit(dataInMstmb.getNowPrice(), tcnud.getQty(), tcnud.getCost()),tcnud.getCost())
        ));

        return results;
    }

    public List<UnrealizedDetail> getDetailList(String branchNo, String custSeq, String minProfit, String maxProfit) {
        List<Tcnud> dataList = tcnudRepository.findByBranchNoAndCustSeq(branchNo, custSeq);
        if(dataList.isEmpty()) {
            throw new TcnudNotFoundException();
        }

        return tcnudListToDetail(dataList, minProfit, maxProfit);
    }

    public List<UnrealizedDetail> getDetailList(String branchNo, String custSeq, String stock, String minProfit, String maxProfit) {
        List<Tcnud> dataList = tcnudRepository.findByBranchNoAndCustSeqAndStock(branchNo, custSeq, stock);
        if(dataList.isEmpty()) {
            throw new TcnudNotFoundException();
        }
        
        return tcnudListToDetail(dataList, minProfit, maxProfit);
    }

    private List<UnrealizedDetail> tcnudListToDetail(List<Tcnud> dataList, String minProfit, String maxProftit) {
        List<UnrealizedDetail> resultList = new ArrayList<>();

        for (Tcnud data : dataList) {
            Mstmb dataInMstmb = mstmbRepository.findByStock(data.getStock());
            String profit = getProfitability(getUnrealProfit(dataInMstmb.getNowPrice(), data.getQty(), data.getCost()),data.getCost());
            if(profit.compareTo(minProfit) > 0 && profit.compareTo(maxProftit) < 0 ) {
                resultList.add(new UnrealizedDetail(
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
                        getProfitability(getUnrealProfit(dataInMstmb.getNowPrice(), data.getQty(), data.getCost()), data.getCost())
                ));
            }
        }
        return resultList;
    }

    public List<UnrealizedDetail> getDetail(UnrealizedRequest request) throws TcnudNotFoundException{
        if(request.getStock() == null) {
            return getDetailList(request.getBranchNo(), request.getCustSeq(), request.getMinProfit(), request.getMaxProfit());
        }
        else {
            return getDetailList(request.getBranchNo(), request.getCustSeq(), request.getStock(), request.getMinProfit(), request.getMaxProfit());
        }
    }

    public SumUnrealizedProfit sumProfit(UnrealizedRequest request) throws TcnudNotFoundException, MstmbNotFoundException {        
        List<UnrealizedDetail> detailList = getDetail(request);

        Mstmb dataInMstmb = mstmbRepository.findByStock(request.getStock());
        if (dataInMstmb == null) {
            throw new MstmbNotFoundException();
            // return new SumUnrealizedProfit(null, "001", "查無符合資料");
        }

        double sumFee = 0, sumCost = 0, sumMarketValue = 0, sumUnrealProfit = 0;
        int sumRemainQty = 0;

        for (UnrealizedDetail data : detailList) {
            sumRemainQty += data.getRemainQty();
            sumFee += data.getFee();
            sumCost += data.getCost();
            sumMarketValue += data.getMarketValue();
            sumUnrealProfit += data.getUnrealProfit();
        }

        UnrealizedProfit result = new UnrealizedProfit();

        result.setStock(request.getStock());
        result.setStockName(dataInMstmb.getStockName());
        result.setNowPrice(dataInMstmb.getNowPrice());
        result.setSumRemainQty(sumRemainQty);
        result.setSumFee(sumFee);
        result.setSumCost(sumCost);
        result.setSumMarketValue(sumMarketValue);
        result.setSumUnrealProfit(sumUnrealProfit);
        result.setDetailList(detailList);
        result.setSumProfitability(getProfitability(result.getSumUnrealProfit(),result.getSumCost()));

        List<UnrealizedProfit> results = Arrays.asList(result);

        return new SumUnrealizedProfit(results, "000", "");
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
    public String getProfitability(double unrealProfit,double cost){
        double profitability;
        profitability = unrealProfit/cost;
        DecimalFormat df = new DecimalFormat("00.00%");
        return df.format(profitability);
    }
    
}
