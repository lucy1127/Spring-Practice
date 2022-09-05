package com.example.project.controller;


import com.example.project.controller.dto.request.CreateHcmioRequest;
import com.example.project.controller.dto.resopnse.StatusResponse;
import com.example.project.model.entity.Hcmio;
import com.example.project.service.HcmioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/hcmio")
public class HcmioController {

    @Autowired
    HcmioService hcmioService;

    @GetMapping()
    public List<Hcmio> getHcmioDataList(){
        return this.hcmioService.getHcmioList();
    }

    @GetMapping("/date/branch/cust/{doc}")
    public List<Hcmio> getHcmioData(@RequestParam String date,@RequestParam String branch,@RequestParam String cust,@PathVariable String doc){
        return this.hcmioService.getDataByDocSeq(date,branch,cust,doc);
    }

    @PostMapping()
    public StatusResponse createHcmioData(@RequestBody CreateHcmioRequest request) {
        String result = this.hcmioService.createHcmio(request);
        return new StatusResponse(result);
    }

    @DeleteMapping("/{docSeq}")
    public StatusResponse deleteHcmioData(@PathVariable String docSeq) {
        String result = this.hcmioService.deleteHcmio(docSeq);
        return new StatusResponse(result);
    }



}
