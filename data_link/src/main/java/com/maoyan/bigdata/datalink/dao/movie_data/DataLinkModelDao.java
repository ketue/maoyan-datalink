package com.maoyan.bigdata.datalink.dao.movie_data;

import com.maoyan.bigdata.datalink.core.DataLinkModelBean;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface DataLinkModelDao {

    @Select("SELECT name,model,crane_cron,crane_job_id " +
            "FROM t_data_link_model " +
            "WHERE name = #{name} ")
    @Results({
            @Result(property = "key",column = "name"),
            @Result(property = "model",column = "model"),
            @Result(property = "craneCron",column = "crane_cron"),
            @Result(property = "craneJobId",column = "crane_job_id")
    })
    DataLinkModelBean getDataLinkModelByKey(@Param("name") String name);



    @Select("SELECT name,model,crane_cron,crane_job_id " +
            "FROM t_data_link_model ")
    @Results({
            @Result(property = "key",column = "name"),
            @Result(property = "model",column = "model"),
            @Result(property = "craneCron",column = "crane_cron"),
            @Result(property = "craneJobId",column = "crane_job_id")
    })
    List<DataLinkModelBean> getDataLinkModel();


    @Update("update t_data_link_model " +
            "set model=#{bean.model}," +
            "crane_cron=#{bean.craneCron}, " +
            "crane_job_id=#{bean.craneJobId} " +
            "where name=#{bean.key} ")
    @Results({
            @Result(property = "key",column = "name"),
            @Result(property = "model",column = "model"),
            @Result(property = "craneCron",column = "crane_cron"),
            @Result(property = "craneJobId",column = "crane_job_id")
    })
    int updateDataLinkModel(@Param("bean") DataLinkModelBean bean);
}
