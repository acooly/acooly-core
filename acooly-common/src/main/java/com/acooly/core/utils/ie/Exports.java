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
import com.acooly.core.utils.ie.anno.ExportColumn;
import com.acooly.core.utils.ie.anno.ExportModel;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sap.conn.jco.util.Codecs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.core.OrderComparator;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
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
    public static ExportModelMeta parse(Class<?> clazz) {
        ExportModel exportModel = clazz.getAnnotation(ExportModel.class);
        if (exportModel == null) {
            return null;
        }
        ExportModelMeta exportModelMeta = new ExportModelMeta();
        // 主属性
        exportModelMeta.setFileName(exportModel.fileName());
        exportModelMeta.setBorder(exportModel.border());
        // 标题行样式
        if (exportModel.headerStyle() != null) {
            exportModelMeta.setHeaderStyleMeta(new ExportStyleMeta(exportModel.headerStyle()));
        }

        // Temporary data container<name,ExportItem>
        Map<String, ExportColumnMeta> itemMap = Maps.newHashMap();
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
        List<ExportColumnMeta> exportColumnMetas = Lists.newArrayList(itemMap.values());
        OrderComparator.sort(exportColumnMetas);

        // 设置值
        exportModelMeta.setItems(exportColumnMetas);
        List<String> titles = Lists.newArrayList();
        for (ExportColumnMeta item : exportColumnMetas) {
            titles.add(item.getHeader());
        }
        exportModelMeta.setHeaders(titles);
        return exportModelMeta;
    }

    /**
     * 获取单个实体对象的导出结果
     *
     * @param object
     * @return
     */
    public static ExportModelMeta parse(Object object) {
        ExportModelMeta result = parse(object.getClass());
        return parse(result, object);
    }

    public static ExportModelMeta parse(ExportModelMeta result, Object object) {
        List<Object> row = Lists.newArrayList();
        for (ExportColumnMeta item : result.getItems()) {
            Object cell = Reflections.getFieldValue(object, item.getName());
            item.setValue(cell);
            row.add(cell);
        }
        result.setRow(row);
        return result;
    }


    private static ExportColumnMeta newExportItem(ExportColumn exportColumn, String name, Object value) {
        ExportColumnMeta item = new ExportColumnMeta();
        item.setValue(value);
        item.setOrder(exportColumn.order());
        item.setFormat(exportColumn.format());
        item.setShowMapping(exportColumn.showMapping());
        item.setWidth(exportColumn.width());
        item.setName(Strings.isBlankDefault(name, exportColumn.name()));
        item.setHeader(Strings.isBlankDefault(exportColumn.header(), exportColumn.name()));
        return item;
    }

    /**
     * 16进制颜色字符串转换成rgb
     *
     * @param hexStr
     * @return rgb
     */
    public static byte[] hex2RGB(String hexStr) {
        if (hexStr != null && !"".equals(hexStr) && hexStr.length() == 7) {
            byte[] rgb = new byte[3];
            rgb[0] = Byte.valueOf(hexStr.substring(1, 3), 16);
            rgb[1] = Byte.valueOf(hexStr.substring(3, 5), 16);
            rgb[2] = Byte.valueOf(hexStr.substring(5, 7), 16);
            return rgb;
        }
        return null;
    }


}
