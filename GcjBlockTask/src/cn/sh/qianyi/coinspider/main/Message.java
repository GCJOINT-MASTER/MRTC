package cn.sh.qianyi.coinspider.main;

import cn.sh.qianyi.coinspider.util.MessageUtil;

public class Message {


	public static void main(String[] args) {
		
		String phone = args[0];
   		String phoneNumber = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
       	String content = "【草田签】尊敬的"+phoneNumber+"用户，您的ma'm为:123"; 
       	System.out.println("");
       	String sendResult = MessageUtil.sendMessage(phone, content);
   		System.out.println(sendResult);
   	}
	
}
