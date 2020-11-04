package cn.sh.qianyi.coinspider.task;

import cn.sh.qianyi.coinspider.bean.CodePwd;
import cn.sh.qianyi.coinspider.bean.ContractCertification;
import cn.sh.qianyi.coinspider.httpclient.SQLConnection;
import cn.sh.qianyi.coinspider.util.*;
import org.apache.log4j.Logger;
import org.gcj3j.protocol.Gcj3j;
import org.gcj3j.protocol.http.HttpService;

import java.util.List;

import static cn.sh.qianyi.coinspider.util.Aes.aesDecrypt;
import static cn.sh.qianyi.coinspider.util.Gcj3jUtils.myNetword;

/**
 * 实名后将预创建合同改为正常合同   ，这里可以在每次上链成功后 调用，  不需要定时来启动调用。
 */
public class AuthAfterProcessPreContract {
    static Logger log;
    //节点
    static Gcj3j gcj3j = Gcj3j.build(new HttpService(myNetword));

    static {
        AuthAfterProcessPreContract.log = Logger.getLogger(AuthAfterProcessPreContract.class);
    }

    /**
     * 机构或者个人实名后主动调用的函数
     *
     * @param userId
     */
    public void processContract(Long userId, String unAuthAddr, String pubK) {

        //根据用户 id 去查询所有的预创建合同
        final SQLConnection connection = new SQLConnection();
        List<ContractCertification> certifications = connection.getContractCertificationsByCertificationId(userId);

        //        System.out.println(JSONObject.toJSONString(certifications));
        // 循环对当前 用户下的每一封合同 对其修改将预创建 改为创建
        for (ContractCertification contractCertification : certifications) {
            String contractAddress = contractCertification.getAddress();
            //            首先判断合约的版本，只有1.1版本的 才是 需要处理的合约
            try {
                if (Gcj3jUtils.version(contractAddress) == 1.1) {
//                    获取 用户名   证件号码
                    List<String> contractInfo = Gcj3jUtils.getContractInfo(gcj3j, contractAddress);
                    String[] split = contractInfo.get(2).split("#");
                    for (String str : split) {

                        if (str.split("&")[5].equals("public")) {
                            String name = str.split("&")[0];
                            String number = str.split("&")[1];
//                            获取加密后的密码

                            System.out.println(name);
                            System.out.println(number);
                            List<CodePwd> codePwds = connection.getCodePwdsByContractAddressAndMsgIsNotNull(contractAddress);
                            for (CodePwd codePwdTemp : codePwds) {

                                String privateKey = codePwdTemp.getMsg();
                                privateKey = MD5Util.string2MD5(privateKey);
                                String encrypt = codePwdTemp.getCode();
                                String decrypt = aesDecrypt(encrypt, privateKey);
                                System.out.println("解密后：" + decrypt);

//                              解密后的初始密码
                                String password = decrypt;
                                password = CtEncryptAndDecrypt.encryptWithPublicKey(password, pubK);
//                              更新只能合约
                                Json json = Gcj3jUtils.sign(name, number, unAuthAddr, contractAddress, password);

                                if (json.isFlag()) {
//                                    如果发送合约交易成功， 则会更新数据库。
                                    contractCertification.setPwd(password);
                                    contractCertification.setUpdateTime(DateUtils.stampToDate(DateUtils.getCurrentTime()));
                                    connection.updateContractCertification(contractCertification);
                                }
                            }
                        } else {
//                            有可能是智能合约已经更新，但是数据库没有更新。

                            String pwd = Gcj3jUtils.getPwdByAddress(gcj3j, unAuthAddr, contractAddress);
                            if (pwd.length() != 0) {
                                contractCertification.setPwd(pwd);
                                contractCertification.setUpdateTime(DateUtils.stampToDate(DateUtils.getCurrentTime()));
                                connection.updateContractCertification(contractCertification);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new AuthAfterProcessPreContract().processContract(1724L, "0xce20B3D6C069c0CC9837f0Db93Ad7cD197E3423", "bc416fcdac85af94933e0d650b2bcc351a953c3df715ce00821c4b3260109722b83032d55d814e95474ea4e2771cfef88892053de6051b70a1c78044135734");
    }


}
