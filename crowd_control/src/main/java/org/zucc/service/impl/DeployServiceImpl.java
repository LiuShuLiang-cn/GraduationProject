package org.zucc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.zucc.dao.DeployDao;
import org.zucc.dao.SystemDao;
import org.zucc.entity.Deploy;
import org.zucc.entity.NumberOfPeople;
import org.zucc.entity.vo.DeployVo;
import org.zucc.service.DeployService;
import org.zucc.service.NumberOfPeopleService;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class DeployServiceImpl extends ServiceImpl<DeployDao, Deploy> implements DeployService {
    @Resource
    private DeployDao deployDao;
    @Resource
    private RedisTemplate<String, List<Object>> redisTemplate;
    @Resource
    private NumberOfPeopleService numberOfPeopleService;
    @Resource
    private SystemDao systemDao;

    @Override
    public void initDeploy(String systemName) {
        /*
        初始化部署人数
         */
        double[][] a =
                {
                        {120.160988, 30.257241},// 钱塘里
                        {120.163306, 30.256641},// 长生里
                        {120.161679, 30.256041},// 劝业里
                        {120.162178, 30.254551},// 学士里
                        {120.163452, 30.254666},//龙翔里
                        {120.162555, 30.252937},//仁和里
                        {120.163585, 30.252126},//东坡里
                        {120.162707, 30.249315},//将军里
                        {120.165169, 30.249562},//泗水里
                        {120.161503, 30.253908},//
                        {120.160966, 30.254118},
                        {120.161199, 30.253807}
                };
        QueryWrapper<Deploy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sysName", systemName);
        List<Deploy> deploys = baseMapper.selectList(queryWrapper);
        if (deploys.isEmpty())
            for (int i = 0; i < 12; i++) {
                Deploy deploy = new Deploy();
                deploy.setZyz(10);
                deploy.setCg(0);
                deploy.setJj(0);
                deploy.setGa(0);
                deploy.setSysName(systemName);
                deploy.setCgLat(a[i][1]);//纬度
                deploy.setCgLng(a[i][0]);//经度
                deploy.setJjLat(a[i][1]);//纬度
                deploy.setJjLng(a[i][0]);//经度
                deploy.setGaLat(a[i][1]);//纬度
                deploy.setGaLng(a[i][0]);//经度
                deploy.setZyzLat(a[i][1]);//纬度
                deploy.setZyzLng(a[i][0]);//经度
                baseMapper.insert(deploy);
            }
    }

    @Override
    public void directivesDeploy(DeployVo deployVo) {
        String systemName = systemDao.selectById(deployVo.getSystemId()).getSystemName();
        deployVo.setSystemName(systemName);
        /*
        部署人员
         */
        switch (deployVo.getRole()) {
            case "志愿者":
                deployDao.updateZyz(deployVo);
                break;
            case "城管":
                deployDao.updateCg(deployVo);
                break;
            case "交警":
                deployDao.updateJj(deployVo);
                break;
            case "公安":
                deployDao.updateGa(deployVo);
                break;
        }

        //List<Deploy> deploys = deployDao.getNumBySys(systemName);
        redisTemplate.delete(systemName + "_Deploys");
        log.info("部署了" + deployVo.getNum() + "人");
    }
    @Override
    public String transPeople(String systemId, String from, String to, int number) {
        String systemName = systemDao.selectById(systemId).getSystemName();
        QueryWrapper<NumberOfPeople> wrapper = new QueryWrapper<>();
        wrapper.eq("systemName", systemName);
        List<NumberOfPeople> numberOfPeopleList = numberOfPeopleService.list(wrapper);
        if (numberOfPeopleList == null || numberOfPeopleList.size() == 0) {
            return "系统错误！";
        }
        try {
            numberOfPeopleList.forEach(numberOfPeople -> {
                if (numberOfPeople.getRegion().equals(from)) {
                    if (numberOfPeople.getNumber() - number<0){
                        throw new RuntimeException("输入的人数超出了转移的范围");
                    }
                    numberOfPeople.setNumber(numberOfPeople.getNumber() - number);
                    numberOfPeopleService.updateById(numberOfPeople);
                } else if (numberOfPeople.getRegion().equals(to)) {
                    numberOfPeople.setNumber(numberOfPeople.getNumber() + number);
                    numberOfPeopleService.updateById(numberOfPeople);
                }
            });
        }catch (RuntimeException e){
            return e.getMessage();
        }
        redisTemplate.delete(systemName + "_NumberOfPeople");
        return "转移成功！";
    }

}
