package cn.sh.qianyi.coinspider.task;

import cn.sh.qianyi.coinspider.bean.*;
import cn.sh.qianyi.coinspider.email.EmailInfo;
import cn.sh.qianyi.coinspider.email.EmailUtil;
import cn.sh.qianyi.coinspider.httpclient.SQLConnection;
import cn.sh.qianyi.coinspider.main.CertificationGcj3j;
import cn.sh.qianyi.coinspider.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.gcj3j.protocol.Gcj3j;
import org.gcj3j.protocol.core.DefaultBlockParameter;
import org.gcj3j.protocol.core.DefaultBlockParameterName;
import org.gcj3j.protocol.core.methods.response.EthBlock;
import org.gcj3j.protocol.core.methods.response.EthBlockNumber;
import org.gcj3j.protocol.core.methods.response.EthGetBalance;
import org.gcj3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.gcj3j.protocol.http.HttpService;
import org.gcj3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.sh.qianyi.coinspider.util.Gcj3jUtils.myNetword;

/**
 * Title: CoinTask.java
 * <p>
 * Desiription: 扫描区块
 * <p>
 * Company:SH qianyi
 *
 * @author corwin
 * @version 1.0
 * @date 2019年4月23日
 */
public class CoinTask {
    static Logger log;
    //static Gcj3j gcj3j = Gcj3j.build(new HttpService("http://172.16.235.43:7576"));
    static Gcj3j gcj3j = Gcj3j.build(new HttpService(myNetword));
    static SQLConnection connection = new SQLConnection();

    static {
        CoinTask.log = Logger.getLogger(CoinTask.class);
    }

