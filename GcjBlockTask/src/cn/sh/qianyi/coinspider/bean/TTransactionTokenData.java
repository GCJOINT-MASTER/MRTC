package cn.sh.qianyi.coinspider.bean;

import java.io.Serializable;

/**
 * @author
 */
public class TTransactionTokenData implements Serializable {

	public TTransactionTokenData() {
	}

	public TTransactionTokenData(String transaction, String tokenId, String balance, String oldBalance) {
		super();
		this.transaction = transaction;
		this.tokenId = tokenId;
		this.balance = balance;
		this.oldBalance = oldBalance;
	}

	private Integer id;

	/**
	 * 用户钱包地址
	 */
	private String transaction;

	/**
	 * 代币或者主币id
	 */
	private String tokenId;

	/**
	 * 代币或者主币余额
	 */
	private String balance;

	private String oldBalance;

	public String getOldBalance() {
		return oldBalance;
	}

	public void setOldBalance(String oldBalance) {
		this.oldBalance = oldBalance;
	}

	private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}
}