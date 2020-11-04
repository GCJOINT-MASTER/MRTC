package cn.sh.qianyi.coinspider.task;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sh.qianyi.coinspider.email.EmailInfo;
import cn.sh.qianyi.coinspider.email.EmailUtil;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.sh.qianyi.coinspider.bean.Certification;
import cn.sh.qianyi.coinspider.httpclient.SQLConnection;
import cn.sh.qianyi.coinspider.main.CertificationGcj3j;
import cn.sh.qianyi.coinspider.main.HttpClient;
import cn.sh.qianyi.coinspider.util.Json;
import cn.sh.qianyi.coinspider.util.MessageUtil;
import cn.sh.qianyi.coinspider.util.SendSms;

import static cn.sh.qianyi.coinspider.util.SysConstants.GCJ_PUBLIC_SERVER;

public class CompanyCertificationTask {
    static Logger log;

    static {
        CompanyCertificationTask.log = Logger.getLogger(CompanyCertificationTask.class);
    }

    public static void start() {

        final SQLConnection connection = new SQLConnection();
        while (true) {
            List<Certification> certifications = connection.getCompanyCertifications();
            int i = 0;
            for (Certification certification : certifications) {
                try {
                    if (i == certifications.size()) {
                        break;
                    }
                    if (i == certifications.size() - 1) {
                        System.out.println(1);
                    }
                    i++;
                    Thread.sleep(500);
                    String name = certification.getRealName().substring(0, 1);
                    String nameKey = certification.getRealName().substring(1, 3); // 只能加密五个字
                    String name1 = certification.getRealName().substring(3, certification.getRealName().length());
                    String number = certification.getNumber();
                    String publicKey = certification.getPublicKey();
                    String address = certification.getWalletAddress();

                    // 姓名取得需要脱敏字符
                    /*
                     * Map<String, Object> map =
                     * EncryptWithPubKey.getNameVal(name); String name1 =
                     * name.substring(new
                     * Integer(map.get("index").t88oString()), name.length());
                     */
                    Map<String, Object> certificationMap = new HashMap<String, Object>();
                    certificationMap.put("publicKey", publicKey);
                    certificationMap.put("msg", nameKey);

                    Map<String, Object> headerMap = new HashMap<String, Object>();
                    String result = HttpClient.doPostJson(certificationMap, headerMap,
                            GCJ_PUBLIC_SERVER + "/getEncryptPubkey");
                    // String encrypt = EncryptWithPubKey.encrypt(content,
                    // certification.getPublicKey());
                    String content;
                    String number1;
                    String number2;
                    String mobileT = certification.getMobile();

                    String key = JSONObject.parseObject(result).getString("data");
                    // 证件号取得需要脱敏字符
                    if (mobileT.contains("@") || certification.getNumber().length() < 18) {
                        content = number.substring(2, number.length() - 2);
                        number1 = number.substring(0, 2);
                        number2 = number.substring(number.length() - 2, number.length());
                    } else {

                        content = number.substring(4, 14);
                        number1 = number.substring(0, 4);
                        number2 = number.substring(14, number.length());

                    }

                    certificationMap.put("msg", content);
                    result = HttpClient.doPostJson(certificationMap, headerMap,
                            GCJ_PUBLIC_SERVER + "/getEncryptPubkey");
                    // String encrypt = EncryptWithPubKey.encrypt(content,
                    // certification.getPublicKey());

                    String encrypt = JSONObject.parseObject(result).getString("data");
                    // 姓名
                    String encryptName = name + "&" + key + "&" + name1;

                    // 证件号
                    String encryptNumber = number1 + "&" + encrypt + "&" + number2;

                    certificationMap.put("msg", certification.getMobile());
                    result = HttpClient.doPostJson(certificationMap, headerMap, GCJ_PUBLIC_SERVER + "/getEncryptPubkey");
                    //String encrypt = EncryptWithPubKey.encrypt(content, certification.getPublicKey());
                    if (JSONObject.parseObject(result).getInteger("err_code") != 0) {
                        continue;
                    }

                    //手机
                    String mobile = JSONObject.parseObject(result).getString("data");


                    // 实名信息上链
                    Json json = CertificationGcj3j.handleCertification(address, encryptName, encryptNumber,
                            new BigInteger("2"), mobile);
                    System.out.println(json.toString());

                    //	====TODO  =========这里待测试=====	TODO 这里将 之前预创建的合同全部改为正常合同
                    AuthAfterProcessPreContract authAfterProcessPreContract = new AuthAfterProcessPreContract();
                    authAfterProcessPreContract.processContract(certification.getId(), certification.getWalletAddress(), certification.getPublicKey());
                    //===TODO=======================================
                    if (json.isFlag()) {
                        certification.setStatus(1);

                        //区块链转帐
                        BigInteger amount = new BigInteger(connection.getDictByKey("CO_CA_REWARD"));
                        Json reward = null;
                        try {
                            //公司不需要管哪个渠道  只要实名就送
                            int insertReward = connection.insertReward(certification.getRealName(), number, certification.getWalletAddress(), amount);
                            if (insertReward == 1) {
                                reward = CertificationGcj3j.reward(certification.getWalletAddress(), amount);
                                if (reward.isFlag()) {
                                    System.out.println("奖励成功;" + certification.getWalletAddress());
                                    certification.setIsReward(1);
                                }

                            } else {
                                certification.setIsReward(2);
                            }


                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        connection.updateCertification(certification);
                        System.out.println("pass : " + certification.getRealName());
                        //String msg = "【草田签】您好！"+certification.getRealName()+"，您申请的企业认证已审核通过，您可以登录草田签机构端: https://orgweb.ctsign.cn 进行查看，感谢您的支持与配合。 ";
                        //String sendMessage = MessageUtil.sendMessage(certification.getMobile(), msg);
                        if (certification.getMobile().contains("@")) {
//                            String content1 = "【草田签】您好！" + certification.getRealName() + "，您申请的企业认证已审核通过，您可以登录草田签Dapp: https://ctsign.cn 进行查看，感谢您的支持与配合。 ";
                            String content1 = "您好！" + certification.getRealName() + "，您申请的企业认证已审核通过，您可以登录草田签Dapp: https://ctsign.cn 进行查看，感谢您的支持与配合。";

                            EmailInfo info = new EmailInfo(mobile, "草田签提醒您...", content1);
                            EmailUtil.sendHtmlMail(info);
                        } else {
                            SendSms.sendSms(certification.getMobile(), SendSms.CA_SUCCESS, "{name:'" + certification.getRealName() + "'}");
                        }

                        continue;
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("Did not pass : " + certification.getRealName());
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        start();
    }
}