    /**
     * Title:start Description:开始扫描区块
     */
    public static void start() {


        Block lastBlock = connection.queryLastBlock();
        BigInteger blockNumber = (lastBlock.getNumber() == null) ? new BigInteger("1")
                : new BigInteger(lastBlock.getNumber().toString()).add(new BigInteger("1"));
//        BigInteger blockNumber = new BigInteger("14");
        while (true) {
            try {
            	System.out.println(blockNumber);
                EthBlock ethBlock = gcj3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), true).send();
                EthBlock.Block result = ethBlock.getResult();
                System.out.println(result);
                EthBlockNumber send = gcj3j.ethBlockNumber().send();
                log.info("currentBlockNumber:" + blockNumber + " ;lastBlockNumber:" + send.getBlockNumber());
                if (result == null) {
                    log.info("stopBlockNumber:" + blockNumber);
                    Thread.sleep(2000L);
                    continue;
                }
                Block block = new Block();
                block.setTimestamp(result.getTimestamp());
                block.setDifficulty(result.getDifficulty());
                block.setGasLimit(result.getGasLimit());
                block.setGasUsed(result.getGasUsed());
                block.setHash(result.getHash());
                block.setMiner(result.getMiner());
                block.setNonce(result.getNonce());

                block.setNumber(result.getNumber().intValue());
                block.setSize(result.getSize().intValue());
                block.setExtraData(result.getExtraData());
                block.setTotalDifficulty(result.getTotalDifficulty());
                block.setParentHash(result.getParentHash());
                block.setReceiptsRoot(result.getReceiptsRoot());
                block.setTxs(result.getTransactions().size());
                block.setSha3Uncles(result.getSha3Uncles());

                connection.insertBlock(block);

                String json = JSON.toJSONString(result.getTransactions());
                JSONArray array = JSON.parseArray(json);
                CoinTask.log.info("array:" + array.size());
                List<Transaction> transactions = new ArrayList<Transaction>();

                for (Object object : array) {
                    try {
                        Transaction transaction = new Transaction();
                        JSONObject jsonObject = (JSONObject) object;
                        transaction.setBlockHash(jsonObject.getString("blockHash"));
                        transaction.setBlockNumber(jsonObject.getInteger("blockNumber"));
                        transaction.setFrom(jsonObject.getString("from"));
                        transaction.setGas(jsonObject.getBigInteger("gas"));
                        transaction.setGasPrice(jsonObject.getBigInteger("gasPrice"));
                        transaction.setHash(jsonObject.getString("hash"));

                        transaction.setNonce(jsonObject.getBigInteger("nonce"));
                        transaction.setTo(jsonObject.getString("to"));
                        transaction.setTransactionIndex(jsonObject.getInteger("transactionIndex"));
                        transaction.setV(jsonObject.getInteger("v"));
                        transaction.setValue(jsonObject.getBigInteger("value"));
                     
                        transaction.setTimestamp(result.getTimestamp());

                        // 获取交易详情
                        EthGetTransactionReceipt transactionReceipt = gcj3j
                                .ethGetTransactionReceipt(jsonObject.getString("hash")).send();
                        String tradeStr = JSONObject.toJSONString(transactionReceipt.getResult());
                        JSONObject parseObject = JSONObject.parseObject(tradeStr);
                        BigInteger cumulativeGasUsed = parseObject.getBigInteger("cumulativeGasUsed");

                        JSONArray jsonArray = parseObject.getJSONArray("logs");

                        if (jsonArray.size() != 0) {
                            JSONObject obj = (JSONObject) jsonArray.get(0);
                            String removed = obj.getString("removed");
                            transaction.setRemoved(removed);

                            JSONArray jsonArray2 = obj.getJSONArray("topics");
                            if (jsonArray2.size() != 0) {
                                transaction.setTopics(JSON.toJSONString(jsonArray2));
                            }
                        }

                        transaction.setCumulativeGasUsed(cumulativeGasUsed);
                        transaction.setGasUsed(parseObject.getBigInteger("gasUsed"));
                        transaction.setStatus(parseObject.getString("status"));
                        String contractAddress = parseObject.getString("contractAddress");


                        /**
                         * 如果是智能合约交易
                         ***/
                        if (!jsonObject.getString("input").equals("0x") && !jsonObject.getString("input").equals("")) {
                            BigInteger isQianYiContract = null;
                            if (contractAddress != null) {
                                isQianYiContract = Gcj3jUtils.getIsQianYiContract(gcj3j, contractAddress);
                                if (isQianYiContract.compareTo(new BigInteger("1")) == 0) {
                                    String pwd = Gcj3jUtils.getPwdByAddress(gcj3j, jsonObject.getString("from"), contractAddress);
                                    String id = Gcj3jUtils.getId(gcj3j, contractAddress);
                                    String name = Gcj3jUtils.getName(gcj3j, contractAddress);
                                    Contract contract = new Contract();
                                    contract.setAddress(contractAddress);
                                    contract.setCreator(transaction.getFrom());
                                    contract.setName(name);
                                    contract.setHash(transaction.getHash());
                                    contract.setTimestamp(result.getTimestamp());
                                    contract.setId(id);
                                    contract.setType(1);
                                    connection.insertContract(contract);
                                    transaction.setPwd(pwd);
                                    transaction.setContract(1);
                                    transaction.setTo(contractAddress);
                                    transaction.setOperating(TransOpeTypeEnum.CONTRAGCJ_CREATE.status);
                                    DatePackage datePackage = connection.queryDatePackage(transaction.getFrom());
                                    if (datePackage != null) {
                                        if (new Date().getTime() >= datePackage.getBeginDate().getTime() && new Date().getTime() <= datePackage.getEndDate().getTime()) {
                                            String price = connection.getDictByKey("CO_SIGN_RMB_FEE");
                                            Json reward = CertificationGcj3j.reward(transaction.getFrom(), new BigInteger(price));
                                            if (reward.isFlag()) {
                                                System.out.println("因购买套餐,赠送创建方金额:" + connection.getDictByKey("CO_SIGN_RMB_FEE"));
                                            }
                                        }
                                    }
                                    Certification fromCertification = connection.getCertificationsByAddress(transaction.getFrom());
                                    List<Long> list = new ArrayList<Long>();
                                    List<String> contractInfo = Gcj3jUtils.getContractInfo(gcj3j, contractAddress);
                                    String[] split = contractInfo.get(2).split("#");
                                    for (String str : split) {
                                        list.add(new Long(str.split("&")[3]));
                                    }
                                    List<String> addressList = Gcj3jUtils.getAddressList(gcj3j, contractAddress);
                                    for (int i = 0; i < list.size(); i++) {
                                        ContractCertification contractCertification = new ContractCertification();
                                        contractCertification.setAddress(contractAddress);
                                        contractCertification.setCertificationId(list.get(i));
                                        contractCertification.setCertIdentity(1);
                                        contractCertification.setStatus(0);
                                        contractCertification.setPwd(Gcj3jUtils.getPwdByAddress(gcj3j, addressList.get(i), contractAddress));
                                        if (fromCertification.getId().equals(list.get(i))) {

                                            contractCertification.setCertIdentity(2);
                                            contractCertification.setStatus(1);
                                        }
                                        connection.insertContractCertification(contractCertification);

                                    }


                                    List<String> sAddress = Gcj3jUtils.getAddressList(gcj3j, contractAddress);
                                    Certification fromAddress = connection.getCertificationsByAddress(jsonObject.getString("from"));
                                    if (fromAddress.getChannelId() == 1) {
                                        for (String add : sAddress) {
                                            if (!add.equals(jsonObject.getString("from"))) {
                                                Certification certificationsByAddress = connection.getCertificationsByAddress(add);
                                                if (certificationsByAddress.getType() == null) {
                                                    //预创建
//                                                    if (certificationsByAddress.getMobile().contains("@")) {
//                                                        String content = "【草田签】您好，" + fromAddress.getRealName() + "通过非中心化区块链电子合同草田签给您发一个有隐私保证在线电子合同， 请通过草田签Dapp进行确认签署。\r\n客服电话：15900824215\r\n[草田签：充分保障合同隐私与身份安全的非中心化区块链电子合同]";
//
//                                                        EmailInfo info = new EmailInfo(certificationsByAddress.getMobile(), "草田签提醒您...", content);
//                                                        EmailUtil.sendHtmlMail(info);
//
//                                                    } else {
//                                                        SendSms.sendSms(certificationsByAddress.getMobile(), SendSms.CREATE_CONTRACT, "{name:'" + fromAddress.getRealName() + "'}");
//                                                    }
                                                    continue;
                                                }
                                                String mobile = certificationsByAddress.getMobile();
                                                if (certificationsByAddress.getType() == 1) {

                                                    if (mobile.contains("@")) {
//                                                        String content = "【草田签】您好，" + fromAddress.getRealName() + "通过非中心化区块链电子合同草田签给您发一个有隐私保证在线电子合同， 请通过草田签Dapp进行确认签署。\r\n客服电话：15900824215\r\n[草田签：充分保障合同隐私与身份安全的非中心化区块链电子合同]";
                                                        String content = "您好，" + fromAddress.getRealName() + "通过非中心化区块链电子合同草田签给您发一个有隐私保证在线电子合同， 请前往草田签Dapp：https://ctsign.cn登录后进行确认签署。";
                                                        EmailInfo info = new EmailInfo(mobile, "草田签提醒您...", content);
                                                        EmailUtil.sendHtmlMail(info);

                                                    } else {
                                                        SendSms.sendSms(mobile, SendSms.CREATE_CONTRACT, "{name:'" + fromAddress.getRealName() + "'}");
                                                    }


                                                } else {
//                                                    SendSms.sendSms(mobile, SendSms.CO_CREATE_CONTRACT, "{name:'" + fromAddress.getRealName() + "'}");
                                                    if (mobile.contains("@")) {
//                                                        String content = "【草田签】您好，" + fromAddress.getRealName() + "通过非中心化区块链电子合同草田签给您发一个有隐私保证在线电子合同， 请登录Dapp应用https://orgweb.ctsign.cn进行确认签署。\r\n客服电话：15900824215\r\n[草田签：充分保障合同隐私与身份安全的非中心化区块链电子合同]";
                                                        String content = "您好，" + fromAddress.getRealName() + "通过非中心化区块链电子合同草田签给您发一个有隐私保证在线电子合同，请登录Dapp应用https://ctsign.cn进行确认签署。";

                                                        EmailInfo info = new EmailInfo(mobile, "草田签提醒您...", content);
                                                        EmailUtil.sendHtmlMail(info);

                                                    } else {
                                                        SendSms.sendSms(mobile, SendSms.CREATE_CONTRACT, "{name:'" + fromAddress.getRealName() + "'}");
                                                    }
                                                }
												/*String type = "";
												if(certificationsByAddress.getType() == 1){
													type = "SIGN_RMB_FEE"; 
												}else{
													type = "CO_SIGN_RMB_FEE"; 
												}
												String price  = connection.getDictByKey(type);
												Json reward = CertificationGcj3j.reward(certificationsByAddress.getWalletAddress(), new BigInteger(price));
												if(reward.isFlag()){
													System.out.println("赠送签署方金额:"+connection.getDictByKey(type));
												}*/
                                                //MessageUtil.sendMessage(mobile, content);
                                            }
                                        }
                                    }/*else if(fromAddress.getChannelId() == 2){
										for (String add : sAddress) {
											Certification certificationsByAddress = connection.getCertificationsByAddress("0x"+add);
											String mobile = certificationsByAddress.getMobile();
											String content = "【享尚链】您好！"+certificationsByAddress.getRealName()+"，"+fromAddress.getRealName()+"通过享尚链APP向您发起一份电子合同，合同号为"+contractAddress+"，请及时前往享尚链APP并进行签署，感谢您的支持与配合！ ";
											MessageUtil.sendMessage(mobile, content);
										}
									}*/


                                }

                            } else if (Gcj3jUtils.getIsQianYiContract(gcj3j, jsonObject.getString("to")).compareTo(new BigInteger("1")) == 0) {
                                Contract contract = connection.getContractByAddress(jsonObject.getString("to"));
                                Certification certificationsByAddress = connection.getCertificationsByAddress(contract.getCreator());

                                String content = null;
                                transaction.setContract(1);
                                String pwd = Gcj3jUtils.getPwdByAddress(gcj3j, jsonObject.getString("from"), jsonObject.getString("to"));
                                transaction.setPwd(pwd);
                                String input = jsonObject.getString("input");
                                if (input.substring(0, 10).equals("0xaeaddea4")) {
                                    Certification fromAuthAddress = connection.getCertificationsByAddress(jsonObject.getString("from"));
                                    Certification toAuthAddress = connection.getCertificationsByAddress("0x" + input.substring(34, 74));
                                    if (fromAuthAddress.getChannelId() == 1) {
                                        //content = "【草田签】您好！"+toAuthAddress.getRealName()+"，用户"+fromAuthAddress.getRealName()+"向你授权合同查看，您可以通过网址https://ctsign.cn/#/compactDetail2?contractAddress="+jsonObject.getString("to")+" 查看此合同，感谢您的支持与配合。 ";
                                        MessageUtil.sendMessage(toAuthAddress.getMobile(), content);
                                    }
                                    System.out.println(Gcj3jUtils.getAuthorizationPwdByAddress(gcj3j, "0x" + input.substring(34, 74), jsonObject.getString("to")));
                                    connection.insertAuthorization(fromAuthAddress.getId(),
                                            toAuthAddress.getId(), jsonObject.getString("to"),
                                            Gcj3jUtils.getAuthorizationPwdByAddress(gcj3j, "0x" + input.substring(34, 74), jsonObject.getString("to")));
                                    //授权
                                    transaction.setOperating(TransOpeTypeEnum.CONTRAGCJ_AUTH.status);
                                } else if (input.substring(0, 10).equals("0x6a89a3f6")) {
                                    //合同变更
                                    transaction.setOperating(TransOpeTypeEnum.CONTRAGCJ_SUPPLY.status);
                                } else if (input.substring(0, 10).equals("0x089ae1aa")) {
                                    //补充签署
                                    transaction.setOperating(TransOpeTypeEnum.SUPPLY_SIGN.status);
                                } else if (input.substring(0, 10).equals("0x80520898")) {
                                    //TODO 实名后预创建合同改为正常合同   TODO
                                    transaction.setContract(0);
                                } else if (input.substring(0, 10).equals("0x4a44c61a")) {
                                    //合同签署
                                    transaction.setOperating(TransOpeTypeEnum.CONTRAGCJ_SIGN.status);
                                    if (certificationsByAddress.getChannelId() == 1 && transaction.getStatus().equals("0x1")) {
                                        Certification signCertification = connection.getCertificationsByAddress(jsonObject.getString("from"));
                                        ContractCertification cc = new ContractCertification();
                                        cc.setCertificationId(signCertification.getId());
                                        cc.setAddress(jsonObject.getString("to"));
                                        cc.setStatus(1);
                                        cc.setPwd(pwd);
                                        connection.updateContractCertification(cc);
                                        //content = "【草田签】您好！"+certificationsByAddress.getRealName()+"，您发起的电子合同已被签署，您可以通过网址https://ctsign.cn/#/compactDetail2?contractAddress="+jsonObject.getString("to")+" 进行查看，感谢您的支持与配合。 ";
                                        //MessageUtil.sendMessage(certificationsByAddress.getMobile(), content);
                                    }
                                }
                            }
                        }

                        transactions.add(transaction);
                        reward(connection.getCertificationsByAddress(transaction.getFrom()));

                        CoinTask.log.info("insert transaction success!");
                    } catch (Exception e) {
                        CoinTask.log.error("error array", e);
                        e.printStackTrace();
                    }
                }
                connection.insertTransactions(transactions);

            } catch (Exception e2) {
                blockNumber = blockNumber.subtract(new BigInteger("1"));
                CoinTask.log.error("error erc20 recharge", e2);
                e2.printStackTrace();
            }
            blockNumber = blockNumber.add(new BigInteger("1"));
        }
    }

    //余额不足就赠送
    public static void reward(Certification certification) throws Exception {
        if (certification == null || certification.getChannelId() == null) {
            return;
        }
        if (certification.getChannelId() == 4) {
            System.out.println("检查该渠道地址中");
            String boundaries = connection.getDictByKey("XSL_BOUNDARY");
            BigDecimal balance = getBalanceOf(certification.getWalletAddress());
            if (balance.compareTo(new BigDecimal(boundaries)) == -1) {
                Json reward = CertificationGcj3j.reward(certification.getWalletAddress(), new BigInteger(connection.getDictByKey("XSL_REWARD")));
                if (reward.isFlag()) {
                    System.out.println("检查余额为" + balance.toString() + ",渠道赠送成功");
                }

            }
        }
    }

    public static BigDecimal getBalanceOf(String walletAddress) {
        BigDecimal fromWei = new BigDecimal("0.00");
        try {
            EthGetBalance ethGetBalance = /* 102:130 */ (EthGetBalance) gcj3j
                    .ethGetBalance(walletAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
            BigInteger balance = ethGetBalance.getBalance();
            fromWei = Convert.fromWei(balance.toString(), Convert.Unit.ETHER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fromWei;
    }

    public static void main(String[] args) {
        ///String content = "【草田签】您好！陶卓延，诉讼文书已通过草田签向您发送，请您进入网址https://ctsign.cn/#/deliveryDetail?contractAddress=0x6677E6d055f628468b1C3D9bdA4932d0cCbb1c4D进行登录并签收，感谢您的支持与配合。";
        //MessageUtil.sendMessage("15002107061", content);
    	
        start();
//        try {
//            while (true) {
//                String doGet = HttpClient.doGet("https://www.instagram.com/dennycagur/");
//                System.out.println(doGet.contains("Suami"));
//                Thread.sleep(50);
//            }
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }
}
