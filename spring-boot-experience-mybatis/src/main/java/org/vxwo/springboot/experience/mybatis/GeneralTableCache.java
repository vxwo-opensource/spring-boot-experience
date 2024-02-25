package org.vxwo.springboot.experience.mybatis;

import java.util.*;
import java.util.concurrent.locks.*;
import org.vxwo.springboot.experience.mybatis.sql.*;

/**
 * @author vxwo-team
 */

public class GeneralTableCache {
    private final static Map<String, TableEntity> CACHE = new HashMap<>();
    private final static ReadWriteLock CACHE_LOCK = new ReentrantReadWriteLock();

    public static TableEntity findTable(Class<?> type) {
        TableEntity table = null;
        String typeName = type.getName();

        Lock readLock = CACHE_LOCK.readLock();
        readLock.lock();
        table = CACHE.get(typeName);
        readLock.unlock();
        if (table != null) {
            return table;
        }

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
}
