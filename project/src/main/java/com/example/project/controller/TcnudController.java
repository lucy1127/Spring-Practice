package com.example.project.controller;

import com.example.project.controller.dto.resopnse.StatusResponse;
import com.example.project.model.entity.Tcnud;
import com.example.project.service.TcnudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tcnud")
public class TcnudController {

    @Autowired
    TcnudService tcnudService;

    @GetMapping()
    public List<Tcnud> getDataList(){
        return tcnudService.getTcnudList();
    }

    @GetMapping("/date/branch/cust/{doc}")
    public List<Tcnud> getData(@RequestParam String date, @RequestParam String branch, @RequestParam String cust, @PathVariable String doc){
        return tcnudService.getDataByDocSeq(date,branch,cust,doc);
    }

   /*@PutMapping("/{docSeq}")
    public StatusResponse updateDate(@PathVariable String docSeq, @RequestBody UpdateTcnudRequest request) {
        String userUpdate = this.tcnudService.upDateTchud(docSeq,request);
        return new StatusResponse(userUpdate);
    }*/

    @DeleteMapping("/{docSeq}")
    public StatusResponse deleteData(@PathVariable String docSeq) {
        String userDelete = this.tcnudService.deleteTcnud(docSeq);
        return new StatusResponse(userDelete);
    }

}
