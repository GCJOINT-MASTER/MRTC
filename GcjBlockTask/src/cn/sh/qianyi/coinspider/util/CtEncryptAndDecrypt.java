package cn.sh.qianyi.coinspider.util;

import cn.sh.qianyi.coinspider.main.HttpClient;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static cn.sh.qianyi.coinspider.util.SysConstants.GCJ_PUBLIC_SERVER;

/**
 * Description: 草田签加密
 * @author: corwin
 * @date: 2019年9月1日 下午4:44:22
 */
public class CtEncryptAndDecrypt {

	static String api = GCJ_PUBLIC_SERVER;

	/**
	 * 生成随机密码
	 * @return String
	 */
	public static String random() {
		// 请求地址
		final String postUrl = api + "/getRandom";
		// 请求参数
		Map<String, Object> params = new HashMap<String, Object>();
		// POST请求
		return doPost(params, null, postUrl);
	}

	/**
	 * ECC加密
	 * @param context 加密内容
	 * @param publicKey 随机密码
	 * @return String
	 */
	public static String encryptWithPublicKey(String context, String publicKey) {
		// 请求地址
		final String postUrl = api + "/getEncryptPubkey";
		// 请求参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("msg", context);
		params.put("publicKey", publicKey);
		// POST请求
		return doPost(params, null, postUrl);
	}

	/**
	 * ECC 解密
	 * @param context 解密内容
	 * @param password 私钥
	 * @return String
	 */
	public static String decryptWithPrivateKey(String context, String password) {
		// 请求地址
		final String postUrl = api + "/decryptWithPrivateKey";
		// 请求参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("context", context);
		params.put("password", password);
		// POST请求
		return doPost(params, null, postUrl);
	}

	/**
	 * AES加密
	 * @param context 加密内容
	 * @param password 随机密码
	 * @return String
	 */
	public static String cryptoAes(String context, String password) {
		// 请求地址
		final String postUrl = api + "/cryptoAes";
		// 请求参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("context", context);
		params.put("password", password);
		// POST请求
		return doPost(params, null, postUrl);
	}

	/**
	 * AES解密
	 * @param context 解密内容
	 * @param password 随机密码
	 * @return String
	 */
	public static String decryptAes(String context, String password) {
		// 请求地址
		final String postUrl = api + "/decryptAes";
		// 请求参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("context", context);
		params.put("password", password);
		// POST请求
		return doPost(params, null, postUrl);
	}

	/**
	 * 生成Hash
	 * @param context 加密内容
	 * @return String
	 */
	public static String hash(String context) {
		// 请求地址
		final String postUrl = api + "/getHash";
		// 请求参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("context", context);
		// POST请求
		return doPost(params, null, postUrl);
	}

	/**
	 * ECDSA数字签名
	 * @param context 签名内容
	 * @param password 随机密码
	 * @return String
	 */
	public static String digest(String context, String password) {
		// 请求地址
		final String postUrl = api + "/getDigest";
		// 请求参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("context", context);
		params.put("password", password);
		// POST请求
		return doPost(params, null, postUrl);
	}



	public static String doPost(Map<String, Object> map, Map<String, Object> headerMap, String url){
		// POST请求
		String postStr = HttpClient.doPost(map,null, url);
		if (StringUtils.isBlank(postStr)) {
			return "";
		}
		// 转JSON
		JSONObject parseObject = JSONObject.parseObject(postStr);
		if (Objects.isNull(parseObject)) {
			return "";
		}
		// 查询加密状态码
		int errCode = parseObject.getIntValue("err_code");
		// 加密失败
		if (Objects.isNull(errCode) || errCode == 1) {
			return "";
		}
		// 加密成功
		if (Objects.nonNull(errCode) && errCode == 0) {
			// 查询加密结果
			String encryData = parseObject.getString("data");
			return StringUtils.isBlank(encryData) ? "" : encryData;
		}
		return "";
	}

	/**
	 * 获取密码
	 */
	public static String getPassword(Object... params) {
		if (Objects.isNull(params)) {
			return "";
		}
		try {
			JSONArray paramArray = JSONObject.parseArray(JSONObject.toJSONString(params));
			String randomPass = paramArray.getString(0);
			String publicKey = paramArray.getString(1);
			String password = encryptWithPublicKey(randomPass, publicKey);
			return StringUtils.trimToEmpty(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void main(String[] args) {
//		String ret = random();
//		System.out.println(ret);
		String publicPassword = "3733c91607b6a03cbf83133a010afc36036f1e68d421dada90fde97ac0433aaab1d97bad74e553a9af2173355a6a241ac815d2093bddf2337c0d2dcbe5983e7f12e4536630e26493b4bda7ce84560bf12ed717d77a723d1bf6be932f9ff4deb7080aba4d2bfb86145ef83005d42470012df1648e4fd0b8dd21a22a9691432cd920c21c2c0e50e9e10f70830c7f565ff832b2b89b2e290b7107e2f13fce2b420aed";
		String privateKey = "7d665aa34883a0e69539c6a91d8921b9e2ada3124969c43339565c02feabd97b";
		String randomPass = CtEncryptAndDecrypt.decryptWithPrivateKey(publicPassword, privateKey);
		System.out.println(randomPass);
		encryptWithPublicKey("a4174240f8c1fa73c6237118f7f8045b0b78724783d9de3d500b98d721d1ca09","e32b22989638f769a06a933c5f778776dcc9326729c7ecd2253cab59c97c4a6e95237b39107c0d7852c69cfebb0b98320b9755ce51deac74fd7db5990ea3e94b");
	}
}
