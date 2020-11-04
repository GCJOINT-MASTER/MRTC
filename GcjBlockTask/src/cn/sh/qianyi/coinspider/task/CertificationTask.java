package cn.sh.qianyi.coinspider.task;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.faceid.v20180301.FaceidClient;
import com.tencentcloudapi.faceid.v20180301.models.GetDetectInfoRequest;
import com.tencentcloudapi.faceid.v20180301.models.GetDetectInfoResponse;

import cn.sh.qianyi.coinspider.bean.Certification;
import cn.sh.qianyi.coinspider.httpclient.SQLConnection;
import cn.sh.qianyi.coinspider.main.CertificationGcj3j;
import cn.sh.qianyi.coinspider.main.EncryptWithPubKey;
import cn.sh.qianyi.coinspider.main.HttpClient;
import cn.sh.qianyi.coinspider.util.Json;
import cn.sh.qianyi.coinspider.util.TxConfig;

import static cn.sh.qianyi.coinspider.util.SysConstants.GCJ_PUBLIC_SERVER;

public class CertificationTask {

    static FaceidClient client = null;

    static SQLConnection connection = new SQLConnection();

    static {
        Credential cred = new Credential(TxConfig.secretId, TxConfig.secretKey);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(TxConfig.faceEndpoint);

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        client = new FaceidClient(cred, "ap-shanghai", clientProfile);
    }


