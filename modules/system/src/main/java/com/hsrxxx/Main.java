package com.hsrxxx;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: ordersystem
 * @description:
 * @author: hsrxxx
 * @create: 2021-01-19 16:29
 **/
public class Main {
    public static void main(String[] args) {
        // new 自动代码生成器对象
        AutoGenerator autoGenerator = new AutoGenerator();

        // new 数据源 设置好数据源信息后装载进生成器
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("981222");
        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/ordersystem?useUnicode=true&characterEncoding=UTF-8");
        autoGenerator.setDataSource(dataSourceConfig);

        // new 全局配置对象
        GlobalConfig globalConfig = new GlobalConfig();
        //不打开生产的文件
//        globalConfig.setOpen(true);
        //不覆盖之前生成的文件
//        globalConfig.setFileOverride(false);
        // 输出地址
        globalConfig.setOutputDir(System.getProperty("modules.dir") + "/system/src/main/java");
        globalConfig.setAuthor("松仁");
        globalConfig.setServiceName("%sService");
        autoGenerator.setGlobalConfig(globalConfig);

        // new 包配置对象
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.hsrxxx");
        packageConfig.setEntity("entity");
        packageConfig.setMapper("mapper");
        packageConfig.setController("controller");
        packageConfig.setService("service");
        packageConfig.setServiceImpl("service.impl");
        autoGenerator.setPackageInfo(packageConfig);

        // new 生成策略对象
        StrategyConfig strategyConfig = new StrategyConfig();
        //可以用同配符号:表示生成t_开头的对应库下所有表
//        strategyConfig.setInclude("t"+"_\\w*");
//        strategyConfig.setInclude("t_order");
        //实体是否为lombok模型（默认 false）
        strategyConfig.setEntityLombokModel(true);
        //生成 @RestController 控制器
        strategyConfig.setRestControllerStyle(true);
        //设置逻辑删除字段 要和数据库中表对应哈
        strategyConfig.setLogicDeleteFieldName("del_flag");
        //下划线转驼峰
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);

        //设置创建时间和更新时间自动填充策略
        List<TableFill> list = new ArrayList<>();
        TableFill tableFill1 = new TableFill("create_time", FieldFill.INSERT);
        TableFill tableFill2 = new TableFill("update_time", FieldFill.INSERT_UPDATE);
        list.add(tableFill1);
        list.add(tableFill2);

        // 自动填充某些字段（创建时间...）
        strategyConfig.setTableFillList(list);

        autoGenerator.setStrategy(strategyConfig);

        autoGenerator.execute();
    }
}
