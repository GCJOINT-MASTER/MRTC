package cn.sh.qianyi.coinspider.util;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
/*
pom.xml
*/
public class SendSms {
//	public static String CA_SUCCESS = "SMS_187270843";
	public static String CA_SUCCESS = "SMS_199585216";
//	public static String CREATE_CONTRACT = "SMS_187270838";
	public static String CREATE_CONTRACT = "SMS_199580228";
//	public static String CO_CREATE_CONTRACT = "SMS_187270837";
	public static String CO_CREATE_CONTRACT = "SMS_199600213";

    public static void sendSms(String mobile,String TemplateCode,String TemplateParam) {
		String accessKeyId = "";
		String accessSecret = "";

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setAction("SendSms");
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        /*request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com"); //写死的
        request.setSysVersion("2017-05-25"); //写死的
        request.setSysAction("SendSms");  //写死的
*/        request.putQueryParameter("RegionId", "cn-hangzhou");  //写死的
        request.putQueryParameter("SignName", "草田签");//写死的
        request.putQueryParameter("PhoneNumbers", mobile); //参数，接受者手机号码
        request.putQueryParameter("TemplateCode", TemplateCode);//参数,短信模板ID,见上文
        request.putQueryParameter("TemplateParam", TemplateParam);//参数，json格式，比如短信内容中带有${name}就这样写      
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
    	String name = "";
    	SendSms.sendSms("", SendSms.CA_SUCCESS, "{name:'"+name+"'}");
	}
}

