package org.vxwo.springboot.experience.mybatis.sql;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.springframework.util.StringUtils;
import org.vxwo.springboot.experience.mybatis.annotations.*;
import org.vxwo.springboot.experience.util.lang.Tuple2;

/**
 * @author vxwo-team
 */

public final class TableParser {
    private static String camelToUnderline(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = value.length(); i < len; i++) {
            char ch = value.charAt(i);
            if (Character.isUpperCase(ch) && i > 0) {
                sb.append("_");
            }
            sb.append(Character.toLowerCase(ch));
        }
        return sb.toString();
    }

    private static String parseGetterName(String fieldName, Class<?> type) {
        char firstCh = fieldName.charAt(0);
        String methodPrefix = type.isInstance(Boolean.class) ? "is" : "get";
        if (Character.isUpperCase(firstCh)) {
            return methodPrefix + fieldName;
        }
        return methodPrefix + Character.toUpperCase(firstCh) + fieldName.substring(1);
    }

    private static Tuple2<List<Field>, Map<String, Method>> parseFieldAndMethods(
            Class<?> cursorType) {
        Set<String> fieldNames = new HashSet<>();
        Set<String> methodNames = new HashSet<>();
        List<Field> fieldList = new ArrayList<>();
        Map<String, Method> methodMap = new HashMap<>(50);
        while (cursorType != Object.class) {
            Arrays.stream(cursorType.getDeclaredFields())
                    .filter(field -> !fieldNames.contains(field.getName()))
                    .collect(Collectors.toList()).forEach(field -> {
                        fieldNames.add(field.getName());
                        fieldList.add(field);
                    });

            Arrays.stream(cursorType.getMethods())
                    .filter(method -> !methodNames.contains(method.getName()))
                    .collect(Collectors.toList()).forEach(method -> {
                        methodNames.add(method.getName());
                        methodMap.put(method.getName(), method);
                    });

            cursorType = cursorType.getSuperclass();
        }

        return Tuple2.of(fieldList, methodMap);
    }

    public static TableEntity parseTable(Class<?> type, boolean camelCaseToUnderscore) {
        String typeName = type.getName();
        GeneralTable tableAnnotation = type.getAnnotation(GeneralTable.class);
        if (tableAnnotation == null) {
            throw new ReflectionException(
                    "Not found annotation '@GeneralTable' on class: " + typeName);

        }

        String tableName = tableAnnotation.value();
        if (!StringUtils.hasText(tableName)) {
            throw new ReflectionException(
                    "Invalid table name in annotation '@GeneralTable' on class: " + typeName);
        }

        Tuple2<List<Field>, Map<String, Method>> fieldAndMehtods = parseFieldAndMethods(type);

        int idAnnotationCount = 0;
        ColumnEntity idByName = null, idByAnnotation = null;
        List<ColumnEntity> columns = new ArrayList<>();
        for (Field field : fieldAndMehtods.getT1()) {
            boolean foundIdAnnotation = false;
            GeneralId idAnnotation = field.getAnnotation(GeneralId.class);
            if (idAnnotation != null) {
                ++idAnnotationCount;
                foundIdAnnotation = true;
            }

            String fieldName = field.getName();
            Method getterMethod =
                    fieldAndMehtods.getT2().get(parseGetterName(fieldName, field.getType()));
            if (getterMethod == null) {
                if (foundIdAnnotation) {
                    throw new ReflectionException("Not found 'Getter' with field {" + fieldName
                            + "} in annotation '@GeneralId' on class: " + typeName);
                }

                continue;
            }

            String typeHandler = null;
            GeneralField fieldAnnotation = field.getAnnotation(GeneralField.class);
            if (fieldAnnotation != null) {
                if (!fieldAnnotation.typeHandler().equals(UnknownTypeHandler.class)) {
                    typeHandler = fieldAnnotation.typeHandler().getName();
                }
            }

            ColumnEntity column =
                    ColumnEntity.of(camelCaseToUnderscore ? camelToUnderline(fieldName) : fieldName,
                            fieldName, getterMethod, typeHandler);

            if (foundIdAnnotation) {
                idByAnnotation = column;
            } else if ("id".equals(fieldName)) {
                idByName = column;
            }
            columns.add(column);
        }

        if (idAnnotationCount > 1) {
            throw new ReflectionException(
                    "Mutiple annotation '@GeneralId' fields on class: " + typeName);
        } else if (idAnnotationCount < 1 && idByName == null) {
            throw new ReflectionException(
                    "Not found 'id' field or none annotation by '@GeneralId' on class: "
                            + typeName);
        }

        ColumnEntity idColumn = idByName != null ? idByName : idByAnnotation;
        return TableEntity.of(tableName, idColumn,
                columns.stream().filter(o -> o != idColumn).collect(Collectors.toList()));
    }
}
