package com.maoyan.bigdata.datalink.datasource.dynamic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaoyangyang on 2018/12/5
 */
@Configuration
public class DynamicDataSourceConfig {

    @Autowired
    @Qualifier("movie-data")
    private DataSource movieData;
    @Autowired
    @Qualifier("movie-mis")
    private DataSource movieMis;
    @Autowired
    @Qualifier("movie-bi")
    private DataSource movieBi;
    @Autowired
    @Qualifier("movie-realdata")
    private DataSource movieRealData;
    @Autowired
    @Qualifier("kylin-movie-app")
    private DataSource kylinMovieApp;

    @Bean
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource ds=new DynamicDataSource();
        Map<Object,Object> dsMap=new HashMap<>();
        dsMap.put("mysql-movieData",movieData);
        dsMap.put("mysql-movieMis",movieMis);
        dsMap.put("mysql-movieBi",movieBi);
        dsMap.put("mysql-movieRealData",movieRealData);
        dsMap.put("kylin-movie-app",kylinMovieApp);
        ds.setTargetDataSources(dsMap);
        ds.setDefaultTargetDataSource(movieData);
        return ds;
    }

    @Bean
    public JdbcTemplate dynamicJdbcTemplate(@Qualifier("dynamicDataSource") DynamicDataSource dynamicDataSource ){
        JdbcTemplate jdbcTemplate=new JdbcTemplate();
        jdbcTemplate.setDataSource(dynamicDataSource);
        return jdbcTemplate;
    }
}
