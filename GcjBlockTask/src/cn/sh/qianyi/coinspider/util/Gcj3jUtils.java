/*   1:    */
package cn.sh.qianyi.coinspider.util;

/*   4:    */

import java.io.File;
        /*   5:    */ import java.io.IOException;
        /*   7:    */ import java.math.BigDecimal;
        /*   8:    */ import java.math.BigInteger;
        /*   9:    */ import java.util.ArrayList;
        /*  11:    */ import java.util.HashMap;
        /*  12:    */ import java.util.List;
        /*  13:    */ import java.util.Map;
import java.util.concurrent.ExecutionException;

        /*  15:    */ import cn.sh.qianyi.coinspider.main.CertificationGcj3j;
import com.alibaba.fastjson.JSONObject;
import org.gcj3j.abi.FunctionEncoder;
        /*  16:    */ import org.gcj3j.abi.FunctionReturnDecoder;
        /*  17:    */ import org.gcj3j.abi.TypeReference;
        /*  18:    */ import org.gcj3j.abi.datatypes.Address;
import org.gcj3j.abi.datatypes.Array;
        /*  19:    */ import org.gcj3j.abi.datatypes.Bool;
        /*  20:    */ import org.gcj3j.abi.datatypes.Function;
        /*  21:    */ import org.gcj3j.abi.datatypes.Type;
import org.gcj3j.abi.datatypes.Uint;
import org.gcj3j.abi.datatypes.Utf8String;
        /*  22:    */ import org.gcj3j.abi.datatypes.generated.Uint256;
        /*  23:    */ import org.gcj3j.abi.datatypes.generated.Uint8;
        /*  24:    */ import org.gcj3j.crypto.Bip39Wallet;
        /*  25:    */ import org.gcj3j.crypto.CipherException;
        /*  26:    */ import org.gcj3j.crypto.Credentials;
        /*  27:    */ import org.gcj3j.crypto.ECKeyPair;
        /*  28:    */ import org.gcj3j.crypto.Keys;
        /*  29:    */ import org.gcj3j.crypto.RawTransaction;
        /*  30:    */ import org.gcj3j.crypto.TransactionEncoder;
        /*  31:    */ import org.gcj3j.crypto.Wallet;
        /*  32:    */ import org.gcj3j.crypto.WalletFile;
        /*  33:    */ import org.gcj3j.crypto.WalletUtils;
        /*  34:    */ import org.gcj3j.protocol.ObjectMapperFactory;
        /*  35:    */ import org.gcj3j.protocol.Gcj3j;
import org.gcj3j.protocol.core.DefaultBlockParameter;
        /*  36:    */ import org.gcj3j.protocol.core.DefaultBlockParameterName;
import org.gcj3j.protocol.core.DefaultBlockParameterNumber;
        /*  39:    */ import org.gcj3j.protocol.core.methods.request.Transaction;
import org.gcj3j.protocol.core.methods.response.*;
        /*  40:    */
        /*  42:    */
        /*  43:    */
        /*  44:    */
        /*  45:    */
        /*  47:    */ import org.gcj3j.protocol.http.HttpService;
import org.gcj3j.tx.ChainId;
        /*  48:    */ import org.gcj3j.tx.Transfer;
        /*  49:    */ import org.gcj3j.utils.Convert;
        /*  51:    */ import org.gcj3j.utils.Numeric;

import com.alibaba.fastjson.JSON;
        /*   2:    */
        /*   3:    */ import com.fasterxml.jackson.databind.ObjectMapper;

import static cn.sh.qianyi.coinspider.main.CertificationGcj3j.systemAddress;
import static cn.sh.qianyi.coinspider.util.SysConstants.GCJ_NETWORK;

