package com.maoyan.bigdata.datalink.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


@Configuration
@MapperScan(basePackages = {"com.maoyan.bigdata.datalink.dao.movie_data"}, sqlSessionFactoryRef = "sqlSessionFactoryMovieData",sqlSessionTemplateRef = "sqlSessionTemplateMovieData")
public class MovieDataDSConfig {

    @Autowired
    @Qualifier("movie-data")
    private DataSource movieData;


    @Bean
    public SqlSessionFactory sqlSessionFactoryMovieData() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(movieData); // 使用titan数据源, 连接titan库
        return factoryBean.getObject();

    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplateMovieData() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactoryMovieData()); // 使用上面配置的Factory
        return template;
    }

    @Bean
    public JdbcTemplate jdbcTemplateMovieData() throws Exception {
        JdbcTemplate jdbcTemplate=new JdbcTemplate(movieData);
        return jdbcTemplate;
    }
}
