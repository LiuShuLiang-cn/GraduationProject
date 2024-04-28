package org.zucc.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
//下面两条是Knife4j引入的，若是只用Swagger可去掉
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
    /**
     * Swagger配置 for Knife4j
     *
     * @return
     */
    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(
                        new ApiInfoBuilder()
                                // 文档标题
                                .title("人流管理实验Swagger文档")
                                // 文档描述信息
                                .description("人员流动虚拟仿真实验系统API")
                                .contact(
                                        new Contact("ShuLiang.Liu",
                                                "https://github.com/LiuShuLiang-cn/crowd_control",
                                                "2622421660@qq.com")
                                )
                                // 文档版本号
                                .version("1.0")
                                .build()
                );
        return docket;
    }

}
