package org.vxwo.springboot.experience.mybatis;

import java.util.*;
import java.util.concurrent.locks.*;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.io.ResolverUtil;
import org.vxwo.springboot.experience.mybatis.annotations.GeneralTable;
import org.vxwo.springboot.experience.mybatis.sql.*;

/**
 * @author vxwo-team
 */

public class GeneralTableRegistrar {
    private final static Map<String, TableEntity> CACHE = new HashMap<>();
    private final static ReadWriteLock CACHE_LOCK = new ReentrantReadWriteLock();

    public static TableEntity findTable(Class<?> type) {
        TableEntity table = null;
        String typeName = type.getName();

        Lock readLock = CACHE_LOCK.readLock();
        readLock.lock();
        table = CACHE.get(typeName);
        readLock.unlock();

        if (table == null) {
            throw new BuilderException("Not found config for class: " + typeName);
        }

        return table;
    }

    private static TableEntity addTableConfig(Class<?> type) {
        TableEntity table = null;
        String typeName = type.getName();

        Lock writeLock = CACHE_LOCK.writeLock();
        writeLock.lock();
        try {
            table = CACHE.get(typeName);
            if (table == null) {
                table = TableParser.parseTable(type, GeneralSqlHelper.isCamelCaseToUnderscore());
                CACHE.put(typeName, table);
            }
        } finally {
            writeLock.unlock();
        }
        return table;
    }

    public static void registerTablesInPackage(String packageName) {
        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<>();
        resolverUtil.findAnnotated(GeneralTable.class, packageName);
        for (Class<?> type : resolverUtil.getClasses()) {
            addTableConfig(type);
        }
    }
}
