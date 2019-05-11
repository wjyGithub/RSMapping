package com.wjy.rsmapping.mapper;

import com.wjy.rsmapping.annotation.RSMap;
import com.wjy.rsmapping.table.Table;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Created by jianyuan.wei@hand-china.com
 * on 2019/4/21 0:11.
 */
@Mapper
@Repository
public interface ThingTestMapper {

    @RSMap
    Table<Long,String,String> queryInfo(List<String> strList);
}
