package org.litespring.beans;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ConstructorArgument {
	
	private final List<ValueHolder> argumentValues = new LinkedList<ValueHolder>();
	
	public ConstructorArgument() {}
	
	public List<ValueHolder> getArgumentValues() {
		return Collections.unmodifiableList(this.argumentValues);
	}
	
	public void addArgumetValue(ValueHolder value) {
		this.argumentValues.add(value);
	}
	
	public int getArgumentCount() {
		return this.argumentValues.size();
	}
	
	public boolean isEmpty() {
		return this.argumentValues.isEmpty();
	}
	
	public void clear() {
		this.argumentValues.clear();
	}
	
	/**
	 * @author MAQUN
	 * @version V1.0 
	 * @Date 2019-04-02 09:12:59
	 * 此类只在ConstructorArgument中使用,因此使用内部类,达到高内聚的效果
	 */
	public static class ValueHolder {
		
		private Object value;
		
		private String type;
		
		private String name;
		
		public ValueHolder(Object value) {
			this.value = value;
		}

		public ValueHolder(Object value, String type) {
			this.value = value;
			this.type = type;
		}

		public ValueHolder(Object value, String type, String name) {
			this.value = value;
			this.type = type;
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Object getValue() {
			return this.value;
		}
		
		public void setValue(Object value) {
			this.value = value;
		}
		
	}

}
