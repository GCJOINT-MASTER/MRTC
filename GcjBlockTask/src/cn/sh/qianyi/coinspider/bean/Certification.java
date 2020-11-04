package cn.sh.qianyi.coinspider.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:实名信息实体类
 * @author: corwin  
 * @date: 2018年9月7日 下午12:26:53 
 */
public class Certification implements Serializable {
	
	private static final long serialVersionUID = -7532276919201445190L;

	/**
     * 主键ID
     */
    private Long id;
    
    /**
     * 类型 
     * 1.个人 2.企业
     *     
     */
    private Integer type;

    
    /**
     * 真实名称
     */
    private String realName;
    
    /**
     * 身份证号或者统一信用代码
     */
    private String number;
    
    /**
     * 电话号码 (个人)
     */
    private String mobile;
    
    /**
     * 联系人姓名(企业)
     */
    private String contactName;
    
    /**
     * 1.成功 2.失败
     */
    private Integer status;
    
    /**
     * 腾讯人脸识别标识
     */
    private String bizToken;
    
    /**
     * 公钥
     */
    private String publicKey;
    
    
    /**
     * 地址
     */
    private String walletAddress;
   
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 编辑时间
     */
    private Date updateTime;
    
    
    /**
     * 渠道Id
     */
    private Integer channelId;
    
    
    
    

    /**
     * 是否赠送
     */
    private Integer isReward = 0;
    
    
    

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Integer getIsReward() {
		return isReward;
	}

	public void setIsReward(Integer isReward) {
		this.isReward = isReward;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getWalletAddress() {
		return walletAddress;
	}

	public void setWalletAddress(String walletAddress) {
		this.walletAddress = walletAddress;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getBizToken() {
		return bizToken;
	}

	public void setBizToken(String bizToken) {
		this.bizToken = bizToken;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

    

}
