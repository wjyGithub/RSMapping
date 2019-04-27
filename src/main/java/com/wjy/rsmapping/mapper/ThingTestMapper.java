package com.wjy.rsmapping.mapper;

import com.wjy.rsmapping.annotation.RSMap;
import com.wjy.rsmapping.table.Table;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.omg.CORBA.OBJ_ADAPTER;

import java.util.List;
import java.util.Map;

/**
 * Created by jianyuan.wei@hand-china.com
 * on 2019/4/21 0:11.
 */
@Mapper
public interface ThingTestMapper {

    @RSMap
    List<Table<Long,String,String>> queryInfo(List<String> strList);
}
