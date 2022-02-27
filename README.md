nacos:注册中心
    8848  http://localhost:8848/nacos
service-hi: 被feign调用的服务
    8762-8764
service-feign: 使用feign或ribbon调用service-hi
    8765-

sentinel:
8748
java -Dserver.port=8748 -Dcsp.sentinel.dashboard.server=localhost:8748 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.1.jar


gateway-server: 网关
    8080
    
    


