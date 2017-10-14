package com.vshcxl.common.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

//@XmlRootElement(name="staff") 
@XStreamAlias("staff")
public class Staff {

//	@XStreamAlias(value = "name")
	private String name; // 职员名称
//	@XStreamAlias("age")
	private int age; // 职员年龄
//	@XStreamAlias("smoker")
	private boolean smoker; // 是否为烟民

	public String getName() {
		return name;
	}

	// @XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	// @XmlElement
	public void setAge(int age) {
		this.age = age;
	}

	public boolean getSmoker() {
		return smoker;
	}

	// @XmlAttribute
	public void setSmoker(boolean smoker) {
		this.smoker = smoker;
	}

}
