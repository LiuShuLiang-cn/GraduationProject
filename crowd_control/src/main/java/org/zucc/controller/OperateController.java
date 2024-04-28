package org.zucc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zucc.entity.Operate;
import org.zucc.service.OperateService;

@Controller
@RequestMapping("/operate")
@RequiredArgsConstructor
@CrossOrigin
public class OperateController {
    private final OperateService operateService;

    @GetMapping("/{systemId}")
    @ResponseBody
    public Operate get(@PathVariable String systemId) {
        return operateService.queryStatus(systemId);
    }

    @GetMapping("/{systemId}/{activate}/{status}")
    @ResponseBody
    public String set(@PathVariable("systemId") String systemId,
                      @PathVariable("activate") String activate,
                      @PathVariable("status") String status) {
        operateService.update(systemId,activate,status);
        return "switch";
    }

}
