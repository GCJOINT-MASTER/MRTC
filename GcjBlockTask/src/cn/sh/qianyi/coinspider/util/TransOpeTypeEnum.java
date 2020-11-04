package cn.sh.qianyi.coinspider.util;



/**
 * Description: 交易操作类型枚举类
 * @author: ydd  
 * @date: 2019年12月27日 下午2:56:44 
 */
public enum TransOpeTypeEnum {
	
	CONTRAGCJ_CREATE("1", "合同创建"),
	CONTRAGCJ_SIGN("2","合同签署"),
	CONTRAGCJ_SUPPLY("3","合同补充"),
	SUPPLY_SIGN("4","补充签署"),
	CONTRAGCJ_AUTH("5","合同授权");
	
	public String status;
	public String name;
	
	TransOpeTypeEnum(String status, String name) {
		this.status = status;
		this.name = name;
	}
	

}
