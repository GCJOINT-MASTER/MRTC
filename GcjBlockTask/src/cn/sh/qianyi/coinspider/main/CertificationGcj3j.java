package cn.sh.qianyi.coinspider.main;

import java.io.IOException;
import java.math.BigInteger;

import cn.sh.qianyi.coinspider.util.SysConstants;
import org.gcj3j.crypto.Credentials;
import org.gcj3j.crypto.ECKeyPair;
import org.gcj3j.crypto.RawTransaction;
import org.gcj3j.crypto.TransactionEncoder;
import org.gcj3j.protocol.Gcj3j;
import org.gcj3j.protocol.core.methods.response.EthSendTransaction;
import org.gcj3j.protocol.http.HttpService;
import org.gcj3j.utils.Numeric;

import cn.sh.qianyi.coinspider.bean.Address;
import cn.sh.qianyi.coinspider.util.Json;

import static cn.sh.qianyi.coinspider.util.Gcj3jUtils.myNetword;

public class CertificationGcj3j {
    //节点
    static Gcj3j gcj3j = Gcj3j.build(new HttpService(myNetword));
    //static Gcj3j gcj3j = Gcj3j.build(new HttpService(""));
    //实名私钥
//    prod
    public static String privateKey = "";
    //执行交易 固定gasPrice
    static BigInteger gasPrice = new BigInteger("20000000000");
    //执行交易 固定gasLimit
    static BigInteger gasLimit = new BigInteger("50000000");
    //链ID
    static byte chainId = SysConstants.GCJ_CHAIN_ID;

    public static Address systemAddress = null;

    static {
        systemAddress = new Address(gcj3j, Credentials.create(privateKey).getAddress(), privateKey);
    }

    public static void main(final String[] args) throws Exception {
        //handleCertification("0x6DD9CE0D2bD9b84aD8cba11c8050D679B88c1b27", "张三", "430502188821011210", new BigInteger("1"));
        //System.out.println("".length());


		
		/*//转账充值地址
		List<Address> chargeAddrs = new ArrayList<Address>();
		Address chargeAddress1 = new Address(gcj3j,"0xdc9653466355cb99772d3219f7c81d309c7a93c3","");
		Address chargeAddress2 = new Address(gcj3j,"0xc337c1dcbbb5e48831699ffec2ad4e287865f847","");
		chargeAddrs.add(chargeAddress1);
		chargeAddrs.add(chargeAddress2);
		
		String[] toAddrs = {"0x84ab0fb944302b955c6876553cfdc78d311f0b27",
				"0x879ac4fc599ebe3061a1de450eacd1cac8393c43",
				"0x23754bc25e47384cb0e70279560d45e6d12992ec",
				"0xd29ce8f3a1ac0ce84741c5b7a3e94f44ddef2fee",
				"0x17405f667f747ec7c30f9ad660d5bd8ca480d0ec",
				"0xba7b0313693ebc05b5076885aeffd0caedfac1db",
		};
		
		
		BigInteger  amount = new BigDecimal("0.1").multiply(BigDecimal.TEN.pow(decimal)).toBigInteger();
		try {
			for (int i = 0; i < chargeAddrs.length; i++) {
				for (int i = 0; i < toAddrs.length; i++) {
					Json sendMoney = sendMoney(chargeAddr,amount, toAddrs[i]);
					System.out.println(sendMoney.isFlag());
					System.out.println(sendMoney.getMsg());
				}
			}
				
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

    }

    public static Json reward(String toAddress, BigInteger amount) throws Exception {
        Json sendMoney = sendMoney(systemAddress, amount, toAddress);
        return sendMoney;
    }

    /**
     * handleCertification 实名认证信息上链
     *
     * @param caAddress 实名地址
     * @param name      真实名称
     * @param number    证件号
     * @param type      实名类型 1.个人 2.企业
     * @return 返回值
     */
    public static Json handleCertification(String caAddress, String name,
                                           String number, BigInteger type, String mobile) throws Exception {
        Json json = new Json();
        BigInteger value = BigInteger.TEN;
        System.out.println("caAddress " + caAddress + "  nonce:" + systemAddress.getNonce());
        String signedData = signCertificationTransaction(systemAddress.getNonce(), gasPrice, gasLimit, caAddress, value, "", chainId,
                systemAddress.getPrivateKey(), mobile, caAddress, name, number, type);
        if (signedData != null) {

            EthSendTransaction ethSendTransaction = gcj3j.ethSendRawTransaction(signedData).send();
            if (ethSendTransaction.getTransactionHash() != null) {
                json.setFlag(true);
                json.setMsg(ethSendTransaction.getTransactionHash());
                systemAddress.setNonce(systemAddress.getNonce().add(new BigInteger("1")));
            } else {
                json.setFlag(false);
                if (ethSendTransaction.getError().getMessage().contains("nonce")) {
                    systemAddress.setNonce(systemAddress.getNonce().add(new BigInteger("1")));
                }
                json.setMsg(ethSendTransaction.getError().getMessage());
            }
        }
        return json;
    }

    // 实名认证签名
    public static String signCertificationTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit,
                                                      String to, BigInteger value, String data, byte chainId, String privateKey, String mobile, String address, String name,
                                                      String number, BigInteger type) throws IOException {
        RawTransaction rawTransaction = RawTransaction.createCertificationTransaction(BigInteger.ZERO, nonce, gasPrice,
                gasLimit, to, value, data, address, mobile, number, name, type);
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Credentials credentials = Credentials.create(ecKeyPair);
        byte[] signedMessage;
        signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);

        String hexValue = Numeric.toHexString(signedMessage);
        return hexValue;
    }

    /**
     * sendMoney 转帐 （充值后用系统地址向充值地址转账）
     *
     * @param systemAddress 发送者私钥
     * @param amount     充值金额
     * @param toAddress  充值地址
     * @return 返回值
     */
    public static Json sendMoney(Address systemAddress, BigInteger amount, String toAddress) throws Exception {
        Json json = new Json();
		/*Credentials credentials = Credentials.create(systemAddress.getPrivateKey());
		String fromAddress = credentials.getAddress();
		BigInteger nonce;
		EthGetTransactionCount ethGetTransactionCount = gcj3j
				.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send();
		nonce = ethGetTransactionCount.getTransactionCount();*/

        String signedData = signTransaction(systemAddress.getNonce(), gasPrice, gasLimit, toAddress, amount, "", chainId, systemAddress.getPrivateKey());
        if (signedData != null) {
            EthSendTransaction ethSendTransaction = gcj3j.ethSendRawTransaction(signedData).send();
            if (ethSendTransaction.getTransactionHash() != null) {
                json.setFlag(true);
                json.setMsg(ethSendTransaction.getTransactionHash());
                //每发送一笔交易就将当前系统地址的nonce+1
                systemAddress.setNonce(systemAddress.getNonce().add(new BigInteger("1")));
            } else {
                json.setFlag(false);
                if (ethSendTransaction.getError().getMessage().contains("nonce")) {
                    systemAddress.setNonce(systemAddress.getNonce().add(new BigInteger("1")));
                }
                json.setMsg(ethSendTransaction.getError().getMessage());
            }
        }
        return json;
    }

    // 转账签名
    public static String signTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
                                         BigInteger value, String data, byte chainId, String privateKey) throws IOException {
        RawTransaction rawTransaction = RawTransaction.createTransaction(BigInteger.ZERO, nonce, gasPrice, gasLimit, to,
                value, data);
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Credentials credentials = Credentials.create(ecKeyPair);
        byte[] signedMessage;
        signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);

        String hexValue = Numeric.toHexString(signedMessage);
        return hexValue;
    }

}
