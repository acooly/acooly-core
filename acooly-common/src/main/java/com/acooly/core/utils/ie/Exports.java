/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-03 09:25
 */
package com.acooly.core.utils.ie;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.exception.CommonErrorCodes;
import com.acooly.core.utils.*;
import com.acooly.core.utils.enums.Messageable;
import com.acooly.core.utils.ie.anno.ExportColumn;
import com.acooly.core.utils.ie.anno.ExportModel;
import com.acooly.core.utils.io.Streams;
import com.acooly.core.utils.validate.Validators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.core.OrderComparator;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
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

    public static ConversionService conversionService = new DefaultConversionService();


    /**
     * 导出多个sheet导出到一个文件
     *
     * @param exportOrder 导出请求参数对象
     */
    public static void exportExcel(ExportOrder exportOrder) {
        Validators.assertJSR303(exportOrder);
        List<ExportSheetMeta> exportSheetMetas = Lists.newArrayList();
        for (ExportSheet exportSheet : exportOrder.getExportSheets()) {
            Object first = Collections3.getFirst(exportSheet.getList());
            ExportModelMeta exportModelMeta = parse(first);
            ExportSheetMeta exportSheetMeta = new ExportSheetMeta(exportSheet.getSheetName(), exportSheet.getList(), exportModelMeta);
            exportSheetMetas.add(exportSheetMeta);
        }
        OutputStream out = null;
        try {
            out = Files.newOutputStream(Paths.get(exportOrder.getOutputFilePath()));
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCodes.INTERNAL_ERROR, e.getMessage());
        }
        doExportExcel(exportSheetMetas, out);
    }

    /**
     * 导出单个sheet的Excel
     *
     * @param sheetName
     * @param list
     * @param filePath
     */
    public static void exportExcel(String sheetName, List<?> list, String filePath) {
        ExportOrder exportOrder = new ExportOrder(sheetName, list, filePath);
        exportExcel(exportOrder);
    }

    /**
     * 导出单个sheet的Excel
     * sheet和文件名来自：@ExportModel
     *
     * @param list
     * @param filePath
     */
    public static void exportExcel(List<?> list, String filePath) {
        ExportOrder exportOrder = new ExportOrder(null, list, filePath);
        exportExcel(exportOrder);
    }


    protected static void doExportExcel(List<ExportSheetMeta> exportSheetMetas, OutputStream out) {
        SXSSFWorkbook workbook = null;
        try {
            workbook = new SXSSFWorkbook();
            workbook.setCompressTempFiles(true);
            for (ExportSheetMeta exportSheetMeta : exportSheetMetas) {
                doExportExcelSheet(exportSheetMeta, workbook);
            }
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            log.warn("do export excel failure -> " + e.getMessage(), e);
            throw new BusinessException("FILE_EXPORT_EXCEL_FAIL", "EXCEL文件输出失败", e.getMessage());
        } finally {
            Streams.closeQuietly(out);
            Streams.closeQuietly(workbook);
            workbook.dispose();
        }

    }

    /**
     * 输出Excel的单个sheet
     *
     * @param exportSheetMeta 单个sheet的元数据
     * @param workbook        工作簿对象
     */
    protected static void doExportExcelSheet(ExportSheetMeta exportSheetMeta, SXSSFWorkbook workbook) {
        ExportModelMeta exportModelMeta = exportSheetMeta.getExportModelMeta();
        String sheetName = getSheetName(exportSheetMeta);
        Sheet sheet = workbook.createSheet(sheetName);
        int rowNum = 0;
        // header
        Row row = sheet.createRow(rowNum);
        // header高度
        if (exportModelMeta != null && exportModelMeta.getHeaderStyleMeta() != null
                && exportModelMeta.getHeaderStyleMeta().getRowHeight() != -1) {
            row.setHeight(exportModelMeta.getHeaderStyleMeta().getRowHeight());
        }
        List<String> headerNames = exportModelMeta.getHeaders();
        if (headerNames != null) {
            // 构建通用CellStyle，并缓存到线程变量中
            excelCommonCellStyle(sheet, exportModelMeta);
            // 构建Header的CellStyle
            CellStyle headerStyle = excelHeaderCellStyle(sheet, exportModelMeta);
            for (int cellnum = 0; cellnum < headerNames.size(); cellnum++) {
                row.createCell(cellnum).setCellValue(headerNames.get(cellnum));
                if (headerStyle != null) {
                    row.getCell(cellnum).setCellStyle(headerStyle);
                }
                excelColumnWidthUpdate(cellnum, headerNames.get(cellnum), sheet);
            }
            rowNum++;
        }
        // 写数据
        doExportExcelPage(exportSheetMeta.getList(), rowNum, sheet, exportModelMeta);
    }

    protected static int doExportExcelPage(List<?> list, int startRow, Sheet sheet, ExportModelMeta exportModelMeta) {
        int rowNum = startRow;
        Object value;
        Row row;
        Cell cell;
        CellStyle cellStyle = excelCommonCellStyle(sheet, exportModelMeta);
        for (Object entity : list) {
            List data = parseRow(exportModelMeta, entity);
            row = sheet.createRow(rowNum);
            for (int cellNum = 0; cellNum < data.size(); cellNum++) {
                value = data.get(cellNum);
                cell = row.createCell(cellNum);
                if (cellStyle != null) {
                    cell.setCellStyle(cellStyle);
                }
                // 空值直接返回
                if (value == null) {
                    cell.setCellType(CellType.BLANK);
                    continue;
                }

                // Money转数字
                if (value instanceof Money) {
                    value = ((Money) value).getAmount();
                }

                // 枚举转字符串
                if (value instanceof Messageable) {
                    String temp = ((Messageable) value).message();
                    if (exportModelMeta != null && exportModelMeta.getExportItem(cellNum) != null) {
                        ExportColumnMeta item = exportModelMeta.getExportItem(cellNum);
                        if (!item.isShowMapping()) {
                            temp = ((Messageable) value).code();
                        }
                    }
                    value = temp;
                }

                // 日期转字符串
                if (value instanceof Date) {
                    // 默认日期时间格式化foramt
                    String foramt = Dates.CHINESE_DATETIME_FORMAT_LINE;
                    if (Dates.isDate((Date) value)) {
                        foramt = Dates.CHINESE_DATE_FORMAT_SLASH;
                    }
                    // 如果@anno设置了format，则按设置的格式进行转换
                    if (exportModelMeta != null && exportModelMeta.getExportItem(cellNum) != null) {
                        ExportColumnMeta item = exportModelMeta.getExportItem(cellNum);
                        if (Strings.isNotBlank(item.getFormat())) {
                            foramt = item.getFormat();
                        }
                    }
                    value = Dates.format((Date) value, foramt);
                }

                // 数字或字符串处理
                if (value instanceof Number) {
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellValue(conversionService.convert(value, Double.class));
                } else {
                    if (!(value instanceof String)) {
                        value = conversionService.convert(value, String.class);
                    }
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue((String) value);
                    excelColumnWidthUpdate(cellNum, (String) value, sheet);
                }

                // 如果@anno设置了宽度，则覆盖设置
                if (exportModelMeta != null && exportModelMeta.getExportItem(cellNum) != null) {
                    ExportColumnMeta item = exportModelMeta.getExportItem(cellNum);
                    if (item.getWidth() != -1) {
                        sheet.setColumnWidth(cellNum, item.getWidth());
                    }
                }
            }
            rowNum = rowNum + 1;
        }
        return rowNum;
    }

    private static CellStyle excelCommonCellStyle(Sheet sheet, ExportModelMeta exportModelMeta) {
        if (exportModelMeta.getStyle() == null) {
            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            cellStyle.setWrapText(true);
            if (exportModelMeta.isBorder()) {
                cellStyle.setBorderTop(BorderStyle.THIN);
                cellStyle.setBorderBottom(BorderStyle.THIN);
                cellStyle.setBorderLeft(BorderStyle.THIN);
                cellStyle.setBorderRight(BorderStyle.THIN);
            }
            exportModelMeta.setStyle(cellStyle);
        }
        return (CellStyle) exportModelMeta.getStyle();
    }

    private static CellStyle excelHeaderCellStyle(Sheet sheet, ExportModelMeta exportModelMeta) {
        XSSFCellStyle cellStyle = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        if (exportModelMeta.isBorder()) {
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
        }

        ExportStyleMeta headerStyleMeta = exportModelMeta.getHeaderStyleMeta();
        if (headerStyleMeta != null) {
            // 按需设置字体
            if (headerStyleMeta.requireFont()) {
                XSSFFont font = (XSSFFont) sheet.getWorkbook().createFont();
                if (Strings.isNotBlank(headerStyleMeta.getFontName())) {
                    font.setFontName(headerStyleMeta.getFontName());
                }
                if (headerStyleMeta.getFontSize() != -1) {
                    font.setFontHeightInPoints(headerStyleMeta.getFontSize());
                }
                font.setBold(headerStyleMeta.isFontBold());
                cellStyle.setFont(font);
            }
            // 按需设置背景填充色
            if (Strings.isNotBlank(headerStyleMeta.getBackgroundColor())) {
                cellStyle.setFillForegroundColor(new XSSFColor(java.awt.Color.decode(headerStyleMeta.getBackgroundColor())));
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
        }
        return cellStyle;
    }

    private static void excelColumnWidthUpdate(int colNum, String value, Sheet sheet) {
        int cellColumnWidth = 0;
        try {
            cellColumnWidth = (value.getBytes("UTF-8").length + 1) * 256;
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCodes.UNSUPPORTED_ERROR, "不支持UTF8-8编码的getBytes");
        }
        if (cellColumnWidth > sheet.getColumnWidth(colNum)) {
            if (cellColumnWidth <= 255 * 256) {
                sheet.setColumnWidth(colNum, cellColumnWidth);
            } else {
                sheet.setColumnWidth(colNum, 100 * 255);
            }
        }
    }

    private static String getSheetName(ExportSheetMeta exportSheetMeta) {
        String sheetName = exportSheetMeta.getSheetName();
        if (Strings.isBlank(sheetName)) {
            sheetName = exportSheetMeta.getExportModelMeta().getName();
        }
        if (Strings.isBlank(sheetName)) {
            sheetName = exportSheetMeta.getExportModelMeta().getClazz().getSimpleName();
        }
        return sheetName;
    }

    /**
     * 解析导出元数据
     *
     * @param clazz 导出数据的类
     * @return 导出模型元数据对象
     */
    public static ExportModelMeta parse(Class<?> clazz) {
        ExportModel exportModel = clazz.getAnnotation(ExportModel.class);
        if (exportModel == null) {
            return null;
        }
        ExportModelMeta exportModelMeta = new ExportModelMeta();
        // 主属性
        exportModelMeta.setClazz(clazz);
        exportModelMeta.setName(exportModel.name());
        exportModelMeta.setBorder(exportModel.border());
        exportModelMeta.setHeaderShow(exportModel.headerShow());
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
            Field[] fields = cls.getDeclaredFields();
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
     * @param object 实体对象
     * @return 导出模型元数据对象
     */
    public static ExportModelMeta parse(Object object) {
        ExportModelMeta result = parse(object.getClass());
        return parse(result, object);
    }

    public static ExportModelMeta parse(ExportModelMeta result, Object object) {
        result.setRow(parseRow(result, object));
        return result;
    }

    public static List<Object> parseRow(ExportModelMeta result, Object object) {
        List<Object> row = Lists.newArrayList();
        for (ExportColumnMeta item : result.getItems()) {
            Object cell = Reflections.getFieldValue(object, item.getName());
            item.setValue(cell);
            row.add(cell);
        }
        return row;
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