/*  52:    */
/*  53:    */ public class Gcj3jUtils
        /* 54: */ {
    /* 55: 69 */ public static String network = "https://mainnet.infura.io/mew";
    /* 56: 72 */ public static String kovannetwork = "https://kovan.infura.io/";
    /* 57: 75 */ public static String ropstennetwork = "https://ropsten.infura.io/";
    //    正式坏境 TODO
    /* 58: 77 */ public static String myNetword = GCJ_NETWORK;
    //    测试坏境
//    /* 58: 77 */ public static String myNetword = "http://47.111.103.44:7576";
    /* 59: */ private static Gcj3j gcj3j = Gcj3j.build(new HttpService(myNetword));
    ;
    /* 60: 80 */ private static String emptyAddress = "0x0000000000000000000000000000000000000000";

    /* 62: */
    /* 63: */

    // 节点
    // 执行交易 固定gasPrice
    static BigInteger gasPrice = new BigInteger("20000000000");
    // 执行交易 固定gasLimit
    static BigInteger gasLimit = new BigInteger("50000000");
    // 执行交易 固定gasLimit
    static BigInteger orgGasLimit = new BigInteger("50000000");
    // 执行交易 固定gasPrice
    static BigInteger orgGasPrice = new BigInteger("60000000000");
    // 链ID
    static byte chainId = SysConstants.GCJ_CHAIN_ID;

    public static Map<String, Object> generateWalletAddress(String password)
        /* 64: */ throws CipherException, IOException
    /* 65: */ {
        /* 66: 91 */
        String keyStoreDir = WalletUtils.getDefaultKeyDirectory();
        /* 67: 92 */
        File file = new File(keyStoreDir);
        /* 68: 94 */
        if ((!file.exists()) && (!file.isDirectory())) {
            /* 69: 96 */
            file.mkdirs();
            /* 70: */
        }
        /* 71: 98 */
        /* 72: */
        /* 73:100 */
        Bip39Wallet wallet = WalletUtils.generateBip39Wallet(password, file);
        /* 74: */
        /* 78: */
        /* 79: */
        /* 80:107 */
        Credentials credentials = WalletUtils.loadBip39Credentials(password, wallet.getMnemonic());
        /* 81: */
        /* 82:109 */
        String walletAddress = credentials.getAddress();
        /* 87:114 */
        String privateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);
        /* 88:115 */
        /* 89:116 */
        Map<String, Object> resultMap = new HashMap();
        /* 92:119 */
        return resultMap;
        /* 93: */
    }

    public static String getBalanceOfTest(String walletAddress) {
        Gcj3j gcj3j = Gcj3j.build(new HttpService(GCJ_NETWORK));
        BigDecimal fromWei = new BigDecimal("0.00");
        try {
            EthBlockNumber send = gcj3j.ethBlockNumber().send();
            System.out.println(send.getBlockNumber());
            DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(send.getBlockNumber());
            EthGetBalance ethGetBalance = (EthGetBalance) gcj3j.ethGetBalance(walletAddress, defaultBlockParameter)
                    .sendAsync().get();

            System.out.println(JSON.toJSONString(ethGetBalance));
            BigInteger balance = ethGetBalance.getBalance();
            fromWei = Convert.fromWei(balance.toString(), Convert.Unit.ETHER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fromWei.toString();
    }

    /**
     * @return
     * @throws Exception
     */
    public static Json sign(String name, String number, String unAuthAddr, String contractAddress, String password) throws Exception {
        String methodName = "sign";

        Credentials credentials = Credentials.create(CertificationGcj3j.privateKey);
        String ownAddr = credentials.getAddress();
//        BigInteger nonce;

        EthGetTransactionCount ethGetTransactionCount = gcj3j.ethGetTransactionCount(
                ownAddr, DefaultBlockParameterName.LATEST).sendAsync().get();

//        nonce = ethGetTransactionCount.getTransactionCount();

        Json json = new Json();

        BigInteger value = BigInteger.ZERO;
        List<Type> inputParameters = new ArrayList<>();

        Address addr = new Address(unAuthAddr);
        inputParameters.add(addr);
        Utf8String nameStr = new Utf8String(name);
        inputParameters.add(nameStr);
        Utf8String numberStr = new Utf8String(number);
        inputParameters.add(numberStr);
        Utf8String passStr = new Utf8String(password);
        inputParameters.add(passStr);

        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);

        RawTransaction rawTransaction = RawTransaction.createTransaction(BigInteger.ZERO, systemAddress.getNonce(), gasPrice, gasLimit, contractAddress,
                value, data);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        String signedData = Numeric.toHexString(signedMessage);

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
            return json;

        }
        return null;
    }

    public static void main(String[] args) throws Exception {
		/*Gcj3j gcj3j = Gcj3j.build(new HttpService("https://ctsign.cn:444"));
		List<String> getsAddress = getsAddress(gcj3j, "0x28fde3B80BfcbBd04Ca4C20c7C06C92909110824");
		System.out.println(JSONObject.toJSONString(getsAddress));
		
		*/

//        List<Long> list = new ArrayList<Long>();
//        List<String> contractInfo = getContractInfo(gcj3j, "0xddef3c7baec8b65e7ea176024d4c5989ce676636");
////
//        String[] split = contractInfo.get(2).split("#");
//
//        for (String str : split) {
//            list.add(new Long(str.split("&")[3]));
//        }
//        System.out.println(JSONObject.toJSONString(list));
//        System.out.println(JSONObject.toJSONString(contractInfo));
//        System.out.println(getBalanceOf("0x8715bCCE45377D8717036Bf86E51780E711C96f2"));

        //String a = transfer("0x76E56E90Ac833dDc6aF73E3ECacbd2D75328f212", "0x73003c64F0e673cAeF9f21Fa0E486B2692bD3Fa1", "51c225b47469db90ce54c666ba0db05763aa15cb0cc70cb50cd12ce419a364a4", "5000000000000000000");
        //System.out.println(a);

//        isRealName(gcj3j, "0x0C73AdC8132ab526fb29BC1daCA9665F909A9E3d", "0x2BA74de1bA8Ad9bc6E255F9839030009f03470DC");

//        String oriPass = "";
//        String enPass = "3733c91607b6a03cbf83133a010afc36036f1e68d421dada90fde97ac0433aaab1d97bad74e553a9af2173355a6a241ac815d2093bddf2337c0d2dcbe5983e7f12e4536630e26493b4bda7ce84560bf12ed717d77a723d1bf6be932f9ff4deb7080aba4d2bfb86145ef83005d42470012df1648e4fd0b8dd21a22a9691432cd920c21c2c0e50e9e10f70830c7f565ff832b2b89b2e290b7107e2f13fce2b420aed";
//        String priK = "7d665aa34883a0e69539c6a91d8921b9e2ada3124969c43339565c02feabd97b";
//        String pubK = "becf19a9a271ba8f2ac01eb73df350ed2fcdafcc54c280637cf639a88013113aabb68a76b67aa0c303441219c7de30b63e73da3c461da56fc5da33284cfc77b3";
//        String addr = "0x18Fa48c3Cc191847372d459B487745cbe4d83053";
//        String contractAddr = "0x79e5Eb96Ed737f0235B22dE42530EAC1c98D087e";
//
//        String addrPwd = getPwdByAddress(gcj3j, addr, contractAddr);
//        System.out.println(addrPwd);
//        String unAuthAddr = "0x18Fa48c3Cc191847372d459B487745cbe4d83053";
//        String contractAddress = "0x79e5Eb96Ed737f0235B22dE42530EAC1c98D087e";
//
//        String name = "赵琪";
//        name = "U2FsdGVkX18b7w8Hf3ZxiQL4NF2+42WsdZ47L+xupec=";
//        String number = "622801199509021233";
//        number = "U2FsdGVkX19HqnUW01AIJruRkbaeyQWvj92TuSMJIjzFllSj6ZtvvPt6YGjzVGu2";
//        String password = "3733c91607b6a03cbf83133a010afc36036f1e68d421dada90fde97ac0433aaab1d97bad74e553a9af2173355a6a241ac815d2093bddf2337c0d2dcbe5983e7f12e4536630e26493b4bda7ce84560bf12ed717d77a723d1bf6be932f9ff4deb7080aba4d2bfb86145ef83005d42470012df1648e4fd0b8dd21a22a9691432cd920c21c2c0e50e9e10f70830c7f565ff832b2b89b2e290b7107e2f13fce2b420aed";
//        updatePreAuthData(name, number, unAuthAddr, contractAddress, password);
//        Credentials credentials = Credentials.create("");
//        String ownAddr = credentials.getAddress();
//        System.out.println(ownAddr);
//        System.out.println(version("0x79e5eb96ed737f0235b22de42530eac1c98d087e"));
//        String pwd = Gcj3jUtils.getPwdByAddress(gcj3j, "0x18Fa48c3Cc191847372d459B487745cbe4d83053", "0xe5b30d4700d0f23a38b7e728854a8935b51a707b");
//        System.out.println(pwd.length());
//        List<String> contractInfo = Gcj3jUtils.getContractInfo(gcj3j, "0xa90b8f85387d0ae398d81b33f1f030f5e7c0ea18");
//        String[] split = contractInfo.get(2).split("#");
//        System.out.println(JSONObject.toJSONString(split));


//        BigInteger blockNumber = new BigInteger("2281624");
//        EthBlock ethBlock = gcj3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), true).send();
//        EthBlock.Block result = ethBlock.getResult();
//        System.out.println(JSONObject.toJSONString(result));
//        String ret = "00000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000005000000000000000000000000028b916512626f7ae5491d9c4765be8360289e4c00000000000000000000000002a564f11d86e7bd0dc71a7629c84998d18951b1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
//        System.out.println(JSONObject.toJSONString(proethCall(ret)));

//        String privateKey = "a2081b5b81fbea0b6b973a3ab6dbbbc65b1164488bf22d8ae2ff0b8260f64853";
//        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
//        System.out.println(ecKeyPair.getPublicKey().toString(16));
//        Credentials credentials = Credentials.create(ecKeyPair);
//        System.out.println(credentials.getAddress());
        Credentials credentials = Credentials.create("cab4b53ea89b1934a349eae308ede6d1581b679dca2b10447ce2dbee1c72f6c9");
        System.out.println(getBalanceOf(credentials.getAddress()));
    }

    public static List<String> proethCall(String str) {

        List<String> retLists = new ArrayList<>();

        if (str.contains("0x")) {
            str = str.replace("0x", "");
        }

        for (int i = 0; i < str.length() / 64; i++) {
            String add = str.substring(i * 64, i * 64 + 64);
            if (add.length() > 40) {
                int forCount = add.length() - 40;
                for (int j = 0; j < forCount; j++) {
                    add = add.substring(1);
                }
            }
            retLists.add(add);
        }

        return retLists;
    }


    public static String getBalanceOf(String walletAddress) {
        BigDecimal fromWei = new BigDecimal("0.00");
        try {
            EthGetBalance ethGetBalance = /* 102:130 */ (EthGetBalance) gcj3j
                    .ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
            BigInteger balance = ethGetBalance.getBalance();
            fromWei = Convert.fromWei(balance.toString(), Convert.Unit.ETHER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fromWei.toString();
    }

    public static String erc20BalanceOf(String walletAddress, String contractAddress) {
        String value2 = "";
        Float value = Float.valueOf(0.0F);
        BigDecimal bigDecimal = null;
        try {
            Gcj3j web3 = Gcj3j.build(new HttpService(myNetword));

            Address address = new Address(walletAddress);

            List<Type> inputParameters = new ArrayList<>();
            List<TypeReference<?>> outputParameters = new ArrayList<>();
            inputParameters.add(address);
            TypeReference<Bool> typeReference = new TypeReference<Bool>() {
            };
            outputParameters.add(typeReference);
            Function function = new Function("balanceOf", inputParameters, outputParameters);

            String encodedFunction = FunctionEncoder.encode(function);

            value2 = ((EthCall) web3
                    .ethCall(Transaction.createEthCallTransaction(walletAddress, contractAddress, encodedFunction),
                            DefaultBlockParameterName.LATEST)
                    .send()).getValue();
            bigDecimal = new BigDecimal(Long.parseLong(value2.substring(2).trim(), 16));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!bigDecimal.toString().equals("0")) {
            try {
                return bigDecimal.toString().substring(0, bigDecimal.toString().length() - 5) + "." + bigDecimal
                        .toString().substring(bigDecimal.toString().length() - 5, bigDecimal.toString().length());
            } catch (Exception e) {
                System.out.println("error=" + bigDecimal);
                e.printStackTrace();
                return "1";
            }
        }
        return "0";
    }

    /**
     * 以太币转账
     */
    public static String transfer(String toAddress, String fromAddress, String privateKey, String amount) {
        gcj3j = Gcj3j.build(new HttpService(myNetword));
        try {
            BigInteger nonce;
            EthGetTransactionCount ethGetTransactionCount = gcj3j
                    .ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send();
            if (ethGetTransactionCount == null)
                return "";
            nonce = ethGetTransactionCount.getTransactionCount();
            EthGasPrice ethGasPrice = gcj3j.ethGasPrice().send();
            BigInteger gasPrice = ethGasPrice.getGasPrice();
            String signedData = signTransaction(nonce, gasPrice, new BigInteger("21000"), toAddress,
                    new BigInteger(amount), "", SysConstants.GCJ_CHAIN_ID, privateKey);
            if (signedData != null) {
                EthSendTransaction ethSendTransaction = gcj3j.ethSendRawTransaction(signedData).send();
                return ethSendTransaction.getTransactionHash();
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 代币转账
     *
     * @throws Exception
     */

    /**
     * 查询是否是电子合同
     *
     * @param gcj3j
     * @param contractAddress
     * @return
     */
    public static BigInteger getIsQianYiContract(Gcj3j gcj3j, String contractAddress) {
        String methodName = "isQianYiContract";
        BigInteger isQianYiContract = new BigInteger("0");
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint> typeReference = new TypeReference<Uint>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = gcj3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            isQianYiContract = (BigInteger) results.get(0).getValue();
        } catch (Exception e) {
            e.printStackTrace();
            return new BigInteger("0");
        }
        return isQianYiContract;
    }


    /**
     * 代币转账
     *
     * @throws Exception
     */

    /**
     * 查询所有受送达人
     *
     * @param gcj3j
     * @param contractAddress
     * @return
     */
    public static List<String> getAddressList(Gcj3j gcj3j, String contractAddress) throws Exception {
        String methodName = "getPartyAddress";
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
/*		Uint uint = new Uint(BigInteger.ZERO);
		inputParameters.add( uint);*/
        List<TypeReference<?>> outputParameters = new ArrayList<>();

//        TypeReference<Uint> typeReference = new TypeReference<Uint>() {
//        };

//        outputParameters.add(typeReference);
//        outputParameters.add(typeReference);
//        outputParameters.add(typeReference);
//        outputParameters.add(typeReference);
//        outputParameters.add(typeReference);
//        outputParameters.add(typeReference);
//        outputParameters.add(typeReference);
//        outputParameters.add(typeReference);
//        outputParameters.add(typeReference);
//        outputParameters.add(typeReference);
//        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);
        List<String> list = new ArrayList<String>();
        EthCall ethCall;
        try {
            ethCall = gcj3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
//            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            List<String> results = proethCall(ethCall.getValue());
            for (int i = 2; i < results.size(); i++) {
                try {
                    System.out.println(i);
                    String add = results.get(i);
                    System.out.println(add.length());
                    if (add.length() < 40) {
                        for (int j = 0; j < 40 - add.length(); j++) {
                            add = "0" + add;
                        }
                    }

                    list.add("0x" + add);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

            }

        } catch (Exception e) {
            outputParameters.remove(outputParameters.size() - 1);
            ethCall = gcj3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            function = new Function(methodName, inputParameters, outputParameters);
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            for (int i = 2; i < results.size(); i++) {
                String add = new BigInteger(results.get(i).getValue().toString()).toString(16);
                System.out.println(add.length());
                if (add.length() < 40) {
                    for (int j = 0; j < 40 - add.length(); j++) {
                        add = "0" + add;
                    }
                }
                list.add("0x" + add);

            }
        }
        return list;
    }

    /**
     * 查询所有人ID
     *
     * @param gcj3j
     * @param contractAddress
     * @return
     */
    public static List<String> getContractInfo(Gcj3j gcj3j, String contractAddress) {
        String methodName = "contractInfo";
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };

        outputParameters.add(typeReference);
        outputParameters.add(typeReference);
        outputParameters.add(typeReference);
        outputParameters.add(typeReference);
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);
        List<String> list = new ArrayList<String>();
        EthCall ethCall;
        try {
            ethCall = gcj3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            for (Type type : results) {
                list.add(type.getValue().toString());
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return list;
    }

    /**
     * 查询合同名称
     *
     * @param gcj3j
     * @param contractAddress
     * @return
     */
    public static String getName(Gcj3j gcj3j, String contractAddress) {
        String methodName = "name";
        String name = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = gcj3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            System.out.println(results);
            name = results.get(0).getValue().toString();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 查询实名状态
     *
     * @param gcj3j
     * @param contractAddress
     * @return
     */
    public static String isRealName(Gcj3j gcj3j, String contractAddress, String addr) {
        String methodName = "isRealName";
        String name = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        Address address = new Address(addr);
        inputParameters.add(address);
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = gcj3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            System.out.println(JSONObject.toJSONString(ethCall));
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            System.out.println(results);
            name = results.get(0).getValue().toString();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 查询合同编号
     *
     * @param gcj3j
     * @param contractAddress
     * @return
     */
    public static String getId(Gcj3j gcj3j, String contractAddress) {
        String methodName = "id";
        String name = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = gcj3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            name = results.get(0).getValue().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 查询合同编号
     *
     * @param gcj3j
     * @param contractAddress
     * @return
     */
    public static String getSAddressList(Gcj3j gcj3j, String contractAddress) {
        String methodName = "sAddressList";
        String name = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = gcj3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            name = results.get(0).getValue().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return name;
    }


    /**
     * 根据地址查询密码
     *
     * @param gcj3j
     * @param contractAddress
     * @return
     */
    public static String getPwdByAddress(Gcj3j gcj3j, String from, String contractAddress) {
        String methodName = "getPwdByAddress";
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(from));
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        String pwd = "";

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = gcj3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            pwd = results.get(0).getValue().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pwd;
    }

    /**
     * 根据地址查询密码
     *
     * @param gcj3j
     * @param contractAddress
     * @return
     */
    public static String getAuthorizationPwdByAddress(Gcj3j gcj3j, String from, String contractAddress) {
        String methodName = "getAuthorizationPwdByAddress";
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(from));
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        String pwd = "";

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = gcj3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            pwd = results.get(0).getValue().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pwd;
    }

    /**
     * 查询代币余额
     */
    public static BigInteger getTokenBalance(Gcj3j gcj3j, String fromAddress, String contractAddress) {

        String methodName = "balanceOf";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address address = new Address(fromAddress);
        inputParameters.add(address);

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);

        EthCall ethCall;
        BigInteger balanceValue = BigInteger.ZERO;
        try {
            ethCall = gcj3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            balanceValue = (BigInteger) results.get(0).getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return balanceValue;
    }

    /**
     * 查询代币名称
     *
     * @param gcj3j
     * @param contractAddress
     * @return
     */
    public static String getTokenName(Gcj3j gcj3j, String contractAddress) {
        String methodName = "name";
        String name = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = gcj3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            name = results.get(0).getValue().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 查询代币符号
     *
     * @param gcj3j
     * @param contractAddress
     * @return
     */
    public static String getTokenSymbol(Gcj3j gcj3j, String contractAddress) {
        String methodName = "symbol";
        String symbol = null;
        String fromAddr = emptyAddress;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = gcj3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            symbol = results.get(0).getValue().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return symbol;
    }

    /**
     * 查询代币精度
     *
     * @param gcj3j
     * @param contractAddress
     * @return
     */
    public static int getTokenDecimals(Gcj3j gcj3j, String contractAddress) {

        String methodName = "decimals";
        String fromAddr = emptyAddress;
        int decimal = 0;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint8> typeReference = new TypeReference<Uint8>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = gcj3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            decimal = Integer.parseInt(results.get(0).getValue().toString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return decimal;
    }

    /**
     * 查询代币发行总量
     *
     * @param gcj3j
     * @param contractAddress
     * @return
     */
    public static BigInteger getTokenTotalSupply(Gcj3j gcj3j, String contractAddress) {
        String methodName = "totalSupply";
        String fromAddr = emptyAddress;
        BigInteger totalSupply = BigInteger.ZERO;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall;
        try {
            ethCall = gcj3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            totalSupply = (BigInteger) results.get(0).getValue();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return totalSupply;
    }

    @SuppressWarnings({"rawtypes", "deprecation"})
    //    动态添加验证人
    public static String handleCertTransfer(String privateKey, String contractAddress, String toAddress,
                                            BigDecimal amount, int decimals) throws Exception {
        gcj3j = Gcj3j.build(new HttpService(myNetword));
        Credentials credentials = Credentials.create(privateKey);
        String fromAddress = credentials.getAddress();
        BigInteger nonce;
        EthGetTransactionCount ethGetTransactionCount = gcj3j
                .ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send();
        if (ethGetTransactionCount == null)
            return "";
        nonce = ethGetTransactionCount.getTransactionCount();
        BigInteger gasPrice = Transfer.GAS_PRICE;
        BigInteger gasLimit = new BigInteger("90000");
        BigInteger value = BigInteger.ZERO;

        EthGasPrice ethGasPrice = gcj3j.ethGasPrice().send();
        gasPrice = ethGasPrice.getGasPrice();
        byte chainId = ChainId.MAINNET;
        String signedData = signCertTransaction(BigInteger.ONE, nonce, gasPrice, gasLimit, "", value, "", SysConstants.GCJ_CHAIN_ID,
                privateKey);
        if (signedData != null) {

            EthSendTransaction ethSendTransaction = gcj3j.ethSendRawTransaction(signedData).send();
            return ethSendTransaction.getTransactionHash();
            // return send.getTransactionHash();
        } else {
            return "";
        }
    }

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
        // if (chainId > -1) {
        signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        /*
         * } else { signedMessage =
         * TransactionEncoder.signMessage(rawTransaction, credentials); }
         */
        String hexValue = Numeric.toHexString(signedMessage);
        return hexValue;
    }

    public static String signCertTransaction(BigInteger type, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to,
                                             BigInteger value, String data, byte chainId, String privateKey) throws IOException {
//       1 我想成为候选恶人
//       2 取消选恶人
        //        storageAddress =

//        3 投票
//        4 取消投票
        //        to

//        BigInteger type = BigInteger.ZERO;
        RawTransaction rawTransaction = RawTransaction.createCertificationTransaction(type, nonce, gasPrice, gasLimit, to,
                value, data, "", "", "", "", BigInteger.ZERO);
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

    public static String getAddressByPrivateKey(String privateKey)
        /* 255: */ {
        /* 256:297 */
        Credentials credentials = Credentials.create(privateKey);
        /* 257:298 */
        return credentials.getAddress();
        /* 258: */
    }

    /* 259: */
    /* 260: */
    public static String getMinerFee()
    /* 261: */ {
        /* 262:305 */
        BigInteger gasPrice = Convert.toWei(BigDecimal.valueOf(3L), Convert.Unit.GWEI).toBigInteger();
        /* 263:306 */
        BigInteger gasLimit = BigInteger.valueOf(60000L);
        /* 264:307 */
        BigDecimal minerFee = Convert.fromWei(gasLimit.multiply(gasPrice).toString(), Convert.Unit.ETHER);
        /* 265:308 */
        return String.format(minerFee.toString() + " %s", new Object[]{"ether"});
        /* 266: */
    }

    /* 267: */
    /* 268: */
    public static void createWallet(String password)/* 269: */ throws Exception
    /* 270: */ {
        /* 271:319 */
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        /* 272:320 */
        WalletFile walletFile = Wallet.createStandard(password, ecKeyPair);
        /* 273:321 */
        System.out.println("address " + walletFile.getAddress());
        /* 274:322 */
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        /* 275:323 */
        String jsonStr = objectMapper.writeValueAsString(walletFile);
        /* 276:324 */
        System.out.println("keystore json file " + jsonStr);
        /* 277:325 */
        System.out.println(decryptWallet(jsonStr, password));
        /* 278: */
    }

    /* 279: */
    /* 280: */
    public static String decryptWallet(String keystore, String password)
    /* 281: */ {
        /* 282:335 */
        String privateKey = null;
        /* 283:336 */
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        /* 284: */
        try
            /* 285: */ {
            /* 286:338 */
            WalletFile walletFile = (WalletFile) objectMapper.readValue(keystore, WalletFile.class);
            /* 287:339 */
            ECKeyPair ecKeyPair = null;
            /* 288:340 */
            ecKeyPair = Wallet.decrypt(password, walletFile);
            /* 289:341 */
            privateKey = ecKeyPair.getPrivateKey().toString(16);
            /* 290:342 */
            System.out.println(privateKey);
            /* 291: */
        }
        /* 292: */ catch (CipherException e)
            /* 293: */ {
            /* 294:344 */
            if ("Invalid password provided".equals(e.getMessage())) {
                /* 295:345 */
                System.out.println("瀵嗙爜閿欒");
                /* 296: */
            }
            /* 297:347 */
            e.printStackTrace();
            /* 298: */
        }
        /* 299: */ catch (IOException e)
            /* 300: */ {
            /* 301:349 */
            e.printStackTrace();
            /* 302: */
        }
        /* 303:351 */
        return privateKey;
        /* 304: */
    }
    /* 305: */

    @SuppressWarnings("rawtypes")
    public static int getTokenDecimals(String contractAddress) throws Exception {
        gcj3j = Gcj3j.build(new HttpService(myNetword));
        String methodName = "decimals";
        String fromAddr = emptyAddress;
        int decimal = 0;
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Uint8> typeReference = new TypeReference<Uint8>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, inputParameters, outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall = gcj3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
        List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        if (results != null) {
            decimal = Integer.parseInt(results.get(0).getValue().toString());
        }
        return decimal;
    }

    public static double version(String contractAddress) throws Exception {
        gcj3j = Gcj3j.build(new HttpService(myNetword));
        String methodName = "version";
        String fromAddr = emptyAddress;
        double decimal = 0.0;
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
        };
        outputParameters.add(typeReference);

        Function function = new Function(methodName, new ArrayList<>(), outputParameters);

        String data = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

        EthCall ethCall = gcj3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
        List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
//        System.out.println(JSONObject.toJSONString(results));
        if (results != null) {
            decimal = Double.parseDouble(results.get(0).getValue().toString());
        }
        return decimal;
    }

    /**
     * 动态添加验证节点
     *
     * @param privateKey
     * @param contractAddress
     * @param toAddress
     * @param amount
     * @param decimals
     * @return
     * @throws Exception
     */
    @SuppressWarnings({"rawtypes", "deprecation"})
    public static String handleTokenTransfer(String privateKey, String contractAddress, String toAddress,
                                             BigDecimal amount, int decimals) throws Exception {
        gcj3j = Gcj3j.build(new HttpService(myNetword));
        Credentials credentials = Credentials.create(privateKey);
        String fromAddress = credentials.getAddress();
        BigInteger nonce;
        EthGetTransactionCount ethGetTransactionCount = gcj3j
                .ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send();
        if (ethGetTransactionCount == null)
            return "";
        nonce = ethGetTransactionCount.getTransactionCount();
        BigInteger gasPrice = Transfer.GAS_PRICE;
        BigInteger gasLimit = new BigInteger("90000");
        BigInteger value = BigInteger.ZERO;
        // token转账参数
        String methodName = "transfer";
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Address tAddress = new Address(toAddress);
        Uint256 tokenValue = new Uint256(amount.multiply(BigDecimal.TEN.pow(decimals)).toBigInteger());
        inputParameters.add(tAddress);
        inputParameters.add(tokenValue);
        TypeReference<Bool> typeReference = new TypeReference<Bool>() {
        };
        outputParameters.add(typeReference);
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);

        EthGasPrice ethGasPrice = gcj3j.ethGasPrice().send();
        gasPrice = ethGasPrice.getGasPrice();
        byte chainId = ChainId.MAINNET;
        String signedData = signTransaction(nonce, gasPrice, gasLimit, contractAddress, value, data, SysConstants.GCJ_CHAIN_ID,
                privateKey);
        if (signedData != null) {
            // Transaction transaction = new
            // Transaction("0x18aec70b28a04774e43fd8542a4b9690a60b35dd", nonce,
            // gasPrice, gasLimit, toAddress, value, data);
            // EthSendTransaction send =
            // gcj3j.ethSendTransaction(transaction).send();
            EthSendTransaction ethSendTransaction = gcj3j.ethSendRawTransaction(signedData).send();
            return ethSendTransaction.getTransactionHash();
            // return send.getTransactionHash();
        } else {
            return "";
        }
    }

}

/*
 * Location: C:\Users\tzy\Desktop\coin_spider.jar
 *
 * Qualified Name: cn.sh.qianyi.coinspider.util.Gcj3jUtils
 *
 * JD-Core Version: 0.7.0.1
 *
 */