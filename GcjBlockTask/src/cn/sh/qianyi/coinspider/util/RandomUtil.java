package cn.sh.qianyi.coinspider.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Description:当前时间戳+随机字符串
 * @author: ydd  
 * @date: 2018年8月5日 下午7:54:55 
 */
public class RandomUtil {
	
	public static String getUuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static String getCurrTime(String datePattern) {
		return new SimpleDateFormat(datePattern).format(new Date());
	}

	public static String getRandomString(int length) {
		Random random = new Random();

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; ++i) {
			int number = random.nextInt(3);
			long result = 0;
			switch (number) {
			case 0:
				result = Math.round(Math.random() * 25 + 65);
				sb.append(String.valueOf((char) result));
				break;
			case 1:
				result = Math.round(Math.random() * 25 + 97);
				sb.append(String.valueOf((char) result));
				break;
			case 2:
				sb.append(String.valueOf(new Random().nextInt(10)));
				break;
			}
		}
		return sb.toString();
	}
	
	public static String getDigitString(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; ++i) {
			sb.append(String.valueOf(new Random().nextInt(10)));
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(getDigitString(5));
	}
	
}
