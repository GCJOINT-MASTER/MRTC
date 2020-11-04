package cn.sh.qianyi.coinspider.bean;

import java.io.IOException;
import java.math.BigInteger;

import org.gcj3j.protocol.Gcj3j;
import org.gcj3j.protocol.core.DefaultBlockParameterName;
import org.gcj3j.protocol.core.methods.response.EthGetTransactionCount;

public class Address {
	public String address;
	public String privateKey;
	public BigInteger nonce;
	
	
	
	public Address(Gcj3j gcj3j,String address,String privateKey) {
		super();
		try {
			EthGetTransactionCount ethGetTransactionCount = gcj3j
					.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
			nonce = ethGetTransactionCount.getTransactionCount();
			this.address = address;
			this.privateKey = privateKey;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public BigInteger getNonce() {
		return nonce;
	}
	public void setNonce(BigInteger nonce) {
		this.nonce = nonce;
	}
	
	

}
