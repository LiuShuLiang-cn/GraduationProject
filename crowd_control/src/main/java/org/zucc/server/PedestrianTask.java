package org.zucc.server;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zucc.dao.SystemDao;
import org.zucc.entity.*;
import org.zucc.service.NumberOfPeopleService;
import org.zucc.service.OperateService;
import org.zucc.utils.CastClass;
import org.zucc.utils.Constant;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 区域间人流移动
 */
@Component
@Slf4j
public class PedestrianTask {
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private OperateService operateService;
    @Resource
    private SystemDao systemDao;

    public static void getWeight(List<Weight> categorys) {
        int weightSum = 0;
        for (Weight wc : categorys) {
            weightSum += wc.getWeight();
        }

        if (weightSum <= 0) {
            log.error("Error: weightSum={}", weightSum);
        }
        Random random = new Random();
        int n = random.nextInt(weightSum); // n in [0, weightSum)
        int m = 0;
        for (Weight wc : categorys) {
            if (m <= n && n < m + wc.getWeight()) {
                int num = wc.getNum();
                num++;
                wc.setNum(num);
                break;
            }
            m += wc.getWeight();
        }
    }

    @Scheduled(fixedDelay = 8 * 1000)
    public void interareaTask() {
        Set<String> keys = redisTemplate.keys("*" + "_NumberOfPeople");
        assert keys != null;
        if (keys.isEmpty() || keys.size() == 0) {
            return;
        }
        for (String key : keys) {
            String[] parts = key.split("_");
            transferPedestrian(parts[0]);
        }
    }
@Resource
private NumberOfPeopleService numberOfPeopleService;
    public void transferPedestrian(String systemName) {
        int count = 0;
        Object object = redisTemplate.opsForValue().get(systemName + "_NumberOfPeople");
        List<NumberOfPeople> numberOfPeopleList = CastClass.castList(object, NumberOfPeople.class);
        if (numberOfPeopleList.size() != 9) {
            //QueryWrapper<NumberOfPeople> wrapper = new QueryWrapper<>();
            //wrapper.eq("systemName", systemName);
            //numberOfPeopleList=numberOfPeopleService.list(wrapper);
            //return;
        }
        List<NumberOfPeople> res = new ArrayList<>();
        for (NumberOfPeople numberOfPeople : numberOfPeopleList) {
            int number = numberOfPeople.getNumber() / Constant.TRANSFER_RATE;
            /* 流入的人数 */
            if (new Random().nextInt(100) % 2 == 0) {
                numberOfPeople.setNumber(numberOfPeople.getNumber() + number);
            } else {
                numberOfPeople.setNumber(numberOfPeople.getNumber() - number);
            }
            res.add(numberOfPeople);
            count += number;
        }
        redisTemplate.delete(systemName + "_NumberOfPeople");
        redisTemplate.opsForValue().set(systemName + "_NumberOfPeople", res);
        updateNumber(count, systemName, res);
    }

    /**
     * 根据比例转移人数
     *
     * @param num        转移人数
     * @param systemName 系统名称
     */
    private void updateNumber(int num, String systemName, List<NumberOfPeople> numberOfPeopleList) {
        List<NumberOfPeople> res = new ArrayList<>();
        QueryWrapper<Systems> wrapper = new QueryWrapper<>();
        wrapper.eq("systemname", systemName);
        int id = systemDao.selectOne(wrapper).getId();
        List<Weight> categoryList = getWeightCategoryList();
        Operate operate = (Operate) redisTemplate.opsForValue().get(systemName + "_operate");
        Object o = redisTemplate.opsForValue().get(systemName + "_Deploys");
        List<Deploy> deploys = CastClass.castList(o, Deploy.class);
        if (operate == null) {
            QueryWrapper<Operate> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("systemName", systemName);
            operate = operateService.queryStatus(id + "");
            redisTemplate.opsForValue().set(systemName + "_operate", operate);
        }
        List<Weight> categoryList2 = new ArrayList<>();
        for (Weight weight : categoryList) {
            //检查音乐喷泉活动是否开启
            if (operate.getStatusActivity().equals("1") && ("学士里".equals(weight.getCategory()) || "仁和里".equals(weight.getCategory()))) {
                weight.setWeight(weight.getWeight() - 5);
                //log.info("{}:检测到音乐喷泉处于开启状态，修改{}的权重为{}", systemName, weight.getCategory(), weight.getWeight());
            }
            //交通
            weight.setWeight(weight.getWeight() + getBusAndSub(systemName) / 4);
            categoryList2.add(weight);

        }
        //检查志愿者，交警，公安，城管有多少人,修改权重
        List<Weight> weights = updateWeightByDeploy(categoryList2, deploys);
        /*根据权重新分配人数*/
        for (Weight weight : weights) {
            numberOfPeopleList.forEach(numberOfPeople -> {
                if (weight.getCategory().equals(numberOfPeople.getRegion())) {
                    if (numberOfPeople.getNumber() - weight.getWeight() * 10 >= Constant.NUMBER_MIN) {
                        /* 出去的人数 */
                        numberOfPeople.setNumber(numberOfPeople.getNumber() - weight.getWeight() * 10);
                        if (numberOfPeople.getNumber() > 2500 && numberOfPeople.getNumber() <= 5000) {
                            numberOfPeople.setStatus("yellow");
                        } else if (numberOfPeople.getNumber() > 5000) {
                            numberOfPeople.setStatus("red");
                        } else {
                            numberOfPeople.setStatus("green");
                        }

                    }
                    res.add(numberOfPeople);
                }
            });
        }
        redisTemplate.delete(systemName + "_NumberOfPeople");
        redisTemplate.opsForValue().set(systemName + "_NumberOfPeople", res);
    }

