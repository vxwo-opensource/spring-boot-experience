package org.vxwo.springboot.experience.mybatis.sql;

import java.util.*;

/**
 * @author vxwo-team
 */

public final class TableEntity {
    private String name;
    private ColumnEntity idColumn;
    private List<ColumnEntity> otherColumns;

    private TableEntity(String name, ColumnEntity idColumn, List<ColumnEntity> otherColumns) {
        this.name = name;
        this.idColumn = idColumn;
        this.otherColumns = Collections.unmodifiableList(otherColumns);
    }

    public String getName() {
        return name;
    }

    public ColumnEntity getIdColumn() {
        return idColumn;
    }

    public List<ColumnEntity> getOtherColumns() {
        return otherColumns;
    }

    public static TableEntity of(String tableName, ColumnEntity idColumn,
            List<ColumnEntity> otherColumns) {
        return new TableEntity(tableName, idColumn, otherColumns);
    }

}
