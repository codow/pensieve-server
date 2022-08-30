package com.codowang.pensieve.core.sql.entity;

public final class BaseResetData {

	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static BaseResetData instance(Object data) {
		return BaseResetData.instance(data, "success");
	}

	public static BaseResetData instance(Object data, String status) {
		BaseResetData BaseResetData = new BaseResetData();
		BaseResetData.setData(data);
		BaseResetData.setStatus(status);
		return BaseResetData;
	}
}
