package com.study.spring.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages="com.study.spring")
@PropertySource("classpath:mysql.properties")
public class SpringRestAppConfig implements WebMvcConfigurer {

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(getProperty("jdbc.driver"));
            dataSource.setJdbcUrl(getProperty("jdbc.url"));
            dataSource.setUser(getProperty("jdbc.user"));
            dataSource.setPassword(getProperty("jdbc.password"));

            //Config for pool size
            dataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
            dataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
            dataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));
            dataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));

        } catch(Exception exception) {
            throw new RuntimeException(exception);
        }
        return dataSource;
    }

    private String getProperty(String property) {
        return environment.getProperty(property);
    }

    private Integer getIntProperty(String property) {
        return Integer.parseInt(environment.getProperty(property));
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(){

        // create session factorys
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

        // set the properties
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(environment.getProperty("hibernate.packagesToScan"));
        sessionFactory.setHibernateProperties(getHibernateProperties());

        return sessionFactory;
    }

    private Properties getHibernateProperties() {

        // set hibernate properties
        Properties props = new Properties();

        props.setProperty("hibernate.dialect", environment.getProperty("hibernate.dialect"));
        props.setProperty("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));

        return props;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {

        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);

        return txManager;
    }
}
