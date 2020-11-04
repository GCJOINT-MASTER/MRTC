package cn.sh.qianyi.coinspider.bean;

import java.math.BigInteger;

import org.apache.solr.client.solrj.beans.Field;

public class Block {
	@Field
	private String extraData;
	
	@Field
	private Integer number;
	
	
	@Field
	private BigInteger difficulty;
	
	@Field
	private BigInteger gasLimit;
	
	@Field
	private BigInteger gasUsed;
	
	@Field
	private String hash;
	
	@Field
	private String miner;
	
	@Field
	private BigInteger nonce;
	
	@Field
	private Integer size;
	
	@Field
	private BigInteger timestamp;
	
	@Field
	private BigInteger  totalDifficulty;
	
	@Field
	private String  parentHash;
	
	@Field
	private String  receiptsRoot;
	
	@Field
	private String  sha3Uncles;
	
	@Field
	private Integer txs;
	
	public String getExtraData() {
		return extraData;
	}

	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}

	public String getParentHash() {
		return parentHash;
	}

	public void setParentHash(String parentHash) {
		this.parentHash = parentHash;
	}

	public String getReceiptsRoot() {
		return receiptsRoot;
	}

	public void setReceiptsRoot(String receiptsRoot) {
		this.receiptsRoot = receiptsRoot;
	}

	public String getSha3Uncles() {
		return sha3Uncles;
	}

	public void setSha3Uncles(String sha3Uncles) {
		this.sha3Uncles = sha3Uncles;
	}

	public Integer getTxs() {
		return txs;
	}

	public void setTxs(Integer txs) {
		this.txs = txs;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public BigInteger getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigInteger difficulty) {
		this.difficulty = difficulty;
	}

	public BigInteger getGasLimit() {
		return gasLimit;
	}

	public void setGasLimit(BigInteger gasLimit) {
		this.gasLimit = gasLimit;
	}

	public BigInteger getGasUsed() {
		return gasUsed;
	}

	public void setGasUsed(BigInteger gasUsed) {
		this.gasUsed = gasUsed;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getMiner() {
		return miner;
	}

	public void setMiner(String miner) {
		this.miner = miner;
	}

	public BigInteger getNonce() {
		return nonce;
	}

	public void setNonce(BigInteger nonce) {
		this.nonce = nonce;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}


	public BigInteger getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(BigInteger timestamp) {
		this.timestamp = timestamp;
	}

	public BigInteger getTotalDifficulty() {
		return totalDifficulty;
	}

	public void setTotalDifficulty(BigInteger totalDifficulty) {
		this.totalDifficulty = totalDifficulty;
	}
	
	
	
}
