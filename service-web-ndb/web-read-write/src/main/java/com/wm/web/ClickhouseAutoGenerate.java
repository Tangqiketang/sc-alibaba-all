package com.wm.web;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.GeneratorBuilder;
import com.baomidou.mybatisplus.generator.config.querys.ClickHouseQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * 描述:
 * 1.mapper文件需要加上数据库注解
 * 2.xml文件需要手动移动到xml文件中
 * 3.tableId主键需要去掉
 * @auther WangMin
 * @create 2023-03-13 15:15
 */
public class ClickhouseAutoGenerate {

    private static final String MODULE_NAME = "service-web-ndb/web-read-write";

    public static void main(String[] args) {
        String[] tables = new String[]{
                "t_device_prop_history"
        };
        // 获取项目路径
        String projectPath = System.getProperty("user.dir");
        // 全局配置
        GlobalConfig gc = GeneratorBuilder.globalConfigBuilder()
                .fileOverride()
                .openDir(false)
                .outputDir(projectPath+ "/" + MODULE_NAME +"/src/main/java")
                .author("Wang Min")
                .enableSwagger()
                .dateType(DateType.TIME_PACK)
                .commentDate("yyyy-MM-dd").build();


        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig.Builder("jdbc:clickhouse://192.168.40.131:8123", "mydev", "zDYQYhT1")
                .dbQuery(new ClickHouseQuery())
                //数据库
                .schema("device_log")
                .build();
        // 包配置
        PackageConfig pc = GeneratorBuilder.packageConfigBuilder().parent(null)
                .entity("com.wm.web.model.entity")
                .mapper("com.wm.web.mapper")
                .xml("com.wm.web.mapper.xml")
                .controller("TTTT")
                .service("TTTT")
                .serviceImpl("TTTT")
                .build();

        // 策略配置
        StrategyConfig strategy = GeneratorBuilder.strategyConfigBuilder()
                .addInclude(tables)
                .addTablePrefix(new String[]{"t_"})
                .entityBuilder()
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
                .enableLombok()
                .enableChainModel()
                .enableSerialVersionUID()
                .enableTableFieldAnnotation()
                .superClass("com.baomidou.mybatisplus.extension.activerecord.Model")
                .mapperBuilder()
                .enableBaseResultMap()
                .enableBaseColumnList()
                .build();

        TemplateConfig templateConfig = GeneratorBuilder.templateConfigBuilder().build();

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator(dsc).global(gc).strategy(strategy).template(templateConfig).packageInfo(pc);

        mpg.execute(new FreemarkerTemplateEngine());

    }

}