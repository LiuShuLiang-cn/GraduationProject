package org.zucc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zucc.dao.OperateDao;
import org.zucc.entity.Operate;
import org.zucc.service.OperateService;
import org.zucc.service.SystemService;

import javax.annotation.Resource;

@Service
public class OperateServiceImpl extends ServiceImpl<OperateDao, Operate> implements OperateService {
    @Override
    public void initOperate(String systemName) {
        Operate operate = new Operate();
        operate.setSystemName(systemName);
        baseMapper.insert(operate);
    }

    @Override
    public void update(String systemId, String activate,String status) {
        String systemName = systemService.getById(systemId).getSystemName();
        QueryWrapper<Operate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("systemName", systemName);
        UpdateWrapper<Operate> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("systemName",systemName).set(activate, status);
        baseMapper.update(baseMapper.selectOne(queryWrapper), updateWrapper);
    }

    @Resource
    private SystemService systemService;

    @Override
    public Operate queryStatus(String systemId) {
        QueryWrapper<Operate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("systemName", systemService.getById(systemId).getSystemName());
        return baseMapper.selectOne(queryWrapper);
    }
}
