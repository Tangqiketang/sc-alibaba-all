package com.wm.web.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-06-01 17:52
 */
@Configuration
@EnableOpenApi //开启swagger
public class SwaggerConfig implements EnvironmentAware {

    private String applicationName;

    private String applicationDescription;

    @Bean
    public Docket createRestApi(){
        //返回文档概要信息
        return new Docket(DocumentationType.OAS_30)
                .groupName(applicationName)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(getGlobalRequestParameters())
                .globalResponses(HttpMethod.GET,getGlobalResponseMessage())
                .globalResponses(HttpMethod.POST,getGlobalResponseMessage());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(applicationName+"接口文档")
                .description(applicationDescription)
                .contact(new Contact("wangmin", "https://github.com/Tangqiketang", "641718840@qq.com"))
                .version("1.0")
                .build();
    }

    /*
    封装全局通用参数
     */
    private List<RequestParameter> getGlobalRequestParameters() {
        List<RequestParameter> requestParameters = new ArrayList<>();
        RequestParameterBuilder builder = new RequestParameterBuilder();
        requestParameters.add(
                builder.name("token")
                        .required(false)
                        .in(ParameterType.HEADER)
                        .build()
        );
        return requestParameters;
    }
    /*
    封装通用相应信息
     */
    private List<Response> getGlobalResponseMessage() {
        List<Response> responseList=new ArrayList<>();
        responseList.add(new ResponseBuilder().code("404").description("未找到资源").build());
        return responseList;
    }



    @Override
    public void setEnvironment(Environment environment) {
        this.applicationDescription = environment.getProperty("spring.application.description");
        this.applicationName = environment.getProperty("spring.application.name");
    }
}