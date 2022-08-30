package com.codowang.pensieve.core.sql.entity;

/**
 * Elastic 系列之SQL命令处理参数实现类，不允许继承。
 *
 * @author lvhonglun
 */
public final class SqlParameter {

	// The name of the parameter, if any
	private String name;

	// The value of the parameter, if any
	private Object value;

	/**
	 * 字段对应的表，或者表的别名
	 */
	private String table;

	/**
	 * 实际字段名
	 */
	private String field;

	// The index of the parameter, is digital
	private int index;

	public SqlParameter() {
		
	}

	public SqlParameter(int index, Object value) {
		this.setIndex(index);
		this.setValue(value);
	}

	public SqlParameter(int index, Object value, String name) {
		this.setIndex(index);
		this.setValue(value);
		this.setName(name);
	}

	public SqlParameter(int index, Object value, String name, String table) {
		this.setIndex(index);
		this.setValue(value);
		this.setTable(table);
		this.setName(name);
	}

	public SqlParameter(int index, Object value, String name, String table, String field) {
		this.setIndex(index);
		this.setValue(value);
		this.setTable(table);
		this.setName(name);
		this.setField(field);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	@Override
	public String toString() {
		return "SqlParameter [name=" + name + ", value=" + value + ", index=" + index + "]";
	}
}
