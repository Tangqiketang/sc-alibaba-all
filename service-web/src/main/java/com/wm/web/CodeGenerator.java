package com.wm.web;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * @Author Wang Min
 * @Date 2020-07-28
 * @Description
 */

public class CodeGenerator {

    private static final String MODULE_NAME = "service-web";

    private static final String TEMPLATE_PATH = "/templates/mapper.xml.vm";

    private static final String HOST_IP_ADDRESS = "localhost";


    public static void main(String[] args) {
        entityGenerator();
    }

    public static void entityGenerator() {

        String[] tables = new String[]{
                "ipc_camera"
        };
        InjectionConfig injectionConfig = new InjectionConfig() {
            //自定义属性注入:abc
            //在.ftl(或者是.vm)模板中，通过${cfg.abc}获取属性
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };
        AutoGenerator mpg = new AutoGenerator();
        mpg.setCfg(injectionConfig);
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath+ "/" + MODULE_NAME +"/src/main/java");
        //gc.setOutputDir(projectPath + "/jeecg-boot-module-system/src/main/java");
        gc.setFileOverride(true);//是否覆盖
        gc.setActiveRecord(true);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setSwagger2(true);
        gc.setAuthor("Wang Min");
        gc.setOpen(false);
        gc.setDateType(DateType.TIME_PACK);
        gc.setIdType(IdType.AUTO);
//		gc.setIdType(IdType.NONE);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setUrl("jdbc:mysql://192.168.40.131:3306/service-web?characterEncoding=UTF-8&zeroDateTimeBehavior" +
                "=convertToNull&useSSL=true&verifyServerCertificate=false");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //strategy.setTablePrefix(new String[]{"_"});// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setEntityBuilderModel(true);
        strategy.setInclude(tables);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(null);
        pc.setEntity("com.wm.web.entity");
        pc.setMapper("com.wm.web.mapper");
        pc.setXml("com.wm.web.entity.xml");
        String generateCaS = scanner("!!!是否覆盖当前的controller和service(是:1)");
        if ("1".equals(generateCaS)) {
            pc.setService("com.wm.web.service");       //本项目没用，生成之后删掉
            pc.setServiceImpl("com.wm.web.service.impl");   //本项目没用，生成之后删掉
            pc.setController("com.wm.web.controller");
        } else {
            pc.setService("TTTT");       //本项目没用，生成之后删掉
            pc.setServiceImpl("TTTT");   //本项目没用，生成之后删掉
            pc.setController("TTTT");
        }
        mpg.setPackageInfo(pc);


        // 执行生成
        mpg.execute();

        System.out.println("mybatis-plus generator over.");

    }

    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (!StringUtils.isBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

}
