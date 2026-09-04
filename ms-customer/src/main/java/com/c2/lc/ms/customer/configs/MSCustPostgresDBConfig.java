package com.c2.lc.ms.customer.configs;

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

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration

@EnableJpaRepositories(basePackages = {"com.c2.lc.ms.customer.repos.customer"},
        entityManagerFactoryRef = "custPostgresEntityManager", transactionManagerRef = "custPostgresTransactionManager")
public class    MSCustPostgresDBConfig {

    @Autowired private Environment env;
    @Value("${cust.postgres.datasource.hikari.poolName}") private String poolName;
    @Value("${postgres.datasource.hikari.minimumIdle}") private int minimumIdle;
    @Value("${postgres.datasource.hikari.idleTimeout}") private long idleTimeout;
    @Value("${postgres.datasource.hikari.maxLifetime}") private long maxLifetime;
    @Value("${postgres.datasource.hikari.maximumPoolSize}") private int maximumPoolSize;
    @Value("${postgres.datasource.hikari.connectionTimeout}") private long connectionTimeout;

    @Bean
    @Primary
    public DataSource custPostgreDataSource() {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(env.getProperty("cust.postgres.datasource.driverClassName"));
        hikariConfig.setJdbcUrl(env.getProperty("cust.postgres.datasource.url"));
        hikariConfig.setUsername(env.getProperty("cust.postgres.datasource.username"));
        hikariConfig.setPassword(env.getProperty("cust.postgres.datasource.password"));
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setPoolName(poolName);
        hikariConfig.setIdleTimeout(idleTimeout);
        hikariConfig.setMaxLifetime(maxLifetime);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setConnectionTimeout(connectionTimeout);

        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "custPostgresEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean custPostgresEntityManager() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(custPostgreDataSource());
        em.setPackagesToScan(new String[]{"com.c2.lc.ms.customer.entities.customer"});
        em.setPersistenceUnitName("postgresql");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("Spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("postgres.hibernate.dialect"));
        properties.put("hibernate.jdbc.lob.non_contextual_creation", env.getProperty("hibernate.jdbc.lob.non_contextual_creation"));
        em.setJpaPropertyMap(properties);
        return em;
    }



    @Bean(name = "custPostgresTransactionManager")
    @Primary
    public PlatformTransactionManager custPostgresTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(custPostgresEntityManager().getObject());
        return transactionManager;
    }
}