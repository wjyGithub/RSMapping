package com.wjy.rsmapping.serializer;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.wjy.rsmapping.table.Table;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Set;

/**
 * Created by jianyuan.wei@hand-china.com
 * on 2019/4/23 15:54.
 */
@Component
public class TableJsonSerializer extends JsonSerializer<Table> {


    public void serialize(Table table, JsonGenerator generator, SerializerProvider provider)
            throws IOException{
        ObjectMapper om = new ObjectMapper();
        Set<Table.TableEntry> set = table.entrySet();
        if(set != null && !set.isEmpty()) {
            JSONObject tableJson = new JSONObject();
            for(Table.TableEntry tableEntry : set) {
                tableJson.put(table.getKeyAlias(),tableEntry.getKey());
                tableJson.put(table.getMidAlias(),tableEntry.getMid());
                tableJson.put(table.getValueAlias(),tableEntry.getValue());
            }
            om.writeValue(generator,tableJson);
        }
    }
}
