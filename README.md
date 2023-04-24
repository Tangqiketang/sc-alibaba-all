项目为微服务开发脚手架。
本着各模块独立的原则。
集成日志模块cloud-logging-starter，通过注解形式开启组件。
z-deploy模块包含了docker部署配置


nacos:注册中心 startup.cmd -m standalone
    8848  http://localhost:8848/nacos



sentinel:
8748
java -Dserver.port=8748 -Dcsp.sentinel.dashboard.server=localhost:8748 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.3.jar
sentinel/sentinel

gateway-server: 网关
    8080
auth-server:认证中心
    8081
    
    


