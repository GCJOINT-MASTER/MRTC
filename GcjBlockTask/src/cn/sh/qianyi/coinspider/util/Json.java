package cn.sh.qianyi.coinspider.util;


/**
 * @此类为实现ajax局部刷新使用.
 * @author apple
 * */
public class Json {
	
	private boolean flag;
	
	private Object obj;
	
	private Object sale;
	
	private Object middle;
	
	private Object arrgument;
	
	private String msg;

	private String name;
	
	private String size;
	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getSale() {
		return sale;
	}

	public void setSale(Object sale) {
		this.sale = sale;
	}

	public Object getMiddle() {
		return middle;
	}

	public void setMiddle(Object middle) {
		this.middle = middle;
	}

	public Object getArrgument() {
		return arrgument;
	}

	public void setArrgument(Object arrgument) {
		this.arrgument = arrgument;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	

}
