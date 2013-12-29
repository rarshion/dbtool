package com.ablesky.dbtool.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware {

	private static ApplicationContext context;
	private static final Properties EMPTY_PROPERTIES = new Properties();
	public static final String DEFAULT_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	public static final String DEFAULT_DATA_SOURCE_BEAN_NAME_PREFIX = "DB_";

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext() {
		return context;
	}
	
	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}
	
	public static <T> T getBean(Class<T> clazz) {
		Map<?,T> beanMap = context.getBeansOfType(clazz);
		if(beanMap == null || beanMap.size() == 0) {
			return null;
		}
		return beanMap.values().iterator().next();
	}
	
	public static DataSource addDataSourceBean(String address, String username, String password) {
		String beanName = getDefaultDataSourceBeanNameByAddress(address);
		String url = buildDefaultDataSourceUrlByAddress(address);
		return addDataSourceBean(beanName, url, username, password);
	}
	
	public static DataSource addDataSourceBean(String beanName, String url, String username, String password) {
		return addDataSourceBean(beanName, DEFAULT_DRIVER_CLASS_NAME, url, username, password);
	}
	
	public static DataSource addDataSourceBean(String beanName, String driverClassName, String url, String username, String password) {
		return addDataSourceBean(beanName, driverClassName, url, username, password, EMPTY_PROPERTIES);
	}
	
	public static DataSource getDataSourceBeanByAddress(String address) {
		return (DataSource) getBean(getDefaultDataSourceBeanNameByAddress(address));
	}
	
	public static DataSource addDataSourceBean(String beanName, String driverClassName, String url, String username, String password, Properties properties) {
		ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) context;
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
		if(beanFactory.containsBean(beanName)) {
			Object bean = getBean(beanName);
			return bean instanceof DataSource? (DataSource) bean: null;
		}
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(org.apache.commons.dbcp.BasicDataSource.class);
		builder.addPropertyValue("driverClassName", driverClassName);
		builder.addPropertyValue("url", url);
		builder.addPropertyValue("username", username);
		builder.addPropertyValue("password", password);
		for(Entry<Object, Object> entry: properties.entrySet()) {
			builder.addPropertyValue(entry.getKey().toString(), entry.getValue().toString());
		}
		builder.setDestroyMethodName("close");
		beanFactory.registerBeanDefinition(beanName, builder.getBeanDefinition());
		return (DataSource) getBean(beanName);
	}
	
	private static String buildDefaultDataSourceUrlByAddress(String address) {
		return "jdbc:mysql://" + address + ":3306/information_schema";
	}
	
	public static String getDefaultDataSourceBeanNameByAddress(String address) {
		return DEFAULT_DATA_SOURCE_BEAN_NAME_PREFIX + address;
	}
	
	
}
