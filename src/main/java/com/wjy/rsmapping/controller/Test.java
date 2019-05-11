package com.wjy.rsmapping.controller;

import com.wjy.rsmapping.mapper.ThingTestMapper;
import com.wjy.rsmapping.table.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jianyuan.wei@hand-china.com
 * on 2019/4/21 0:25.
 */
@RestController
@RequestMapping("test")
public class Test {

    @Autowired
    private ThingTestMapper thingTest;

    @GetMapping("/query")
    public Table<Long,String,String> test(){
        Table<Long,String,String> table = thingTest.queryInfo(null);
        Table.TableEntry tableEntry = table.get(727L);
        Object name = table.getColumnByAlias(727L, "name");
        return table;
    }
}
