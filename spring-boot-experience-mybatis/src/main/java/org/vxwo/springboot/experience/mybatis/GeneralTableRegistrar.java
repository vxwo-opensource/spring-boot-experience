package org.vxwo.springboot.experience.mybatis;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.io.ResolverUtil;
import org.vxwo.springboot.experience.mybatis.annotations.GeneralTable;
import org.vxwo.springboot.experience.mybatis.sql.*;
import org.vxwo.springboot.experience.util.lang.Tuple2;

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

    private static Tuple2<String, String> addTableConfig(Class<?> type) {
        String typeName = type.getName();

        TableEntity table = TABLE_CACHE.get(typeName);
        if (table != null) {
            return null;
        }

        table = TableParser.parseTable(type,
                GeneralSqlHelper.getConfiguration().isMapUnderscoreToCamelCase());
        TABLE_CACHE.put(typeName, table);

        return Tuple2.of(table.getName(), typeName);
    }

    public static List<Tuple2<String, String>> registerTablesInPackage(String packageName) {
        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<>();
        resolverUtil.findAnnotated(GeneralTable.class, packageName);

        List<Tuple2<String, String>> result = new ArrayList<>();
        for (Class<?> type : resolverUtil.getClasses()) {
            Tuple2<String, String> tablePair = addTableConfig(type);
            if (tablePair != null) {
                result.add(tablePair);
            }
        }
        return result;
    }
}
