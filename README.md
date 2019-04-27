# RSMapping

该项目主要的要实现的功能如下:
将数据库查询出来的小于4个字段的数据无需新建实体类接收,直接通过Table数据结构进行接收并操作。
```text
用法:
UserMapper.xml
<select id="queryUser" resultType="com.wjy.rsmapping.table.Table">
    SELECT NAME,AGE,SEX FROM USER
</select>

UserMapper.java
@Mapper
public interface UserMapper {

    @RSMap
    List<Table<String,String,String>> queryUser();
}

如上所示:
@RSMap注解: 用于标识该返回值需要映射到Table数据结构上
映射规则: NAME映射到第一列,AGE映射到第二列,SEX映射到第三列

Table提供了一系列操作数据的相关方法，可供开发者方便操作数据
```


