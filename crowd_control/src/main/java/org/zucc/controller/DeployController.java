package org.zucc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zucc.entity.vo.DeployVo;
import org.zucc.service.DeployService;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Api(value = "部署管理")
public class DeployController {
    private final DeployService deployService;
    @GetMapping ("/directives/deploy")
    @ApiOperation("部署警力")
    public void directivesDeploy(DeployVo deployVo) {
        deployService.directivesDeploy(deployVo);
    }

    @GetMapping("/directives/trans")
    @ApiOperation(("转移人数"))
    public String transPeo(@RequestParam("systemId") String systemId,
                           @RequestParam("from") String from,
                           @RequestParam("to") String to,
                           @RequestParam("number") String number) {
        return deployService.transPeople(systemId, from, to, Integer.parseInt(number));
    }
}
