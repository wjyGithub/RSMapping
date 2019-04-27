package com.wjy.rsmapping.controller;

import com.wjy.rsmapping.mapper.ThingTestMapper;
import com.wjy.rsmapping.table.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<Table<Long,String,String>> test(){
        List<Table<Long, String, String>> tables = thingTest.queryInfo(null);
        for(Table<Long,String,String> table :tables) {
            Object code = table.getColumnByAlias(581L,"code");
            Object thingId = table.getColumnByAlias(581L,"thingId");
            Object name = table.getColumnByAlias(581L, "name");
            System.out.println(name);
            System.out.println(code);
            System.out.println(thingId);
        }
        return tables;
    }
}
