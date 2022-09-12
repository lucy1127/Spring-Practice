package com.example.project.service;


import com.example.project.controller.dto.request.AddBalanceRequest;
import com.example.project.controller.dto.request.UnrealizedRequest;
import com.example.project.controller.dto.resopnse.SumUnrealizedProfit;
import com.example.project.controller.dto.resopnse.UnrealizedDetail;
import com.example.project.controller.dto.resopnse.UnrealizedDetailResponse;
import com.example.project.controller.dto.resopnse.UnrealizedProfit;
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


    public UnrealizedDetailResponse addBalance(AddBalanceRequest request) {

        Mstmb dataInMstmb = mstmbRepository.findByStock(request.getStock());
        if (dataInMstmb == null) {
            return new UnrealizedDetailResponse(null, "001", "查無符合資料");
        }
        if (!checkRequestParameter(request).equals("Ok")) {
            return new UnrealizedDetailResponse(null, "002", checkRequestParameter(request));
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

        return new UnrealizedDetailResponse(results, "000", "");
    }

    public List<UnrealizedDetail> getDetailList(UnrealizedRequest request) throws InvalidPropertiesFormatException, ChangeSetPersister.NotFoundException {
        Mstmb dataInMstmb = mstmbRepository.findByStock(request.getStock());

        if (!checkParameter(request).equals("Ok")) {
            throw new InvalidPropertiesFormatException(checkParameter(request));
        }


        List<Tcnud> dataList;
        if (null == request.getStock()) {
            dataList = tcnudRepository.findByBranchNoAndCustSeq(request.getBranchNo(), request.getCustSeq());
        } else {
            if (null == dataInMstmb) {
                throw new ChangeSetPersister.NotFoundException();
            }
            dataList = tcnudRepository.findByBranchNoAndCustSeqAndStock(request.getBranchNo(), request.getCustSeq(), request.getStock());
        }

        List<UnrealizedDetail> resultList = new ArrayList<>();

        for (Tcnud data : dataList) {
            dataInMstmb = mstmbRepository.findByStock(data.getStock());
            String profit = getProfitability(getUnrealProfit(dataInMstmb.getNowPrice(), data.getQty(), data.getCost()),data.getCost());
            if(profit.compareTo(request.getMinProfit()) > 0 && profit.compareTo(request.getMaxProfit()) < 0 ) {
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

    public UnrealizedDetailResponse getDetail(UnrealizedRequest request) {

        UnrealizedDetailResponse response = new UnrealizedDetailResponse();
        try {
            response.setResultList(getDetailList(request));
            response.setResponseCode("000");
            response.setMessage("");
        } catch (ChangeSetPersister.NotFoundException e) {
            response.setResponseCode("001");
            response.setMessage("查無符合資料");
        } catch (InvalidPropertiesFormatException e) {
            response.setResponseCode("002");
            response.setMessage(checkParameter(request));
        } catch (Exception e) {
            response.setResponseCode("005");
            response.setMessage("伺服器內部錯誤");
        }

        return response;
    }

    public SumUnrealizedProfit sumProfit(UnrealizedRequest request) throws InvalidPropertiesFormatException, ChangeSetPersister.NotFoundException {
        Mstmb dataInMstmb = mstmbRepository.findByStock(request.getStock());
        if (!checkParameter(request).equals("Ok")) {
            return new SumUnrealizedProfit(null, "002", checkParameter(request));
        }
        if(request.getStock()!=null) {
            if (dataInMstmb == null) {
                return new SumUnrealizedProfit(null, "001", "查無符合資料");
            }
        }


        List<UnrealizedDetail> detailList = getDetailList(request);

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
    //優化
    public String checkParameter(UnrealizedRequest request) {
        if (request.getBranchNo().isEmpty()) {
            return "分行不能為空值";
        }
        if (request.getCustSeq().isEmpty()) {
            return "客戶帳號不能為空值";
        }

        return "Ok";
    }
    public String checkRequestParameter(AddBalanceRequest request) {
        if (request.getBranchNo().isEmpty()) {
            return "分行不能為空值";
        }
        if (request.getCustSeq().isEmpty()) {
            return "客戶帳號不能為空值";
        }

        if (request.getTradeDate().isEmpty()) {
            return "交易日期不能為空值";
        }
        if (request.getStock().isEmpty()) {
            return "股票不能為空值";
        }
        if (request.getBuyPrice() == null) {
            return "請輸入買進價格 !!!";
        }

        if (request.getQty() == null) {
            return "請輸入股數 !!!";
        }

        return "Ok";
    }
}
