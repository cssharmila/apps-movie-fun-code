package org.superbiz.moviefun;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Bean
    public DataSource albumsDataSource(@Value ("${moviefun.datasources.albums.url}") String url,
                                       @Value ("${moviefun.datasources.albums.username}") String username,
                                       @Value ("${moviefun.datasources.albums.password}") String password){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDataSource(getDataSource(url, username, password));
        return new HikariDataSource(hikariConfig);
    }



    @Bean
    public DataSource moviesDataSource(@Value("${moviefun.datasources.movies.url}") String url,
                                       @Value("${moviefun.datasources.movies.username}") String username,
                                       @Value("${moviefun.datasources.movies.password}") String password){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDataSource(getDataSource(url, username, password));
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public HibernateJpaVendorAdapter hibernateJpaVendorAdapter(){
        HibernateJpaVendorAdapter hibernateAdapter = new HibernateJpaVendorAdapter();
        hibernateAdapter.setDatabase(Database.MYSQL);
        hibernateAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        hibernateAdapter.setGenerateDdl(true);
        return hibernateAdapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean albumsEntityFactory(@Autowired DataSource albumsDataSource,
                                                                      @Autowired HibernateJpaVendorAdapter hibernateJpaVendorAdapter){
        return getEntityManager(albumsDataSource,hibernateJpaVendorAdapter,"albums-entity-factory");
    }

    private LocalContainerEntityManagerFactoryBean getEntityManager(DataSource dataSource, HibernateJpaVendorAdapter hibernateJpaVendorAdapter,
                                                                    String unitName) {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(dataSource);
        entityManager.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        entityManager.setPackagesToScan(this.getClass().getPackage().getName());
        entityManager.setPersistenceUnitName(unitName);
        return entityManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean moviesEntityFactory(@Autowired DataSource moviesDataSource,
                                                                      @Autowired HibernateJpaVendorAdapter hibernateJpaVendorAdapter){
        return getEntityManager(moviesDataSource,hibernateJpaVendorAdapter,"movies-entity-factory");
    }

    @Bean
    public PlatformTransactionManager moviesTransactionManager(@Qualifier("moviesEntityFactory") EntityManagerFactory moviesEntityFactory){
        return new JpaTransactionManager(moviesEntityFactory);
    }

    @Bean
    public PlatformTransactionManager albumsTransactionManager(@Qualifier("albumsEntityFactory") EntityManagerFactory albumsEntityFactory){
        return new JpaTransactionManager(albumsEntityFactory);
    }

    private DataSource getDataSource(String url, String username, String password) {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .build();
    }
}
