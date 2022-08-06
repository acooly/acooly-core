/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-03 09:25
 */
package com.acooly.core.utils.ie;

import com.acooly.core.utils.Reflections;
import com.acooly.core.utils.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.OrderComparator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 根据@Annotation解析实体导出数据
 *
 * @author zhangpu
 * @date 2022-08-03 09:25
 */
@Slf4j
public class Exports {

    /**
     * 解析导出元数据
     *
     * @param clazz
     * @return
     */
    public static ExportResult parse(Class<?> clazz) {
        ExportModel exportModel = clazz.getAnnotation(ExportModel.class);
        if (exportModel == null) {
            return null;
        }
        ExportResult result = new ExportResult();
        result.setFileName(exportModel.fileName());

        // Temporary data container<name,ExportItem>
        Map<String, ExportItem> itemMap = Maps.newHashMap();

        // 获取对象继承树类列表写入sources列表（子类在前）
        List<Class> sources = Lists.newArrayList();
        Class<?> cc = clazz;
        while (cc != null && cc != Object.class) {
            sources.add(cc);
            cc = cc.getSuperclass();
        }
        // 倒置sources（基类在前）
        Collections.reverse(sources);

        // 根据@ExportModel.ignores配置忽略属性
        List<String> ignores = Lists.newArrayList(exportModel.ignores());

        for (Class<?> cls : sources) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                // @ExportColumn标识的才做解析
                if (!field.isAnnotationPresent(ExportColumn.class)) {
                    continue;
                }
                // 根据配置忽略
                if (ignores.contains(field.getName())) {
                    continue;
                }
                ExportColumn exportColumn = field.getAnnotation(ExportColumn.class);
                itemMap.put(field.getName(), newExportItem(exportColumn, field.getName(), null));
            }
        }

        // 根据@ExportModel.exportColumns配置添加导入列属性
        for (ExportColumn exportColumn : exportModel.exportColumns()) {
            // 根据配置忽略
            if (Strings.isBlank(exportColumn.name()) || ignores.contains(exportColumn.name())) {
                continue;
            }
            itemMap.put(exportColumn.name(), newExportItem(exportColumn, null, null));
        }

        // Ordered排序
        List<ExportItem> exportItems = Lists.newArrayList(itemMap.values());
        OrderComparator.sort(exportItems);

        // 设置值
        result.setItems(exportItems);
        List<String> titles = Lists.newArrayList();
        for (ExportItem item : exportItems) {
            titles.add(item.getTitle());
        }
        result.setTitles(titles);
        return result;
    }

    /**
     * 获取单个实体对象的导出结果
     *
     * @param object
     * @return
     */
    public static ExportResult parse(Object object) {
        ExportResult result = parse(object.getClass());
        return parse(result, object);
    }

    public static ExportResult parse(ExportResult result, Object object) {
        List<Object> row = Lists.newArrayList();
        for (ExportItem item : result.getItems()) {
            Object cell = Reflections.getFieldValue(object, item.getName());
            item.setValue(cell);
            row.add(cell);
        }
        result.setRow(row);
        return result;
    }


    private static ExportItem newExportItem(ExportColumn exportColumn, String name, Object value) {
        ExportItem item = new ExportItem();
        item.setValue(value);
        item.setOrder(exportColumn.order());
        item.setName(Strings.isBlankDefault(name, exportColumn.name()));
        item.setTitle(Strings.isBlankDefault(exportColumn.title(), exportColumn.name()));
        return item;
    }

}
