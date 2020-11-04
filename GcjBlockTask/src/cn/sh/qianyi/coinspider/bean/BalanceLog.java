package cn.sh.qianyi.coinspider.bean;

import java.io.Serializable;

/**
 * @author
 */
public class BalanceLog implements Serializable {
	private Integer id;

	public BalanceLog() {
	};

	public BalanceLog(String tokeid, String transaction, String beforeBalance, String afterBalance) {
		super();
		this.tokenId = tokeid;
		this.transaction = transaction;
		this.beforeBalance = beforeBalance;
		this.afterBalance = afterBalance;
	}

	/**
	 * 钱包地址
	 */
	private String transaction;

	/**
	 * 操作前的余额
	 */
	private String beforeBalance;

	/**
	 * 操作后的余额
	 */
	private String afterBalance;

	/**
	 * 代币或者主币id
	 */
	private String tokenId;

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

	public String getBeforeBalance() {
		return beforeBalance;
	}

	public void setBeforeBalance(String beforeBalance) {
		this.beforeBalance = beforeBalance;
	}

	public String getAfterBalance() {
		return afterBalance;
	}

	public void setAfterBalance(String afterBalance) {
		this.afterBalance = afterBalance;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

}