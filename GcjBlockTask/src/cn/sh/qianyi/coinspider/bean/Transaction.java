package cn.sh.qianyi.coinspider.bean;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Transaction {

	
	
	private String blockHash;
	
	private Integer blockNumber;
	
	private String from;
	
	private BigInteger gas;
	
	private BigInteger gasPrice;
	
	private String hash;
	
	private String input;
	
	private BigInteger nonce;
	
	private String to;
	
	
	private Integer transactionIndex;
	
	private Integer v;
	
	private BigInteger value;
	
	private BigInteger timestamp;
	
	//ʵ��ʹ��gas
	private BigInteger gasUsed;
	//״̬
	private String status;
	
	private BigInteger cumulativeGasUsed;
	
	private String removed;
	
	private String topics;
	
	//���ܺ�Լ
	private Integer contract = 0; 

	private String realAddress;
	
	private BigDecimal realValue;
	
	private String tokenName;
	
	
	private String operating;
	
	private String pwd;
	
	
	
	
	
	

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getOperating() {
		return operating;
	}

	public void setOperating(String operating) {
		this.operating = operating;
	}

	public Integer getContract() {
		return contract;
	}

	public void setContract(Integer contract) {
		this.contract = contract;
	}

	public BigDecimal getRealValue() {
		return realValue;
	}

	public void setRealValue(BigDecimal realValue) {
		this.realValue = realValue;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	public String getRealAddress() {
		return realAddress;
	}

	public void setRealAddress(String realAddress) {
		this.realAddress = realAddress;
	}

	public BigInteger getGasUsed() {
		return gasUsed;
	}

	public void setGasUsed(BigInteger gasUsed) {
		this.gasUsed = gasUsed;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigInteger getCumulativeGasUsed() {
		return cumulativeGasUsed;
	}

	public void setCumulativeGasUsed(BigInteger cumulativeGasUsed) {
		this.cumulativeGasUsed = cumulativeGasUsed;
	}
	

	public String getRemoved() {
		return removed;
	}

	public void setRemoved(String removed) {
		this.removed = removed;
	}

	public String getTopics() {
		return topics;
	}

	public void setTopics(String topics) {
		this.topics = topics;
	}

	

	public BigInteger getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(BigInteger timestamp) {
		this.timestamp = timestamp;
	}

	public String getBlockHash() {
		return blockHash;
	}

	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}

	public Integer getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(Integer blockNumber) {
		this.blockNumber = blockNumber;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public BigInteger getGas() {
		return gas;
	}

	public void setGas(BigInteger gas) {
		this.gas = gas;
	}

	public BigInteger getGasPrice() {
		return gasPrice;
	}

	public void setGasPrice(BigInteger gasPrice) {
		this.gasPrice = gasPrice;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public BigInteger getNonce() {
		return nonce;
	}

	public void setNonce(BigInteger nonce) {
		this.nonce = nonce;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Integer getTransactionIndex() {
		return transactionIndex;
	}

	public void setTransactionIndex(Integer transactionIndex) {
		this.transactionIndex = transactionIndex;
	}

	public Integer getV() {
		return v;
	}

	public void setV(Integer v) {
		this.v = v;
	}

	public BigInteger getValue() {
		return value;
	}

	public void setValue(BigInteger value) {
		this.value = value;
	}
	
	
	
	
}
