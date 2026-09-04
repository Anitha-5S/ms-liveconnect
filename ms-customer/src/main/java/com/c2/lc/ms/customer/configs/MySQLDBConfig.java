package com.c2.lc.ms.customer.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration

@EnableJpaRepositories(basePackages = {"com.c2.lc.ms.customer.repos.seller"},
        entityManagerFactoryRef = "sellerMySQLEntityManager", transactionManagerRef = "sellerMySQLTransactionManager")
public class MySQLDBConfig{

    @Autowired private Environment env;
    @Value("${seller.mysql.datasource.hikari.poolName}") private String poolName;
    @Value("${seller.spring.datasource.hikari.mysql.minimumIdle}") private int minimumIdle;
    @Value("${seller.spring.datasource.hikari.mysql.idleTimeout}") private long idleTimeout;
    @Value("${seller.spring.datasource.hikari.mysql.maxLifetime}") private long maxLifetime;
    @Value("${seller.spring.datasource.hikari.mysql.maximumPoolSize}") private int maximumPoolSize;
    @Value("${seller.spring.datasource.hikari.mysql.connectionTimeout}") private long connectionTimeout;

    @Bean
    public DataSource sellerMySQLDataSource() {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(env.getProperty("seller.spring.datasource.mysql.driverClassName"));
        hikariConfig.setJdbcUrl(env.getProperty("seller.spring.datasource.mysql.url"));
        hikariConfig.setUsername(env.getProperty("seller.spring.datasource.mysql.username"));
        hikariConfig.setPassword(env.getProperty("seller.spring.datasource.mysql.password"));
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setPoolName(poolName);
        hikariConfig.setIdleTimeout(idleTimeout);
        hikariConfig.setMaxLifetime(maxLifetime);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setConnectionTimeout(connectionTimeout);

        return new HikariDataSource(hikariConfig);
    }

    @PersistenceContext(unitName = "mysql")
    @Bean(name = "sellerMySQLEntityManager")
    public LocalContainerEntityManagerFactoryBean sellerMySQLEntityManager() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(sellerMySQLDataSource());
        em.setPackagesToScan(new String[]{"com.c2.lc.ms.customer.entities.seller"});
        em.setPersistenceUnitName("mysql");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("Spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.mysql.hibernate.dialect"));
        properties.put("hibernate.jdbc.lob.non_contextual_creation", env.getProperty("hibernate.jdbc.lob.non_contextual_creation"));
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean(name = "sellerMySQLTransactionManager")
    public PlatformTransactionManager sellerMySQLTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(sellerMySQLEntityManager().getObject());
        return transactionManager;
    }
}
