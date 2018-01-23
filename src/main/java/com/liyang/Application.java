package com.liyang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.jpa.query.expression.support.QueryExpressionRepositoryFactoryBean;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = QueryExpressionRepositoryFactoryBean.class)
public class Application {
	
	protected final static Logger logger = LoggerFactory.getLogger(Application.class); 
	

    public static void main(String[] args) {
    	
        SpringApplication.run(Application.class, args);
    }
    
    
    /**
     * 设置内置tomcat属性值比如端口
     * @return
     */
//    @Bean
//    public EmbeddedServletContainerFactory servletContainer(){
//    	TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
//    	factory.setPort(80);
//    	return factory;
//    }

}