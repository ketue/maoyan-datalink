package com.maoyan.bigdata.datalink.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@Configuration
@MapperScan(basePackages = {"com.maoyan.bigdata.datalink.dao.movie_realdata"}, sqlSessionFactoryRef = "sqlSessionFactoryMovieRealData")
public class MovieRealDataDSConfig {

    @Autowired
    @Qualifier("movie-realdata")
    private DataSource movieRealData;


    @Bean
    public SqlSessionFactory sqlSessionFactoryMovieRealData() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(movieRealData);

        return factoryBean.getObject();

    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplateMovieRealData() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactoryMovieRealData()); // 使用上面配置的Factory
        return template;
    }
}
