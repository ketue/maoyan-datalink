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
@MapperScan(basePackages = {"com.maoyan.bigdata.datalink.dao.movie_bi"}, sqlSessionFactoryRef = "sqlSessionFactoryMovieBi",sqlSessionTemplateRef = "sqlSessionTemplateMovieBi")
public class MovieBiDSConfig {

    @Autowired
    @Qualifier("movie-bi")
    private DataSource movieBi;


    @Bean
    public SqlSessionFactory sqlSessionFactoryMovieBi() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(movieBi); // 使用titan数据源, 连接titan库
        return factoryBean.getObject();

    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplateMovieBi() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactoryMovieBi()); // 使用上面配置的Factory
        return template;
    }
}
