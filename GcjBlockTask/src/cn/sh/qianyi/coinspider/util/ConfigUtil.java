package cn.sh.qianyi.coinspider.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Description:
 * @author: ydd  
 * @date: 2018�?10�?25�? 下午5:47:56 
 */
public class ConfigUtil {
	
	/**
	 * 获取文件配置信息
	 */
	public static String getConfig(String key) {
		// 通过反射机制获取访问config.properties文件
		InputStream is = ConfigUtil.class.getResourceAsStream("/config.properties");
		Properties prop = new Properties();
		// 加载db.properties文件
		try {
			prop.load(is);
			return prop.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
