package org.zucc;

import com.sun.xml.internal.ws.api.pipe.Tube;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.zucc.controller.websocket.MessageWS;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi
@SpringBootApplication
@EnableScheduling //开启定时任务
@Slf4j
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(MyApplication.class);
        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
        // 是否打印beanName
        getBeanName(configurableApplicationContext, true);
        // websocket 中需要使用容器来获取bean
        MessageWS.setApplicationContext(configurableApplicationContext);
    }

    static void getBeanName(ConfigurableApplicationContext context, Boolean isLog) {
        if (!isLog) return;
        for (String name : context.getBeanDefinitionNames()) {
            log.info(name);
        }
    }
}