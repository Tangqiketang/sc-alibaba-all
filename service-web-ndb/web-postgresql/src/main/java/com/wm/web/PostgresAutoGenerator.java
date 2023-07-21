package com.wm.web;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.GeneratorBuilder;
import com.baomidou.mybatisplus.generator.config.querys.PostgreSqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2023-03-13 15:40
 */
public class PostgresAutoGenerator {

    private static final String MODULE_NAME = "service-web-ndb/web-postgresql";

    public static void main(String[] args) {
        String[] tables = new String[]{
                "test_user"
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
        DataSourceConfig dsc = new DataSourceConfig.Builder("jdbc:postgresql://192.168.40.131:5432/postgres?characterEncoding=UTF-8&zeroDateTimeBehavior" +
                "=convertToNull&useSSL=true&verifyServerCertificate=false","root","123456")
                .dbQuery(new PostgreSqlQuery())
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