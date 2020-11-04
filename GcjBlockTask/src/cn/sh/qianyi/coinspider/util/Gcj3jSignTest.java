package cn.sh.qianyi.coinspider.util;

import cn.sh.qianyi.coinspider.smartconstarct.QYContract;
import com.alibaba.fastjson.JSONObject;
import org.gcj3j.crypto.Credentials;
import org.gcj3j.crypto.ECKeyPair;
import org.gcj3j.crypto.RawTransaction;
import org.gcj3j.crypto.TransactionEncoder;
import org.gcj3j.protocol.Gcj3j;
import org.gcj3j.protocol.core.DefaultBlockParameterName;
import org.gcj3j.protocol.core.methods.response.EthGasPrice;
import org.gcj3j.protocol.core.methods.response.EthGetBalance;
import org.gcj3j.protocol.core.methods.response.EthGetTransactionCount;
import org.gcj3j.protocol.core.methods.response.EthSendTransaction;
import org.gcj3j.protocol.http.HttpService;
import org.gcj3j.tx.Transfer;
import org.gcj3j.utils.Convert;
import org.gcj3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;


public class Gcj3jSignTest {
    private static Gcj3j gcj3j = Gcj3j.build(new HttpService(SysConstants.GCJ_NETWORK));
    //链ID
    static byte chainId = SysConstants.GCJ_CHAIN_ID;

    public static void main(String[] args) throws Exception {
//        deployContract(privateKey);
//        sendMoney(privateKey, BigInteger.ONE, "");
//        System.out.println(99 << 1);
//        EthGetTransactionCount ethGetTransactionCount = gcj3j
//                .ethGetTransactionCount("0x73003c64F0e673cAeF9f21Fa0E486B2692bD3Fa1", DefaultBlockParameterName.PENDING).send();

        System.out.println(JSONObject.toJSONString(getBalanceOf("0xF2AFd407722d8359C4f6C9F6F78b5ae4861650d9")));


    }

    public static String getBalanceOf(String walletAddress) {
        BigDecimal fromWei = new BigDecimal("0.00");
        try {
            EthGetBalance ethGetBalance = /* 102:130 */ (EthGetBalance) gcj3j
                    .ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
            BigInteger balance = ethGetBalance.getBalance();
//            System.out.println(balance);
            fromWei = Convert.fromWei(balance.toString(), Convert.Unit.ETHER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fromWei.toString();
    }

    @SuppressWarnings({"rawtypes", "deprecation"})
    //    动态添加验证人
    public static String handleCertTransfer(String privateKey, String storageAddr, int chainId) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        String fromAddress = credentials.getAddress();
        BigInteger nonce;
        EthGetTransactionCount ethGetTransactionCount = gcj3j
                .ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send();
        if (ethGetTransactionCount == null)
            return "";
//        nonce = ethGetTransactionCount.getTransactionCount();
        nonce = ethGetTransactionCount.getTransactionCount();
//        nonce = ethGetTransactionCount.getTransactionCount();
        BigInteger gasPrice = Transfer.GAS_PRICE;
        BigInteger gasLimit = new BigInteger("90000");
        BigInteger value = BigInteger.ZERO;

        EthGasPrice ethGasPrice = gcj3j.ethGasPrice().send();
        gasPrice = ethGasPrice.getGasPrice();
        String signedData = signCertTransaction(BigInteger.ONE, nonce, gasPrice, gasLimit, fromAddress, value, "", (byte) chainId,
                privateKey, storageAddr);
        System.out.println(signedData);
        System.out.println(signedData.length());
        System.exit(0);
        if (signedData != null) {

            EthSendTransaction ethSendTransaction = gcj3j.ethSendRawTransaction(signedData).send();
            System.out.println(JSONObject.toJSONString(ethSendTransaction));
            return ethSendTransaction.getTransactionHash();
        } else {
            return "";
        }
    }


    public static String signCertTransaction(BigInteger type, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
                                             BigInteger value, String data, byte chainId, String privateKey, String storageAddr) throws IOException {
//       1 我想成为候选恶人
//       2 取消选恶人
        //        storageAddress =

//        3 投票
//        4 取消投票
        //        to

//        BigInteger type = BigInteger.ZERO;
        RawTransaction rawTransaction = RawTransaction.createCertificationTransaction(type, nonce, gasPrice, gasLimit, to,
                value, data, storageAddr, "", "", "", BigInteger.ZERO);
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
        Credentials credentials = Credentials.create(ecKeyPair);
        byte[] signedMessage;
        // if (chainId > -1) {
        signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        /*
         * } else { signedMessage =
         * TransactionEncoder.signMessage(rawTransaction, credentials); }
         */
        String hexValue = Numeric.toHexString(signedMessage);
        return hexValue;
    }

    /**
     * sendMoney 转帐 （充值后用系统地址向充值地址转账）
     *
     * @param privateKey 发送者私钥
     * @param amount     充值金额
     * @param toAddress  充值地址
     * @return 返回值
     */

    public static Json sendMoney(String privateKey, BigInteger amount, String toAddress) throws Exception {
        //执行交易 固定gasPrice
        BigInteger gasPrice = new BigInteger("20000000000");
        //执行交易 固定gasLimit
        BigInteger gasLimit = new BigInteger("50000000");

        Json json = new Json();
        Credentials credentials = Credentials.create(privateKey);
        String fromAddress = credentials.getAddress();
        BigInteger nonce;
        EthGetTransactionCount ethGetTransactionCount = gcj3j
                .ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send();
        nonce = ethGetTransactionCount.getTransactionCount();

        String signedData = signTransaction(nonce, gasPrice, gasLimit, toAddress, amount, "", chainId, privateKey);
        if (signedData != null) {
            EthSendTransaction ethSendTransaction = gcj3j.ethSendRawTransaction(signedData).send();
            System.out.println(JSONObject.toJSONString(ethSendTransaction));
            if (ethSendTransaction.getTransactionHash() != null) {
                json.setFlag(true);
                json.setMsg(ethSendTransaction.getTransactionHash());
            } else {
                json.setFlag(false);
                if (ethSendTransaction.getError().getMessage().contains("nonce")) {
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

    public static void deployContract(String private_key) throws Exception {

        //转账人私钥
        Credentials credentials = Credentials.create(private_key);
        EthGasPrice ethGasPrice = gcj3j.ethGasPrice().send();
//        BigInteger gasPrice = BigInteger.valueOf(2100000);
        BigInteger gasPrice = ethGasPrice.getGasPrice().multiply(new BigInteger("105")).divide(new BigInteger("100"));
        System.out.println(JSONObject.toJSONString(gasPrice));
        //gasLimit
        BigInteger gasLimit = BigInteger.valueOf(3000000);
        //部署合约
        QYContract contract = QYContract.deploy(gcj3j, credentials, gasPrice, gasLimit).send();
        System.out.println(JSONObject.toJSONString(contract));
        String contractAddress = contract.getContractAddress();
        System.out.println("合约地址：" + contractAddress);
        System.out.println("合约是否有效" + contract.isValid());
    }
}
