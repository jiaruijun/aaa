package com.overcloud.pay.vo;


public class UserTypeInfo {
	
	//用户类型表
	private String id;
	private String userId;
	private String type;
	private String typeName;
	private String remark;
	private String createTime;
	private String updateTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getTypeName() {
		if(type.equals("1")){
			setTypeName("白名单");
		}else if(type.equals("2")){
			setTypeName("黑名单");
		}else if(type.equals("3")){
			setTypeName("测试用户");
		}else if(type.equals("4")){
			setTypeName("超级用户");
		}
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	

}
