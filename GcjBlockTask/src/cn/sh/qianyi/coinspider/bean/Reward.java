package cn.sh.qianyi.coinspider.bean;

import java.io.IOException;
import java.math.BigInteger;

import org.gcj3j.protocol.Gcj3j;
import org.gcj3j.protocol.core.DefaultBlockParameterName;
import org.gcj3j.protocol.core.methods.response.EthGetTransactionCount;

public class Reward {
	private Integer id;
	private String number;
	private String name;
	private String amount;
	private String address;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	

}
