package com.c2.lc.ms.master.configurations;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
@EnableJpaRepositories(
        entityManagerFactoryRef = "sybaseEntityManager",
        transactionManagerRef = "sybaseTransactionManager",
        basePackages = "com.c2.lc.ms.master.repos.sybase")

public class SybaseDBConfig {

    @Autowired
    private Environment env;
    @Value("${sybase.datasource.hikari.poolName}") private String poolName;
    @Value("${sybase.datasource.hikari.minimumIdle}") private int minimumIdle;
    @Value("${sybase.datasource.hikari.idleTimeout}") private long idleTimeout;
    @Value("${sybase.datasource.hikari.maxLifetime}") private long maxLifetime;
    @Value("${sybase.datasource.hikari.maximumPoolSize}") private int maximumPoolSize;
    @Value("${sybase.datasource.hikari.connectionTimeout}") private long connectionTimeout;

    @Bean
    public DataSource sybaseDataSource() {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(env.getProperty("sybase.datasource.url"));
        hikariConfig.setUsername(env.getProperty("sybase.datasource.username"));
        hikariConfig.setPassword(env.getProperty("sybase.datasource.password"));
        hikariConfig.setDriverClassName(env.getProperty("sybase.datasource.driverClassName"));
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setPoolName(poolName);
        hikariConfig.setIdleTimeout(idleTimeout);
        hikariConfig.setMaxLifetime(maxLifetime);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setConnectionTimeout(connectionTimeout);

        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "sybaseEntityManager")
    @PersistenceContext(unitName = "sybase")
    public LocalContainerEntityManagerFactoryBean sybaseEntityManager() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(sybaseDataSource());
        em.setPackagesToScan("com.c2.lc.ms.master.entities.sybase");
        em.setPersistenceUnitName("sybase");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("sybase.hibernate.dialect"));
        properties.put("hibernate.jdbc.lob.non_contextual_creation", env.getProperty("hibernate.jdbc.lob.non_contextual_creation"));
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean
    public PlatformTransactionManager sybaseTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(sybaseEntityManager().getObject());
        return transactionManager;
    }


}
