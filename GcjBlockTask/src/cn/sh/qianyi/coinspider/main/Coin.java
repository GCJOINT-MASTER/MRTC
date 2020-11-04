package cn.sh.qianyi.coinspider.main;

import java.math.BigDecimal;
import java.util.Date;

public class Coin {

    private Long id;
    
    private String name;
    
    private String title;
    
    private String img;
    
    private String type;
    
    private String wallet;
    
    private Integer round;
    
    private Integer status;
    
    private Double autoOut;
    
    private String address;
    
    
    public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**
     * 手续费率
     */
    private Double rate;
    
    private BigDecimal minFeeNum;


    private BigDecimal baseAmount;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;


    private BigDecimal dayMaxAmount;

    private Integer withdrawFlag;
    
    private Integer rechargeFlag;
    
    private Date lastUpdateTime;
    
    private Date created;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWallet() {
		return wallet;
	}

	public void setWallet(String wallet) {
		this.wallet = wallet;
	}

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getAutoOut() {
		return autoOut;
	}

	public void setAutoOut(Double autoOut) {
		this.autoOut = autoOut;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public BigDecimal getMinFeeNum() {
		return minFeeNum;
	}

	public void setMinFeeNum(BigDecimal minFeeNum) {
		this.minFeeNum = minFeeNum;
	}

	public BigDecimal getBaseAmount() {
		return baseAmount;
	}

	public void setBaseAmount(BigDecimal baseAmount) {
		this.baseAmount = baseAmount;
	}

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public BigDecimal getDayMaxAmount() {
		return dayMaxAmount;
	}

	public void setDayMaxAmount(BigDecimal dayMaxAmount) {
		this.dayMaxAmount = dayMaxAmount;
	}

	public Integer getWithdrawFlag() {
		return withdrawFlag;
	}

	public void setWithdrawFlag(Integer withdrawFlag) {
		this.withdrawFlag = withdrawFlag;
	}

	public Integer getRechargeFlag() {
		return rechargeFlag;
	}

	public void setRechargeFlag(Integer rechargeFlag) {
		this.rechargeFlag = rechargeFlag;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}


}
