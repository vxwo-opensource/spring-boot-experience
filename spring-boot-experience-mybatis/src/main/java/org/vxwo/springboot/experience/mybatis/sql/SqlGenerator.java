package org.vxwo.springboot.experience.mybatis.sql;

import java.util.*;
import org.apache.ibatis.builder.BuilderException;

/**
 * @author vxwo-team
 */

public final class SqlGenerator {
    private static boolean existsValue(ColumnEntity column, Object value) {
        return column.getFieldValue(value) != null;
    }

    private static String renderColumnSet(SqlRender render, ColumnEntity column) {
        return render.reserved(column.getName()) + "=" + render.property(column.getFieldName());
    }

    public static String insertOne(SqlRender render, TableEntity table, Object value) {
        List<String> columns = new ArrayList<>();
        List<String> properties = new ArrayList<>();

        ColumnEntity idColumn = table.getIdColumn();
        if (existsValue(idColumn, value)) {
            columns.add(render.reserved(idColumn.getName()));
            properties.add(render.property(idColumn.getFieldName()));
        }

        table.getOtherColumns().forEach(column -> {
            if (existsValue(column, value)) {
                columns.add(render.reserved(column.getName()));
                properties.add(render.property(column.getFieldName()));
            }
        });
        if (columns.isEmpty()) {
            throw new BuilderException("Insert statements must have at least one column");
        }

        return "INSERT INTO " + render.reserved(table.getName()) + " (" + String.join(", ", columns)
                + ") VALUES (" + String.join(", ", properties) + ")";
    }

    public static String updateOneById(SqlRender render, TableEntity table, Object value) {
        ColumnEntity idColumn = table.getIdColumn();
        if (!existsValue(idColumn, value)) {
            throw new BuilderException("Update statements must have value for column 'id'");
        }

        List<String> columnSets = new ArrayList<>();
        table.getOtherColumns().forEach(column -> {
            if (existsValue(column, value)) {
                columnSets.add(renderColumnSet(render, column));
            }
        });
        if (columnSets.isEmpty()) {
            throw new BuilderException("Update statements must have at least one set phrase");
        }

        return "UPDATE " + render.reserved(table.getName()) + " SET "
                + String.join(", ", columnSets) + " WHERE " + renderColumnSet(render, idColumn);
    }

    public static String selectByColumn(SqlRender render, TableEntity table, Object value) {
        List<String> columnSets = new ArrayList<>();
        ColumnEntity idColumn = table.getIdColumn();
        if (existsValue(idColumn, value)) {
            columnSets.add(renderColumnSet(render, idColumn));
        }
        table.getOtherColumns().forEach(column -> {
            if (existsValue(column, value)) {
                columnSets.add(renderColumnSet(render, column));
            }
        });
        if (columnSets.isEmpty()) {
            throw new BuilderException("Select statements must have at least one query expression");
        }

        return "SELECT * FROM " + render.reserved(table.getName()) + " WHERE "
                + String.join(" AND ", columnSets);
    }

    public static String deleteByColumn(SqlRender render, TableEntity table, Object value) {
        List<String> columnSets = new ArrayList<>();
        ColumnEntity idColumn = table.getIdColumn();
        if (existsValue(idColumn, value)) {
            columnSets.add(renderColumnSet(render, idColumn));
        }
        table.getOtherColumns().forEach(column -> {
            if (existsValue(column, value)) {
                columnSets.add(renderColumnSet(render, column));
            }
        });
        if (columnSets.isEmpty()) {
            throw new BuilderException("Delete statements must have at least one query expression");
        }

        return "DELETE FROM " + render.reserved(table.getName()) + " WHERE "
                + String.join(" AND ", columnSets);
    }
}