    private List<Weight> updateWeightByDeploy(List<Weight> weights, List<Deploy> deploys) {
        List<Weight> res = new ArrayList<>();
        deploys.forEach(deploy -> {
            weights.forEach(weight -> {
                if (String.valueOf(deploy.getCgLat()) .equals(weight.getLatitude()) ) {
                    int w = deploy.getCg() + deploy.getGa() * 2 + deploy.getJj() * 2 + deploy.getZyz();
                    weight.setWeight(weight.getWeight() + w);
                    res.add(weight);
                }
            });

        });
        return res;
    }

    /**
     * @return 返回九个里的权重数据
     */
    private List<Weight> getWeightCategoryList() {
        List<Weight> categoryList = new ArrayList<>();
        Weight weightCategory1 = new Weight("钱塘里", 4, 0, "120.160988", "30.257241");
        Weight weightCategory2 = new Weight("长生里", 6, 0, "120.163306", "30.256641");
        Weight weightCategory3 = new Weight("劝业里", 2, 0, "120.161679", "30.256041");
        Weight weightCategory4 = new Weight("学士里", 2, 0, "120.162178", "30.254551");
        Weight weightCategory5 = new Weight("龙翔里", 4, 0, "120.163452", "30.254666");
        Weight weightCategory6 = new Weight("仁和里", 2, 0, "120.162555", "30.252937");
        Weight weightCategory7 = new Weight("东坡里", 6, 0, "120.163585", "30.252126");
        Weight weightCategory8 = new Weight("将军里", 8, 0, "120.162707", "30.249315");
        Weight weightCategory9 = new Weight("泗水里", 8, 0, "120.165169", "30.249562");
        categoryList.add(weightCategory1);
        categoryList.add(weightCategory2);
        categoryList.add(weightCategory3);
        categoryList.add(weightCategory4);
        categoryList.add(weightCategory5);
        categoryList.add(weightCategory6);
        categoryList.add(weightCategory7);
        categoryList.add(weightCategory8);
        categoryList.add(weightCategory9);
        return categoryList;
    }

    private int getBusAndSub(String systemName) {

        Operate operate = (Operate) redisTemplate.opsForValue().get(systemName + "_operate");
        QueryWrapper<Systems> wrapper = new QueryWrapper<>();
        wrapper.eq("systemname", systemName);
        int id = systemDao.selectOne(wrapper).getId();
        if (operate == null) {
            QueryWrapper<Operate> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("systemName", systemName);
            operate = operateService.queryStatus(id + "");
            redisTemplate.opsForValue().set(systemName + "_operate", operate);
        }
        int s = 0;
        if (operate.getBusA().equals("1")) {
            s++;
        }
        if (operate.getBusB().equals("1")) {
            s++;
        }
        if (operate.getBusC().equals("1")) {
            s++;
        }
        if (operate.getBusD().equals("1")) {
            s++;
        }
        if (operate.getSubwayA().equals("1")) {
            s++;
        }
        if (operate.getSubwayB().equals("1")) {
            s++;
        }
        if (operate.getSubwayC().equals("1")) {
            s++;
        }
        if (operate.getSubwayD().equals("1")) {
            s++;
        }
        return s;
    }
}
