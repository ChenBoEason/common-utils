package com.github.x4096.common.utils.data.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;

/**
 * @Author: 0x4096.peng@gmail.com
 * @Project: common-utils
 * @DateTime: 2019-07-13 00:19
 * @Description: Druid 连接数据库工具类
 */
public class JDBCDruidUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDBCDruidUtils.class);


    private static Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    private static final DruidDataSource DRUID_DATA_SOURCE = new DruidDataSource();


    public static void init(){
        DRUID_DATA_SOURCE.setDriverClassName("com.mysql.jdbc.Driver");
        DRUID_DATA_SOURCE.setUrl("jdbc:mysql:///stu");
        DRUID_DATA_SOURCE.setUsername("root");
        DRUID_DATA_SOURCE.setPassword("123456");
    }


    // 获取数据源
    public static DataSource getDataSource() {
        return DRUID_DATA_SOURCE;
    }

    public static Connection getConnection() {
        try {
            connection = DRUID_DATA_SOURCE.getConnection();
        } catch (SQLException e) {
            LOGGER.error("", e);
        }
        return connection;
    }

    public static void close(ResultSet resultSet, Statement statement, Connection connection) {
        try {
            if(resultSet != null) {
                resultSet.close();
            }
            if(statement != null) {
                statement.close();
            }
            if(connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error("", e);
        }
    }


    // public static void get(){
    //     Connection connection = getConnection();
    //     ResultSet resultSet =null;
    //
    //     PreparedStatement preparedStatement = connection.prepareStatement("");
    //     resultSet = preparedStatement.executeQuery();
    //     resultSet.getInt("");
    //
    // }

}
