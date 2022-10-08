package com.wm.web.mahout.model;

import org.apache.mahout.cf.taste.impl.model.jdbc.ConnectionPoolDataSource;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-09-12 23:20
 */
@Component
public class MyDataModel {

    @Resource
    private DataSource dataSource;


    public JDBCDataModel myDataModel() {

        JDBCDataModel dataModel = null;
        try {

/*            dataSource.setServerName("192.168.40.131");
            dataSource.setUser("root");
            dataSource.setPassword("123456");
            dataSource.setDatabaseName("service-web");*/


            ConnectionPoolDataSource connectionPool=new ConnectionPoolDataSource(dataSource);
            // use JNDI
            dataModel = new MySQLJDBCDataModel(connectionPool,"movie_preferences", "userID", "movieID","preference");


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return dataModel;
    }

}