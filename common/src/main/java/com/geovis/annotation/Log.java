package com.geovis.annotation;

import java.lang.annotation.*;

/**
 * @Target@Target 用于描述注解的使用范围，
 * 其取值有描述CONSTRUCTOR用于描述构造器，FIELD用于描述域，
 * LOCAL_VARIABLE用于描述局部变量METHOD用于描述方法
 * PACKAGE用于描述包。PARAMETER用于描述参数。
 * TYPE用于描述类或接口（甚至 enum ）。
 * 
 * @Documented 在默认的情况下javadoc命令不会将我们的annotation生成再doc中去的，
 * 所以使用该标记就是告诉jdk让它也将annotation生成到doc中去
 * 
 * @Inherited 比如有一个类A，在他上面有一个标记annotation，
 * 那么A的子类B是否不用再次标记annotation就可以继承得到呢，答案是肯定的
 * 
 * @Retention 用于描述注解的生命周期（即：被描述的注解在什么范围内有效），其取值有：
 * SOURCE在源文件中有效（即源文件保留）。
 * CLASS在 class 文件中有效（即 class 保留）。
 * RUNTIME在运行时有效（即运行时保留）
 * 
 * 自定义日志注解
 * @author songgm
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Log {
	/**
	 * 方法
	 */
	String method()default "";
	/**
	 * 中文名称
	 */
	String name();
	/**
	 * 路径
	 */
	String path();
	/**
	 * 备注
	 */
	String mark()default "";
}
