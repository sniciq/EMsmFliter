package com.eddy.emsmfliter.db;

/**
 * 过滤器对象
 * @author Eddy
 *
 */
public class FliterEty {
	
	public static final int type_number = 0;//号码
	public static final int type_content = 1;//内容
	public final static String[] Type_Array = {"号码", "内容"};
	
	private Integer id;
	private Integer type;
	private String filterInfo;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public static int convertTypeStr(String typeStr) {
		if(typeStr.equalsIgnoreCase("号码")) {
			return type_number;
		}
		else if(typeStr.equalsIgnoreCase("内容")) {
			return type_content;
		}
		else {
			return -1;
		}
	}
	
	public static String convertType(int type) {
		if(type == type_number) {
			return "号码";
		}
		else if(type == type_content) {
			return "内容";
		}
		else {
			return "";
		}
	}
	
	public String getTypeStr() {
		return convertType(this.type);
	}
	
	/**
	 * 0: 号码，1：内容
	 * @return
	 */
	public Integer getType() {
		return type;
	}
	
	/**
	 * 0: 号码，1：内容
	 * @param type
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	public String getFilterInfo() {
		return filterInfo;
	}
	public void setFilterInfo(String filterInfo) {
		this.filterInfo = filterInfo;
	}
}
