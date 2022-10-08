1.引入common-seata包中pom文件依赖。注意不能单独引入这个包.
2.项目启动类增加注解
@EnableTransactionManagement
@EnableFeignClients 
3.项目中数据库增加undo_log表、application.yml中增加配置
4.项目方法增加@GlobalTransactional