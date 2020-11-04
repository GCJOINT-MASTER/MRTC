package cn.sh.qianyi.coinspider.httpclient;

import cn.sh.qianyi.coinspider.bean.*;
import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLConnection extends BaseDao {
    protected Connection conn;
    protected Statement stat;
    protected PreparedStatement ps;
    protected ResultSet rs;
    static Logger log;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    static {
        SQLConnection.log = Logger.getLogger(SQLConnection.class);
    }

    protected void closeResource() {
        if (this.rs != null) {
            try {
                this.rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (this.ps != null) {
            try {
                this.ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (this.stat != null) {
            try {
                this.stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertBlock(Block block) {
        try {
            final String sql = "insert t_block(number,difficulty,gasLimit,gasUsed,hash,miner,nonce,size,timestamp,totalDifficulty,extraData,parentHash,receiptsRoot,sha3Uncles,txs)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            this.conn = this.getConnection();
            this.ps = this.conn.prepareStatement(sql);
            this.ps.setInt(1, block.getNumber());
            ps.setString(2, block.getDifficulty().toString());
            ps.setString(3, block.getGasLimit().toString());
            ps.setString(4, block.getGasUsed().toString());
            ps.setString(5, block.getHash());
            ps.setString(6, block.getMiner());
            ps.setString(7, block.getNonce().toString());
            ps.setString(8, block.getSize().toString());
            ps.setString(9, block.getTimestamp().toString());
            ps.setString(10, block.getTotalDifficulty().toString());
            ps.setString(11, block.getExtraData());
            ps.setString(12, block.getParentHash());
            ps.setString(13, block.getReceiptsRoot());
            ps.setString(14, block.getSha3Uncles());
            ps.setInt(15, block.getTxs());
            this.ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } finally {
            this.closeResource();
        }
    }

    public void insertBalanceLog(BalanceLog balanceLog) {
        try {
            final String sql = "insert balance_log(transaction,before_balance,after_balance,token_id)values(?,?,?,?)";
            this.conn = this.getConnection();
            this.ps = this.conn.prepareStatement(sql);
            this.ps.setString(1, balanceLog.getTransaction());
            ps.setString(2, balanceLog.getBeforeBalance());
            ps.setString(3, balanceLog.getAfterBalance());
            ps.setString(4, balanceLog.getTokenId());
            this.ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } finally {
            this.closeResource();
        }
    }

    public void insertTTransactionTokenData(TTransactionTokenData tTransactionTokenData) {
        try {
            final String sql = "insert t_transaction_token_data(transaction,token_id,balance)values(?,?,?)";
            this.conn = this.getConnection();
            this.ps = this.conn.prepareStatement(sql);
            this.ps.setString(1, tTransactionTokenData.getTransaction());
            ps.setString(2, tTransactionTokenData.getTokenId());
            ps.setString(3, tTransactionTokenData.getBalance());
            this.ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } finally {
            this.closeResource();
        }
    }

    public void updateTTransactionTokenData(TTransactionTokenData tTransactionTokenData) {
        try {
            final String sql = "update t_transaction_token_data set balance = ? where transaction = ? and token_id = ? ";
            this.conn = this.getConnection();
            this.ps = this.conn.prepareStatement(sql);
            this.ps.setString(1, tTransactionTokenData.getBalance());
            ps.setString(2, tTransactionTokenData.getTransaction());
            ps.setString(3, tTransactionTokenData.getTokenId());
            this.ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } finally {
            this.closeResource();
        }
    }

    public void insertTransactions(List<Transaction> transactions) {
        try {
            final String sql = "insert t_transactions(blockHash,blockNumber,`from`,gas,gasPrice,`hash`,input,nonce,`to`,transactionIndex,v,`value`,`timestamp`,cumulativeGasUsed,gasUsed,status,removed,topics,realAddress,realValue,contract,tokenName,operating,pwd)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            this.conn = this.getConnection();
            this.ps = this.conn.prepareStatement(sql);
            for (Transaction transaction : transactions) {
                this.ps.setString(1, transaction.getBlockHash());
                ps.setInt(2, transaction.getBlockNumber());
                ps.setString(3, transaction.getFrom());
                ps.setString(4, transaction.getGas().toString());
                ps.setString(5, transaction.getGasPrice().toString());
                ps.setString(6, transaction.getHash());
                ps.setString(7, transaction.getInput());
                ps.setString(8, transaction.getNonce().toString());
                ps.setString(9, transaction.getTo());
                ps.setString(10, transaction.getTransactionIndex().toString());
                ps.setString(11, transaction.getV().toString());
                ps.setString(12, transaction.getValue().toString());
                ps.setString(13, transaction.getTimestamp().toString());
                ps.setString(14, transaction.getCumulativeGasUsed().toString());
                ps.setString(15, transaction.getGasUsed().toString());
                ps.setString(16, transaction.getStatus());
                ps.setString(17, transaction.getRemoved());
                ps.setString(18, transaction.getTopics());
                ps.setString(19, transaction.getRealAddress());
                if (transaction.getRealValue() == null) {
                    ps.setString(20, "");
                } else {
                    ps.setString(20, transaction.getRealValue().toString());
                }

                ps.setInt(21, transaction.getContract());
//                System.out.println("contract:"+transaction.getContract());
                ps.setString(22, transaction.getTokenName());
                ps.setString(23, transaction.getOperating());
                ps.setString(24, transaction.getPwd());
                this.ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } finally {
            this.closeResource();
        }
    }

    public Block queryLastBlock() {
        Block block = new Block();
        try {
            this.conn = this.getConnection();
            this.stat = this.conn.createStatement();
            this.rs = this.stat.executeQuery(
                    "select number,difficulty,gasLimit,gasUsed,hash,miner,nonce,size,timestamp,totalDifficulty from t_block order by number desc limit 0,1");
            while (this.rs.next()) {
                block.setNumber(rs.getInt("number"));
                block.setDifficulty(new BigInteger(rs.getString("difficulty")));
                block.setGasLimit(new BigInteger(rs.getString("gasLimit")));
                block.setGasUsed(new BigInteger(rs.getString("gasUsed")));
                block.setHash(rs.getString("hash"));
                block.setMiner(rs.getString("miner"));
                block.setNonce(new BigInteger(rs.getString("nonce")));
                block.setSize(rs.getInt("size"));
                block.setTimestamp(new BigInteger(rs.getString("timestamp")));
                block.setTotalDifficulty(new BigInteger(rs.getString("totalDifficulty")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResource();
        }
        return block;
    }

    public void insertContract(Contract contract) {
        try {
            final String sql = "insert t_contract(address,creator,hash,name,timestamp,id,type)values(?,?,?,?,?,?,?)";
            this.conn = this.getConnection();
            this.ps = this.conn.prepareStatement(sql);
            this.ps.setString(1, contract.getAddress());
            ps.setString(2, contract.getCreator());
            ps.setString(3, contract.getHash());
            ps.setString(4, contract.getName());
            ps.setString(5, contract.getTimestamp().toString());
            ps.setString(6, contract.getId());
            ps.setInt(7, contract.getType());

            this.ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } finally {
            this.closeResource();
        }
    }

    public Token getTokenByAddress(String address) {
        Token token = new Token();
        try {
            this.conn = this.getConnection();
            this.stat = this.conn.createStatement();
            this.rs = this.stat.executeQuery("select * from t_token where address = '" + address + "'");
            while (this.rs.next()) {
                token.setAddress(rs.getString("address"));
                token.setCreator(rs.getString("creator"));
                token.setDecimals(rs.getInt("decimals"));
                token.setHash(rs.getString("hash"));
                token.setName(rs.getString("name"));
                token.setSymbol(rs.getString("symbol"));
                token.setTotalSupply(rs.getString("totalSupply"));
                token.setTimestamp(new BigInteger(rs.getString("timestamp")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResource();
        }
        return token;
    }

    public TTransactionTokenData getTTransactionTokenDataByTransaction(String transaction, String tokenAddress) {
        TTransactionTokenData tTransactionTokenData = null;
        try {
            this.conn = this.getConnection();
            this.stat = this.conn.createStatement();
            this.rs = this.stat.executeQuery("select * from t_transaction_token_data where transaction = '" + transaction + "' and token_id = '" + tokenAddress + "'");
            if (this.rs.next()) {
                tTransactionTokenData = new TTransactionTokenData();
                tTransactionTokenData.setId(rs.getInt("id"));
                tTransactionTokenData.setTransaction(rs.getString("transaction"));
                tTransactionTokenData.setTokenId(rs.getString("token_id"));
                tTransactionTokenData.setBalance(rs.getString("balance"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResource();
        }
        return tTransactionTokenData;
    }

    public Certification getCertificationsByAddress(String address) {
        Certification c = new Certification();
        try {
            this.conn = this.getConnection();
            this.stat = this.conn.createStatement();
            String a = "select * from t_certification where status = 1 and wallet_address ='" + address;
            this.rs = this.stat.executeQuery("select * from t_certification where status = 1 and wallet_address ='" + address + "'");
            while (this.rs.next()) {
                c.setId(rs.getLong("id"));
                c.setRealName(rs.getString("real_name"));
                c.setType(rs.getInt("type"));
                c.setNumber(rs.getString("number"));
                c.setMobile(rs.getString("mobile"));
                c.setChannelId(rs.getInt("channel_id"));
                c.setWalletAddress(rs.getString("wallet_address"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResource();
        }
        return c;
    }

    public Contract getContractByAddress(String address) {
        Contract c = new Contract();
        try {
            this.conn = this.getConnection();
            this.stat = this.conn.createStatement();
            String a = "select * from t_contract where address ='" + address + "'";
            this.rs = this.stat.executeQuery(a);
            while (this.rs.next()) {
                c.setCreator(rs.getString("creator"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResource();
        }
        return c;
    }


    public List<Certification> getCertifications() {
        List<Certification> c = new ArrayList<Certification>();
        try {
            this.conn = this.getConnection();
            this.stat = this.conn.createStatement();
            this.rs = this.stat.executeQuery("select * from t_certification where status = 2 and type = 1");
            while (this.rs.next()) {
                Certification certification = new Certification();
                certification.setId(rs.getLong("id"));
                certification.setBizToken(rs.getString("biz_token"));
                certification.setWalletAddress(rs.getString("wallet_address"));
                certification.setPublicKey(rs.getString("public_key"));
                certification.setRealName(rs.getString("real_name"));
                certification.setNumber(rs.getString("number"));
                certification.setMobile(rs.getString("mobile"));
                certification.setStatus(rs.getInt("status"));
                certification.setChannelId(rs.getInt("channel_id"));
                c.add(certification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResource();
        }
        return c;
    }

    public List<Certification> getCompanyCertifications() {
        List<Certification> c = new ArrayList<Certification>();
        try {
            this.conn = this.getConnection();
            this.stat = this.conn.createStatement();
            this.rs = this.stat.executeQuery("select * from t_certification where status = 2 and type = 2");
            while (this.rs.next()) {
                Certification certification = new Certification();
                certification.setId(rs.getLong("id"));
                certification.setBizToken(rs.getString("biz_token"));
                certification.setWalletAddress(rs.getString("wallet_address"));
                certification.setPublicKey(rs.getString("public_key"));
                certification.setRealName(rs.getString("real_name"));
                certification.setNumber(rs.getString("number"));
                certification.setMobile(rs.getString("mobile"));
                c.add(certification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResource();
        }
        return c;
    }

    public int updateCertification(Certification certification) {
        int result = 0;
        try {
            final String sql = "update t_certification set real_name = ?,number = ?,update_time = ?,status = ?,is_reward = ? where id = ?";
            this.conn = this.getConnection();
            this.ps = this.conn.prepareStatement(sql);
            this.ps.setString(1, certification.getRealName());
            ps.setString(2, certification.getNumber());
            ps.setString(3, dateFormat.format(new Date()));
            ps.setInt(4, certification.getStatus());
            ps.setInt(5, certification.getIsReward());
            ps.setLong(6, certification.getId());
            result = this.ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResource();
        }
        return result;
    }

    public String getDictByKey(String key) {
        String result = null;
        try {
            final String sql = "select dict_value from t_dict where dict_key = '" + key + "'";
            this.conn = this.getConnection();
            this.stat = this.conn.createStatement();
            this.rs = this.stat.executeQuery(sql);
            while (this.rs.next()) {
                result = rs.getString("dict_value");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResource();
        }
        return result;
    }

    public int insertReward(String name, String number, String walletAddress, BigInteger amount) {
        int count = 0;
        try {
            final String sql = "insert t_reward(name,number,address,amount)values(?,?,?,?)";
            this.conn = this.getConnection();
            this.ps = this.conn.prepareStatement(sql);
            this.ps.setString(1, name);
            ps.setString(2, number);
            ps.setString(3, walletAddress);
            ps.setString(4, amount.toString());
            count = this.ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeResource();
        }
        return count;
    }

    public Reward queryReward(String number) {
        Reward reward = null;

        try {
            final String sql = "select * from t_reward where number = '" + number + "'";
            this.conn = this.getConnection();
            this.stat = this.conn.createStatement();
            this.rs = this.stat.executeQuery(sql);
            while (this.rs.next()) {
                reward = new Reward();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResource();
        }
        return reward;
    }

    public DatePackage queryDatePackage(String address) {
        DatePackage datePackage = null;

        try {
            final String sql = "select * from t_date_package where address = '" + address + "'";
            this.conn = this.getConnection();
            this.stat = this.conn.createStatement();
            this.rs = this.stat.executeQuery(sql);
            while (this.rs.next()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                datePackage = new DatePackage();
                datePackage.setAddress(rs.getString("address"));
                datePackage.setBeginDate(dateFormat.parse(rs.getString("begin_date")));
                datePackage.setEndDate(dateFormat.parse(rs.getString("end_date")));
                datePackage.setPackageId(rs.getLong("package_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResource();
        }
        return datePackage;
    }

    public List<ContractCertification> getContractCertificationsByCertificationId(Long CertificationId) {

        List<ContractCertification> c = new ArrayList<>();
        try {
            this.conn = this.getConnection();
            this.stat = this.conn.createStatement();
            this.rs = this.stat.executeQuery("select * from t_contract_certification where pwd='public' and certification_id = '" + CertificationId + "'");
            while (this.rs.next()) {
                ContractCertification contractCertification = new ContractCertification();
                contractCertification.setId(rs.getLong("id"));
                contractCertification.setAddress(rs.getString("address"));
                contractCertification.setCertificationId(rs.getLong("certification_id"));
                contractCertification.setCertIdentity(rs.getInt("cert_identity"));
                contractCertification.setPwd(rs.getString("pwd"));
                contractCertification.setStatus(rs.getInt("status"));
                contractCertification.setUpdateTime(rs.getString("update_time"));
                c.add(contractCertification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResource();
        }
        return c;
    }

    public List<CodePwd> getCodePwdsByContractAddressAndMsgIsNotNull(String contractAddress) {

        List<CodePwd> c = new ArrayList<>();
        try {
            this.conn = this.getConnection();
            this.stat = this.conn.createStatement();
            this.rs = this.stat.executeQuery("select * from t_code_pwd where  msg is not NULL and contract_address = '" + contractAddress + "'");
            while (this.rs.next()) {
                CodePwd codePwd = new CodePwd();
                codePwd.setCodeId(rs.getLong("code_id"));
                codePwd.setContractAddress(rs.getString("contract_address"));
                codePwd.setCode(rs.getString("code"));
                codePwd.setMsg(rs.getString("msg"));
                codePwd.setCreateTime(dateFormat.parse(rs.getString("create_time")));
                c.add(codePwd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResource();
        }
        return c;
    }

    public int insertContractCertification(ContractCertification contractCertification) {
        int count = 0;
        try {
            String sql = "insert t_contract_certification(address,certification_id,cert_identity,status,pwd,update_time)values(?,?,?,?,?,?)";
            this.conn = this.getConnection();
            this.ps = this.conn.prepareStatement(sql);
            this.ps.setString(1, contractCertification.getAddress());
            ps.setLong(2, contractCertification.getCertificationId());
            ps.setInt(3, contractCertification.getCertIdentity());
            ps.setInt(4, contractCertification.getStatus());
            ps.setString(5, contractCertification.getPwd());
            ps.setString(6, dateFormat.format(new Date()));
            count = this.ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeResource();
        }
        return count;
    }

    public int updateContractCertification(ContractCertification contractCertification) {
        int count = 0;
        try {
            String sql = "update t_contract_certification set status = ?,update_time= ?,pwd = ? where certification_id = ? and address = ?";
            this.conn = this.getConnection();
            this.ps = this.conn.prepareStatement(sql);
            this.ps.setInt(1, contractCertification.getStatus());
            ps.setString(2, dateFormat.format(new Date()));
            ps.setString(3, contractCertification.getPwd());
            ps.setLong(4, contractCertification.getCertificationId());
            ps.setString(5, contractCertification.getAddress());

            count = this.ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeResource();
        }
        return count;
    }

    public int insertAuthorization(Long fromId, Long toId, String address, String pwd) {
        int count = 0;
        try {
            String sql = "insert t_authorization(address,from_id,to_id,pwd,create_time)values(?,?,?,?,?)";
            this.conn = this.getConnection();
            this.ps = this.conn.prepareStatement(sql);
            this.ps.setString(1, address);
            ps.setLong(2, fromId);
            ps.setLong(3, toId);
            ps.setString(4, pwd);
            ps.setString(5, dateFormat.format(new Date()));
            count = this.ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeResource();
        }
        return count;
    }

    public static void main(String[] args) {


    }

}
