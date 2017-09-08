package com.snowstone.commons.kern.apiext.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

//@XmlRootElement(name="department") 
@XStreamAlias("department")
public class Department {
//	@XStreamAlias("name")
	private String name;    //部门名称
//	@XStreamAlias("staffs")
    private List<Staff> staffs;           // 其实staff是单复同型，这里是加's'是为了区别staff  
      
    public String getName() {  
        return name;  
    }  
//    @XmlAttribute  
    public void setName(String name) {  
        this.name = name;  
    }  
    public List<Staff> getStaffs() {  
        return staffs;  
    }  
//    @XmlElement(name="staff")  
    public void setStaffs(List<Staff> staffs) {  
        this.staffs = staffs;  
    }  
}
