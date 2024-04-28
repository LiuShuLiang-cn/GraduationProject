package org.zucc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zucc.entity.Operate;

public interface OperateService extends IService<Operate> {
    void initOperate(String systemName);

    void update(String systemId, String activate,String status);

    Operate queryStatus(String systemId);

}
