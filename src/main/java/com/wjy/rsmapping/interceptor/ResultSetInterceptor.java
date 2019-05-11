package com.wjy.rsmapping.interceptor;

import com.wjy.rsmapping.annotation.RSMap;
import com.wjy.rsmapping.constant.ClassMapEnum;
import com.wjy.rsmapping.table.Table;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 结果集拦截
 * Created by jianyuan.wei@hand-china.com
 * on 2019/4/20 23:36.
 */
@Intercepts(@Signature(method = "handleResultSets",type= ResultSetHandler.class,args = {Statement.class}))
@Component
public class ResultSetInterceptor implements Interceptor {
    
    private static final Logger log = LoggerFactory.getLogger(ResultSetInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MetaObject metaObject = SystemMetaObject.forObject(invocation.getTarget());
        MappedStatement mappedStatement = (MappedStatement)metaObject.getValue("mappedStatement");
        // 当前类
        String className = StringUtils.substringBeforeLast(mappedStatement.getId(), ".");
        // 当前方法
        String currentMethodName = StringUtils.substringAfterLast(mappedStatement.getId(), ".");
        // 获取当前Method
        Method currentMethod = findMethod(className, currentMethodName);

        if (currentMethod == null || currentMethod.getAnnotation(RSMap.class) == null) {// 如果当前Method没有注解MapM2M
            return invocation.proceed();
        }
        Type genericType = currentMethod.getGenericReturnType();
        Type[] types = null;
        if(genericType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType)genericType;
            types = pt.getActualTypeArguments();
        }
        System.out.println(genericType.getTypeName());
        RSMap rsMap = currentMethod.getAnnotation(RSMap.class);
        Statement statement = (Statement) invocation.getArgs()[0];
        TypeHandlerRegistry thr = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        return  resultHandle(statement,thr,rsMap,types);
    }

    /**
     * 结果集处理
     * @param statement
     * @param thr
     * @param rsMap
     * @return
     */
    private Object resultHandle(Statement statement, TypeHandlerRegistry thr, RSMap rsMap,Type[] types) throws SQLException {
        ResultSet resultSet = statement.getResultSet();
        List<Table<Object,Object,Object>> tables = new ArrayList<>();
        Table<Object,Object,Object> table = new Table<>();
        if(rsMap.num() == 3) {
            while (resultSet.next()) {
                ResultSetMetaData rsmd = resultSet.getMetaData();
                table.setKeyAlias(columnNameFormat(rsmd.getColumnName(1)));
                table.setMidAlias(columnNameFormat(rsmd.getColumnName(2)));
                table.setValueAlias(columnNameFormat(rsmd.getColumnName(3)));
                Object key = this.getObject(resultSet, 1, thr, getTypeByIndex(types,0));
                Object mid = this.getObject(resultSet, 2, thr, getTypeByIndex(types,1));
                Object value = this.getObject(resultSet, 3, thr, getTypeByIndex(types,2));
                table.put(key, mid, value);
            }
        }
        tables.add(table);
        System.out.println(tables);
        return tables;
    }

    /**
     *
     * @param type
     * @param i
     * @return
     */
    public Class<?> getTypeByIndex(Type[] type,int i) {
        if(type != null && type.length == 3) {
            return ClassMapEnum.getClassMapEnum(type[i].getTypeName()).getClazz();
        }
        return ClassMapEnum.getClassMapEnum("default").getClazz();
    }


    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 格式化列名,将其都转为小写,并除去第一个单词,每个单词首字母大写
     * @param columnName
     * @return
     */
    private String columnNameFormat(String columnName) {
        String lowerColumnName = columnName.toLowerCase();
        String[] allWords = lowerColumnName.split("_");
        StringBuilder sb = new StringBuilder();
        for(String word : allWords) {
            String s = firstCharUpper(word);
            sb.append(s);
        }
        if(sb.length() != 0) {
            String s = firstCharLower(sb.toString());
            return s;
        }
        return null;
    }

    /**
     * 将首字母转成大写
     * @param word
     * @return
     */
    private String firstCharUpper(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    /**
     * 将首字母转为小写
     * @param word
     * @return
     */
    private String firstCharLower(String word) {
        return word.substring(0, 1).toLowerCase() + word.substring(1);
    }

    /**
     * 找到与指定函数名匹配的Method。
     *
     * @param className
     * @param targetMethodName
     * @return
     * @throws Throwable
     */
    private Method findMethod(String className, String targetMethodName) throws Throwable {
        Method[] methods = Class.forName(className).getDeclaredMethods();// 该类所有声明的方法
        if (methods == null) {
            return null;
        }

        for (Method method : methods) {
            if (StringUtils.equals(method.getName(), targetMethodName)) {
                return method;
            }
        }

        return null;
    }

    /**
     * 结果类型转换。
     * <p>
     * 这里借用注册在MyBatis的typeHander（包括自定义的），方便进行类型转换。
     *
     * @param resultSet
     * @param columnIndex 字段下标，从1开始
     * @param typeHandlerRegistry MyBatis里typeHandler的注册器，方便转换成用户指定的结果类型
     * @param javaType 要转换的Java类型
     * @return
     * @throws SQLException
     */
    private Object getObject(ResultSet resultSet, int columnIndex, TypeHandlerRegistry typeHandlerRegistry,
                             Class<?> javaType) throws SQLException {
        final TypeHandler<?> typeHandler = typeHandlerRegistry.hasTypeHandler(javaType)
                ? typeHandlerRegistry.getTypeHandler(javaType) : typeHandlerRegistry.getUnknownTypeHandler();

        return typeHandler.getResult(resultSet, columnIndex);

    }
}
