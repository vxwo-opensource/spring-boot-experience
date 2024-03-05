package org.vxwo.springboot.experience.mybatis;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.io.ResolverUtil;
import org.vxwo.springboot.experience.mybatis.annotations.GeneralTable;
import org.vxwo.springboot.experience.mybatis.sql.*;

/**
 * @author vxwo-team
 */

public class GeneralTableRegistrar {
    private final static Map<String, TableEntity> TABLE_CACHE = new ConcurrentHashMap<>();

    public static TableEntity findTable(Class<?> type) {
        String typeName = type.getName();

        TableEntity table = TABLE_CACHE.get(typeName);
        if (table == null) {
            throw new BuilderException("Not found `GeneralTable` for class: " + typeName);
        }

        return table;
    }

    private static void addTableConfig(Class<?> type) {
        String typeName = type.getName();

        TableEntity table = TABLE_CACHE.get(typeName);
        if (table != null) {
            return;
        }

        TABLE_CACHE.put(typeName,
                TableParser.parseTable(type, GeneralSqlHelper.isCamelCaseToUnderscore()));
    }

    public static void registerTablesInPackage(String packageName) {
        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<>();
        resolverUtil.findAnnotated(GeneralTable.class, packageName);

        for (Class<?> type : resolverUtil.getClasses()) {
            addTableConfig(type);
        }
    }
}
