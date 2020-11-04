package cn.sh.qianyi.coinspider.bean;

public class ContractCertification {
    public Long id;
    public String address;
    public Long certificationId;
    public Integer certIdentity;

    public Integer status;
    public String updateTime;

    public String pwd;


    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(Long certificationId) {
        this.certificationId = certificationId;
    }

    public Integer getCertIdentity() {
        return certIdentity;
    }

    public void setCertIdentity(Integer certIdentity) {
        this.certIdentity = certIdentity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }


}
