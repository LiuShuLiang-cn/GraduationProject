package org.zucc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zucc.controller.websocket.MessageWS;
import org.zucc.dao.DeployDao;
import org.zucc.dao.NumberOfPeopleDao;
import org.zucc.dao.SystemDao;
import org.zucc.entity.Chat;
import org.zucc.entity.Deploy;
import org.zucc.entity.NumberOfPeople;
import org.zucc.entity.Systems;
import org.zucc.entity.vo.DataVo;
import org.zucc.service.ChatService;
import org.zucc.utils.CastClass;

import javax.annotation.Resource;
import java.util.List;

@Controller
@CrossOrigin
public class ChatController {
    @Resource
    private SystemDao systemDao;
    @Resource
    private MessageWS webSocket;
    @Resource
    private NumberOfPeopleDao numberOfPeopleDao;
    @Resource
    private DeployDao deployDao;

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private ChatService chatService;

    /**
     * 通过websocket传送数据给前端
     *
     * @param systemName 系统名称
     */
    @GetMapping("/data/{systemId}")
    @ResponseBody
    public DataVo sendData(@PathVariable("systemId") String systemId) {
        String systemName = null;
        systemName=systemDao.selectById(systemId).getSystemName();
        Object object;
        object = redisTemplate.opsForValue().get(systemName + "_NumberOfPeople");
        List<NumberOfPeople> peoples = CastClass.castList(object, NumberOfPeople.class);
        object = redisTemplate.opsForValue().get(systemName + "_Deploys");
        List<Deploy> deploys = CastClass.castList(object, Deploy.class);
        String time = (String) redisTemplate.opsForValue().get(systemName + "_Time");
        if (peoples == null || deploys == null || time == null||deploys.isEmpty()||deploys==null) {
            peoples = numberOfPeopleDao.getNumBySys(systemName);
            deploys = deployDao.getNumBySys(systemName);
            QueryWrapper<Systems> systemsQueryWrapper = new QueryWrapper<>();
            systemsQueryWrapper.eq("systemname", systemName);
            time = systemDao.selectOne(systemsQueryWrapper).getRunTime();
            redisTemplate.opsForValue().set(systemName + "_NumberOfPeople", peoples);
            redisTemplate.opsForValue().set(systemName + "_Deploys", deploys);
            redisTemplate.opsForValue().set(systemName + "_Time", time);
        }
        DataVo vo = new DataVo();
        vo.setNumberOfPeopleList(peoples);
        vo.setDeployList(deploys);
        vo.setSystemTime(time);
        //webSocket.sendOneMessage("21", JSON.toJSONString(vo));
        return vo;
    }

    @GetMapping("/command")
    @ResponseBody
    public List<Chat> getCommand(@RequestParam("systemId") Integer systemId,
                                 @RequestParam("roleTopic") String roleTopic) {
        /*
        获取没有完成的指令
         */
        return chatService.getAllTODO(systemId, roleTopic);
    }

    @GetMapping("/command/success/{id}")
    @ResponseBody
    public String toDoSuccess(@PathVariable("id") int id) {
        return chatService.finishTODO(id);
    }

    @GetMapping("/msg")
    public void sendMessage(@RequestParam Chat chat) {
        chatService.save(chat);
    }

    @GetMapping("/msg/chat")
    public List<Chat> getChats(@RequestParam("systemName") String systemName,
                         @RequestParam("roleTopic") String roleTopic){
        QueryWrapper<Chat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("systemName", systemName);
        queryWrapper.eq("toRole", roleTopic);
        queryWrapper.ne("type", 1);
        return chatService.list(queryWrapper);
    }
}