    public static void start() {


        while (true) {
            try {
                List<Certification> certifications = connection.getCertifications();
                int i = 0;
                for (Certification certification : certifications) {
                    if (i == certifications.size()) {
                        break;
                    }
                    if (certification.getRealName().length() < 2) {
                        break;
                    }
                    i++;
                    Thread.sleep(500);

                    if (certification.getStatus() == 2) {
                        api(certification);
                    } else if (certification.getStatus() == 0) {
                        tencent(certification);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }

        }
    }

    private static void tencent(Certification certification) throws Exception {
        if (StringUtils.isEmpty(certification.getBizToken())) {
            return;
        }
        String params = "{\"BizToken\":\"" + certification.getBizToken() + "\",\"InfoType\":\"1\",\"RuleId\":\"1\"}";
        GetDetectInfoRequest req = GetDetectInfoRequest.fromJsonString(params, GetDetectInfoRequest.class);


        JSONObject text = null;
        Integer code = 0;
        try {
            GetDetectInfoResponse resp = client.GetDetectInfo(req);
            JSONObject jsonObject = JSONObject.parseObject(GetDetectInfoRequest.toJsonString(resp));
            JSONObject detectInfo = JSONObject.parseObject(jsonObject.getString("DetectInfo"));
            text = detectInfo.getJSONObject("Text");
            code = text.getInteger("ErrCode");
            if (code == null) {
                return;
            }
        } catch (Exception e1) {
        }
        Certification c = new Certification();
        c.setId(certification.getId());

        //认证成功
        if (code == 0) {
            try {
                String name = text.getString("Name");
                String number = text.getString("IdCard");

                //姓名取得需要脱敏字符
                Map<String, Object> map = EncryptWithPubKey.getNameVal(name);
                String name1 = name.substring(new Integer(map.get("index").toString()), name.length());

                Map<String, Object> certificationMap = new HashMap<String, Object>();
                certificationMap.put("publicKey", certification.getPublicKey());
                certificationMap.put("msg", map.get("content").toString());

                Map<String, Object> headerMap = new HashMap<String, Object>();
                String result = HttpClient.doPostJson(certificationMap, headerMap, GCJ_PUBLIC_SERVER + "/getEncryptPubkey");
                //String encrypt = EncryptWithPubKey.encrypt(content, certification.getPublicKey());
                if (JSONObject.parseObject(result).getInteger("err_code") != 0) {
                    return;
                }


                String key = JSONObject.parseObject(result).getString("data");
                //证件号取得需要脱敏字符
                String content = number.substring(4, 14);
                String number1 = number.substring(0, 4);
                String number2 = number.substring(14, number.length());


                certificationMap.put("msg", content);
                result = HttpClient.doPostJson(certificationMap, headerMap, GCJ_PUBLIC_SERVER + "/getEncryptPubkey");
                //String encrypt = EncryptWithPubKey.encrypt(content, certification.getPublicKey());
                if (JSONObject.parseObject(result).getInteger("err_code") != 0) {
                    return;
                }

                String encrypt = JSONObject.parseObject(result).getString("data");
                //姓名
                String encryptName = key + "&" + name1;

                //证件号
                String encryptNumber = number1 + "&" + encrypt + "&" + number2;

                certificationMap.put("msg", certification.getMobile());
                result = HttpClient.doPostJson(certificationMap, headerMap, GCJ_PUBLIC_SERVER + "/getEncryptPubkey");
                //String encrypt = EncryptWithPubKey.encrypt(content, certification.getPublicKey());
                if (JSONObject.parseObject(result).getInteger("err_code") != 0) {
                    return;
                }

                //手机
                String mobile = JSONObject.parseObject(result).getString("data");

                //实名信息上链
                Json json = CertificationGcj3j.handleCertification(certification.getWalletAddress(), encryptName, encryptNumber, BigInteger.ONE, mobile);
                System.out.println("json : " + JSONObject.toJSONString(json));
                if (json.isFlag()) {
                    c.setRealName(name);
                    c.setNumber(number);
                    c.setStatus(1);

                    //区块链转帐
                    BigInteger amount = new BigInteger(connection.getDictByKey("CA_REWARD"));
                    Json reward = null;
                    try {

                        //草田签一个身份证只送一次
                        if (connection.queryReward(number) == null) {
                            int insertReward = connection.insertReward(name, number, certification.getWalletAddress(), amount);
                            if (insertReward == 1) {
                                reward = CertificationGcj3j.reward(certification.getWalletAddress(), amount);
                                if (reward.isFlag()) {
                                    c.setIsReward(1);
                                    System.out.println("奖励成功;" + certification.getWalletAddress());
                                }

                            }
                        } else {
                            c.setIsReward(2);
                        }


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    connection.updateCertification(c);
                    System.out.println("pass : " + certification.getRealName());


                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //实名失败
        /*certification.setStatus(2);
    	connection.updateCertification(certification);*/

        System.out.println("Did not pass : " + certification.getRealName());
    }

    private static void api(Certification certification) throws Exception {
        Certification c = new Certification();
        c.setId(certification.getId());

        //认证成功

        String name = certification.getRealName();
        String number = certification.getNumber();

        //姓名取得需要脱敏字符
        Map<String, Object> map = EncryptWithPubKey.getNameVal(name);
        String name1 = name.substring(new Integer(map.get("index").toString()), name.length());

        Map<String, Object> certificationMap = new HashMap<String, Object>();
        certificationMap.put("publicKey", certification.getPublicKey());
        certificationMap.put("msg", map.get("content").toString());

        Map<String, Object> headerMap = new HashMap<String, Object>();
        String result = HttpClient.doPostJson(certificationMap, headerMap, GCJ_PUBLIC_SERVER + "/getEncryptPubkey");
        //String encrypt = EncryptWithPubKey.encrypt(content, certification.getPublicKey());
        if (JSONObject.parseObject(result).getInteger("err_code") != 0) {
            return;
        }

        String mobileT = certification.getMobile();

        String key = JSONObject.parseObject(result).getString("data");

        String content;
        String number1;
        String number2;

        //台湾手机号的问题
        if (mobileT.contains("@") || certification.getNumber().length() < 18) {
            content = number.substring(2, number.length() - 2);
            number1 = number.substring(0, 2);
            number2 = number.substring(number.length() - 2, number.length());
        } else {

            //证件号取得需要脱敏字符
            content = number.substring(4, 14);
            number1 = number.substring(0, 4);
            number2 = number.substring(14, number.length());

        }

        certificationMap.put("msg", content);
        result = HttpClient.doPostJson(certificationMap, headerMap, GCJ_PUBLIC_SERVER + "/getEncryptPubkey");
        //String encrypt = EncryptWithPubKey.encrypt(content, certification.getPublicKey());
        if (JSONObject.parseObject(result).getInteger("err_code") != 0) {
            return;
        }

        String encrypt = JSONObject.parseObject(result).getString("data");
        //姓名
        String encryptName = key + "&" + name1;

        //证件号
        String encryptNumber = number1 + "&" + encrypt + "&" + number2;

        certificationMap.put("msg", certification.getMobile());
        result = HttpClient.doPostJson(certificationMap, headerMap, GCJ_PUBLIC_SERVER + "/getEncryptPubkey");
        //String encrypt = EncryptWithPubKey.encrypt(content, certification.getPublicKey());
        if (JSONObject.parseObject(result).getInteger("err_code") != 0) {
            return;
        }

        //手机
        String mobile = JSONObject.parseObject(result).getString("data");

        //实名信息上链
        Json json = CertificationGcj3j.handleCertification(certification.getWalletAddress(), encryptName, encryptNumber, BigInteger.ONE, mobile);
        System.out.println("json : " + JSONObject.toJSONString(json));

        //	====TODO  =========这里待测试=====	TODO 这里将 之前预创建的合同全部改为正常合同
        AuthAfterProcessPreContract authAfterProcessPreContract = new AuthAfterProcessPreContract();
        authAfterProcessPreContract.processContract(certification.getId(), certification.getWalletAddress(), certification.getPublicKey());
        //===TODO=======================================
        if (json.isFlag()) {
            c.setRealName(name);
            c.setNumber(number);
            c.setStatus(1);

            //区块链转帐
            BigInteger amount = new BigInteger(connection.getDictByKey("CA_REWARD"));
            Json reward = null;
            try {

                if (certification.getChannelId() == 4) {
                    int insertReward = connection.insertReward(name, number, certification.getWalletAddress(), amount);
                    if (insertReward == 1) {
                        reward = CertificationGcj3j.reward(certification.getWalletAddress(), amount);
                        if (reward.isFlag()) {
                            c.setIsReward(1);
                            System.out.println("奖励成功;" + certification.getWalletAddress());
                        }
                    }
                }
                //草田签一个身份证只送一次
                else if (/*connection.queryReward(number) == null &&*/ certification.getChannelId() == 1) {
                    int insertReward = connection.insertReward(name, number, certification.getWalletAddress(), amount);
                    if (insertReward == 1) {
                        reward = CertificationGcj3j.reward(certification.getWalletAddress(), amount);
                        if (reward.isFlag()) {
                            c.setIsReward(1);
                            System.out.println("奖励成功;" + certification.getWalletAddress());
                        }
                    }
                } else {
                    c.setIsReward(2);
                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            connection.updateCertification(c);
            System.out.println("pass : " + certification.getRealName());


        }


        //实名失败
        /*certification.setStatus(2);
    	connection.updateCertification(certification);*/

        System.out.println("Did not pass : " + certification.getRealName());
    }


    private static void lianxiang(Certification certification) throws Exception {

        Certification c = new Certification();
        c.setId(certification.getId());


        try {
            String name = certification.getRealName();
            String number = certification.getNumber();

            //姓名取得需要脱敏字符
            Map<String, Object> map = EncryptWithPubKey.getNameVal(name);
            String name1 = name.substring(new Integer(map.get("index").toString()), name.length());

            Map<String, Object> certificationMap = new HashMap<String, Object>();
            certificationMap.put("publicKey", certification.getPublicKey());
            certificationMap.put("msg", map.get("content").toString());

            Map<String, Object> headerMap = new HashMap<String, Object>();
            String result = HttpClient.doPostJson(certificationMap, headerMap, GCJ_PUBLIC_SERVER + "/getEncryptPubkey");
            //String encrypt = EncryptWithPubKey.encrypt(content, certification.getPublicKey());
            if (JSONObject.parseObject(result).getInteger("err_code") != 0) {
                return;
            }

            String key = JSONObject.parseObject(result).getString("data");
            //证件号取得需要脱敏字符
            String content = number.substring(4, 14);
            String number1 = number.substring(0, 4);
            String number2 = number.substring(14, number.length());


            certificationMap.put("msg", content);
            result = HttpClient.doPostJson(certificationMap, headerMap, GCJ_PUBLIC_SERVER + "/getEncryptPubkey");
            //String encrypt = EncryptWithPubKey.encrypt(content, certification.getPublicKey());
            if (JSONObject.parseObject(result).getInteger("err_code") != 0) {
                return;
            }

            String encrypt = JSONObject.parseObject(result).getString("data");
            //姓名
            String encryptName = key + "&" + name1;

            //证件号
            String encryptNumber = number1 + "&" + encrypt + "&" + number2;

            certificationMap.put("msg", certification.getMobile());
            result = HttpClient.doPostJson(certificationMap, headerMap, GCJ_PUBLIC_SERVER + "/getEncryptPubkey");
            //String encrypt = EncryptWithPubKey.encrypt(content, certification.getPublicKey());
            if (JSONObject.parseObject(result).getInteger("err_code") != 0) {
                return;
            }

            //手机
            String mobile = JSONObject.parseObject(result).getString("data");

            //实名信息上链
            Json json = CertificationGcj3j.handleCertification(certification.getWalletAddress(), encryptName, encryptNumber, BigInteger.ONE, mobile);
            System.out.println("json : " + JSONObject.toJSONString(json));
            if (json.isFlag()) {
                c.setRealName(name);
                c.setNumber(number);
                c.setStatus(1);

                //区块链转帐
                BigInteger amount = new BigInteger(connection.getDictByKey("CA_REWARD"));
                Json reward = null;
                try {
                    //链享个人 多个身份证多次送
                    int insertReward = connection.insertReward(name, number, certification.getWalletAddress(), amount);
                    if (insertReward == 1) {
                        reward = CertificationGcj3j.reward(certification.getWalletAddress(), amount);
                        if (reward.isFlag()) {
                            c.setIsReward(1);
                            System.out.println("奖励成功;" + certification.getWalletAddress());
                        }

                    } else {
                        c.setIsReward(2);
                    }


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                connection.updateCertification(c);
                System.out.println("pass : " + certification.getRealName());


            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //实名失败
        /*certification.setStatus(2);
    	connection.updateCertification(certification);*/

        System.out.println("Did not pass : " + certification.getRealName());
    }

    public static void main(String[] args) throws Exception {
        start();
    }

}
