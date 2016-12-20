/**
 * create by zhangpu
 * date:2015年3月18日
 */
package com.acooly.core.demo.demo;

import com.acooly.core.common.domain.AbstractEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * @author zhangpu
 *
 */
@Entity
@Table(name = "acooly_demo", schema = "", indexes = { @Index(name = "uk_acooly_demo", unique = true,
		columnList = "code") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AcoolyDemo extends AbstractEntity {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 758041138818825730L;
	
	/** 编码 */
	@Column(name = "code", length = 16, nullable = false, columnDefinition = "varchar(16)  not null comment '编码'")
	private String code;
	
	/** 名称 */
	@Column(name = "code", length = 32, nullable = false, columnDefinition = "varchar(32)  not null comment '名称'")
	private String name;
	
	/** 年龄 */
	@Column(name = "age", nullable = false, columnDefinition = "int  not null comment '年龄'")
	private int age;
	/** 生日 */
	@Column(name = "birthday", columnDefinition = "datetime NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '生日'")
	private Date birthday = new Date();
	
	/** 类型 */
	@Column(name = "demo_type", length = 16, nullable = false, columnDefinition = "varchar(16) not null comment'类型'")
	private DemoType demoType;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public Date getBirthday() {
		return birthday;
	}
	
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	@Enumerated(EnumType.STRING)
	public DemoType getDemoType() {
		return demoType;
	}
	
	public void setDemoType(DemoType demoType) {
		this.demoType = demoType;
	}
	
	@Override
	public String toString() {
		return String.format("AcoolyDemo: {code:%s, name:%s, age:%s, birthday:%s, demoType:%s}", code, name, age,
			birthday, demoType);
	}
	
	public static enum DemoType {
		
		Demo1("Demo1", "案例1"),
		
		Demo2("Demo2", "案例2");
		
		private String key;
		private String value;
		
		private DemoType(String key, String value) {
			this.key = key;
			this.value = value;
		}
		
		public String getKey() {
			return key;
		}
		
		public String getValue() {
			return value;
		}
		
	}
	
}
