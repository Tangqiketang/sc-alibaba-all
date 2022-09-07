package com.wm.gatewayserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-06-03 20:10
 */
@Primary
@Component
public class Swagger3Config implements SwaggerResourcesProvider {

    /** swagger2或3都是v2，如果使用v3，调用接口时，服务名前缀就会丢失*/
    private static final String SWAGGER2URL = "/v2/api-docs";
    /*** 网关路由*/
    private final RouteLocator routeLocator;
    //gateway配置文件
    private final GatewayProperties gatewayProperties;

    /**
     * 网关应用名称
     */
    @Value("${spring.application.name}")
    private String self;

    @Autowired
    public Swagger3Config(RouteLocator routeLocator,GatewayProperties gatewayProperties) {
        this.routeLocator = routeLocator;
        this.gatewayProperties=gatewayProperties;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(DocumentationType.OAS_30.getVersion());
        return swaggerResource;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        //从配置文件中获取并配置SwaggerResource
        gatewayProperties.getRoutes().stream()
                //过滤路由
                .filter(routeDefinition -> routes.contains(routeDefinition.getId()))
                //循环添加，从路由的断言中获取，一般来说路由都会配置断言Path信息
                .forEach(route -> {
                    route.getPredicates().stream()
                            //获取Path信息
                            .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                            //开始添加SwaggerResource
                            .forEach(predicateDefinition -> resources.add(swaggerResource(route.getId(),
                                    predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
                                            .replace("**", "v2/api-docs?group=" + route.getId()))));
                });

        return resources;
    }
}