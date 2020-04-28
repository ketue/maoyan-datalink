package com.maoyan.bigdata.datalink.datasource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {


    @Bean(name = "movie-mis")
    @ConfigurationProperties(prefix = "spring.datasource.movie-mis") // application.properteis中对应属性的前缀
    public DataSource movieMis() {
        DataSourceBuilder.create();
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "movie-bi")
    @ConfigurationProperties(prefix = "spring.datasource.movie-bi") // application.properteis中对应属性的前缀
    public DataSource movieBi() {
        DataSourceBuilder.create();
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "movie-data")
    @ConfigurationProperties(prefix = "spring.datasource.movie-data") // application.properteis中对应属性的前缀
    public DataSource movieData() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "movie-realdata")
    @ConfigurationProperties(prefix = "spring.datasource.movie-realdata") // application.properteis中对应属性的前缀
    public DataSource movieRealData() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "kylin-movie-app")
    @ConfigurationProperties(prefix = "spring.datasource.kylin.movie-app") // application.properteis中对应属性的前缀
    public DataSource kylinMovieApp() {
        return DataSourceBuilder.create().type(BasicDataSource.class).build();
    }


}
