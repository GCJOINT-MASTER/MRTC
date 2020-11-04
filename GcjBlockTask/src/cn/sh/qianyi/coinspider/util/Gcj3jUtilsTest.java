package cn.sh.qianyi.coinspider.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.gcj3j.abi.FunctionEncoder;
import org.gcj3j.abi.TypeReference;
import org.gcj3j.abi.datatypes.Address;
import org.gcj3j.abi.datatypes.Bool;
import org.gcj3j.abi.datatypes.Function;
import org.gcj3j.abi.datatypes.Type;
import org.gcj3j.abi.datatypes.generated.Uint256;
import org.gcj3j.crypto.Credentials;
import org.gcj3j.crypto.ECKeyPair;
import org.gcj3j.crypto.RawTransaction;
import org.gcj3j.crypto.TransactionEncoder;
import org.gcj3j.protocol.Gcj3j;
import org.gcj3j.protocol.core.DefaultBlockParameter;
import org.gcj3j.protocol.core.DefaultBlockParameterName;
import org.gcj3j.protocol.core.DefaultBlockParameterNumber;
import org.gcj3j.protocol.core.methods.response.*;
import org.gcj3j.protocol.http.HttpService;
import org.gcj3j.tx.ChainId;
import org.gcj3j.tx.Transfer;
import org.gcj3j.utils.Convert;
import org.gcj3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static cn.sh.qianyi.coinspider.util.Gcj3jUtils.myNetword;

/*  53:    */ public class Gcj3jUtilsTest
        /* 54: */ {
    //    /* 58: 77 */ public static String myNetword = "http://127.0.0.1:7575";
//    /* 58: 77 */ public static String myNetword = "http://127.0.0.1:7578";
    /* 59: */ private static Gcj3j gcj3j = Gcj3j.build(new HttpService(myNetword));
    ;

    public static void main(String[] args) throws Exception {

        System.out.println(getBalanceOf("0xE89708bd4806643f37FA031745e4FFb39deB33a7"));
        System.out.println(getBalanceOf("0x195894197BAD39A4e83df91c41F40144388e516B"));
        System.out.println(getBalanceOf("0xaFFB24828BE4486A64AC2e28A06851aad2369DBC"));
        System.out.println(getBalanceOf("0xdBaDF513Ba38676066A9B741cbF525287c22B14C"));
        System.out.println(getBalanceOf("0x33f19fc1b693c742CACd9a65972f6582b8Fc3e5B"));
        System.out.println(getBalanceOf("0x6fD4A91a61210C74C554438334CC01b510b0631D"));

//        鍔ㄦ�佹坊鍔犻獙璇佷汉
        String hash = handleCertTransfer("342158f80124630e6fb82177784afc86d5e7bd155288f7477f4687f7f41b36ce", "0x933daC87528aBc2319387687fEeFFbB35Ec872e8",92);
//        String hash = "0x100366c6d6eb79c5503d7fc9d3c9f8a662731bc6b7bb7631658288270adfadee";
        System.out.println(hash);
        if (hash != null) {
            // 鑾峰彇浜ゆ槗璇︽儏
            EthGetTransactionReceipt transactionReceipt = gcj3j
                    .ethGetTransactionReceipt(hash).send();
            String tradeStr = JSONObject.toJSONString(transactionReceipt);
            System.out.println(tradeStr);
        }
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
    //    鍔ㄦ�佹坊鍔犻獙璇佷汉
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
//        System.exit(0);
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
//       1 鎴戞兂鎴愪负鍊欓�夋伓浜�
//       2 鍙栨秷閫夋伓浜�
        //        storageAddress =

//        3 鎶曠エ
//        4 鍙栨秷鎶曠エ
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
}
