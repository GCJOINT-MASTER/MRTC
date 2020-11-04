package cn.sh.qianyi.coinspider.httpclient;

import java.sql.*;

import cn.sh.qianyi.coinspider.util.ConfigUtil;

public class BaseDao
{
    protected Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(ConfigUtil.getConfig("jdbc.trade.driver"));
            conn = DriverManager.getConnection(ConfigUtil.getConfig("jdbc.trade.url"), ConfigUtil.getConfig("jdbc.trade.username"),ConfigUtil.getConfig("jdbc.trade.password"));
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (SQLException e2) {
            e2.printStackTrace();
        }
        return conn;
    }
    
}
