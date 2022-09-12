package com.acooly.core.common.web;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.domain.Entityable;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.exception.FileOperateErrorCodes;
import com.acooly.core.common.service.EntityService;
import com.acooly.core.utils.*;
import com.acooly.core.utils.enums.Messageable;
import com.acooly.core.utils.ie.ExportColumnMeta;
import com.acooly.core.utils.ie.ExportModelMeta;
import com.acooly.core.utils.ie.ExportStyleMeta;
import com.acooly.core.utils.ie.Exports;
import com.acooly.core.utils.io.Files;
import com.acooly.core.utils.io.Streams;
import com.acooly.core.utils.mapper.CsvMapper;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件上传下载，数据导入导出操作封装
 *
 * @param <T>
 * @param <M>
 * @author zhangpu
 */
@Slf4j
public abstract class AbstractFileOperationController<
        T extends Entityable, M extends EntityService<T>>
        extends AbstractOperationController<T, M> {

    private static final Logger logger =
            LoggerFactory.getLogger(AbstractFileOperationController.class);
    /**
     * 文件上传默认配置
     */
    protected UploadConfig uploadConfig = new UploadConfig();

    private int defaultExportBatchSize = 1000;

    @Resource(name = "mvcConversionService")
    private ConversionService conversionService;

    /**
     * 批量导入保存模板数据基础实现
     * <p>
     * <p>该方法用于子类实际的导入Action方法实现完整的数据导入框架功能，子类需要补充：错误处理，消息处理，选择返回视图模式（Json还是页面跳转）
     * 最简使用方法：子类中实现每行的反序列化：unmarshal(List<String[]> lines)<br>
     * <p>
     * <p>功能:
     * <li>自动完成文件上传，参考：doUpload(HttpServletRequest request)
     * <li>根据上次文件的类型，实现文件（行）数据到普通数组的转换（一行文件数据对应一个数组，每列对应数组中一个成员）,参考： doImportLoadXls和doImportLoadCsv
     * <li>数据组装(数组到实体对象)前的合法性检查回调, 参考：beforeUnmarshal
     * <li>只需要实现每行数据的反序列化，自动反序列化数组数据集到实体集合，参考：unmarshal
     * <li>支持转换为实体集合，批量保存前进行合法性检查和相关类处理：afterUnmarshal
     * <li>最后调用服务层批量保存
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected List<T> doImport(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return doImport(request, response, null);
    }

    protected List<T> doImport(
            HttpServletRequest request, HttpServletResponse response, FileType fileType)
            throws Exception {
        Map<String, UploadResult> uploadResults = doUpload(request);
        List<List<String>> lines = loadImportFile(uploadResults, fileType, getCharsetName(request));
        beforeUnmarshal(lines);
        List<T> entities = unmarshal(lines, request);
        afterUnmarshal(entities);
        getEntityService().saves(entities);
        return entities;
    }

    protected List<List<String>> loadImportFile(
            Map<String, UploadResult> uploadResults, FileType fileType, String encoding) {
        if (uploadResults == null || uploadResults.size() == 0) {
            return null;
        }
        if (fileType == null) {
            fileType = getFileType(uploadResults.entrySet().iterator().next().getValue());
        }
        if (fileType == FileType.EXCEL) {
            return loadImportExcel(uploadResults, encoding);
        } else if (fileType == FileType.CSV) {
            return loadImportCsv(uploadResults, encoding);
        } else {
            return null;
        }
    }

    private FileType getFileType(UploadResult uploadResult) {
        String ext = getFileExtention(uploadResult.getName());
        if (StringUtils.equalsIgnoreCase(ext, "xls") || StringUtils.equalsIgnoreCase(ext, "xlsx")) {
            return FileType.EXCEL;
        } else if (StringUtils.equalsIgnoreCase(ext, "csv")) {
            return FileType.CSV;
        } else {
            return null;
        }
    }

    /**
     * 从上传文件中读取数据到集合中
     *
     * @param uploadResults
     * @return
     */
    protected List<List<String>> loadImportExcel(
            Map<String, UploadResult> uploadResults, String charset) {
        if (uploadResults == null || uploadResults.size() == 0) {
            logger.debug("No files have been uploaded successfully.");
            throw new RuntimeException("没有文件上传成功");
        }
        List<List<String>> lines = new LinkedList<List<String>>();
        for (Map.Entry<String, UploadResult> entry : uploadResults.entrySet()) {
            UploadResult uResult = entry.getValue();
            int readRows = 0;
            InputStream in = null;
            Workbook workBook = null;
            try {
                if (uploadConfig.isUseMemery()) {
                    in = uResult.getInputStream();
                } else {
                    File file = uResult.getFile();
                    in = new FileInputStream(file);
                }
                workBook = WorkbookFactory.create(in);
                Sheet sheet = workBook.getSheetAt(0);
                List<String> row = null;
                short sheetTitleCells = sheet.getRow(0).getLastCellNum();
                for (Row r : sheet) {
                    if (r.getLastCellNum() == -1) {
                        continue;
                    }
                    row = new ArrayList<String>(sheetTitleCells);
                    for (int cellNum = 0; cellNum < sheetTitleCells; cellNum++) {
                        Cell cell = r.getCell(cellNum);
                        if (cell == null) {
                            row.add(null);
                        } else {
                            cell.setCellType(CellType.STRING);
                            row.add(cell.getStringCellValue());
                        }

                    }
                    lines.add(row);
                }
            } catch (Exception e) {
                logger.debug(
                        "Read line["
                                + readRows
                                + "] failure with stream mode on file["
                                + uResult.getName()
                                + "]"
                                + e.getMessage());
                throw new RuntimeException(
                        "读取文件[" + uResult.getName() + "]行错误,行号:" + readRows + " ,原因:" + e.getMessage());
            } finally {
                Streams.close(workBook);
                Streams.close(in);
                if (!uploadConfig.isUseMemery()) {
                    if (uResult.getFile().exists()) {
                        uResult.getFile().delete();
                    }
                }
            }
        }
        return lines;
    }

    /**
     * 从上传文件中读取数据到集合中
     *
     * @param uploadResults
     * @return
     */
    protected List<List<String>> loadImportCsv(
            Map<String, UploadResult> uploadResults, String charset) {
        if (uploadResults == null || uploadResults.size() == 0) {
            logger.debug("No files have been uploaded successfully.");
            throw new RuntimeException("没有文件上传成功");
        }
        List<List<String>> lines = new LinkedList<List<String>>();
        for (Map.Entry<String, UploadResult> entry : uploadResults.entrySet()) {
            UploadResult uResult = entry.getValue();
            int readRows = 0;
            BufferedReader reader = null;
            try {
                if (uploadConfig.isUseMemery()) {
                    reader = new BufferedReader(new InputStreamReader(uResult.getInputStream(), charset));
                } else {
                    File file = uResult.getFile();
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
                }
                String line = null;
                while ((line = reader.readLine()) != null) {
                    readRows++;
                    List<String> fieldData = CsvMapper.unmarshal(line);
                    lines.add(fieldData);
                }
            } catch (Exception e) {
                logger.debug(
                        "Read line["
                                + readRows
                                + "] failure with stream mode on file["
                                + uResult.getName()
                                + "]"
                                + e.getMessage());
                throw new RuntimeException("读取文件[" + uResult.getName() + "]行错误,行号:" + readRows);
            } finally {
                Streams.close(reader);
                if (!uploadConfig.isUseMemery()) {
                    if (uResult.getFile().exists()) {
                        uResult.getFile().delete();
                    }
                }
            }
        }
        return lines;
    }

    /**
     * 读取文件后，转换为主实体对象前，进行预处理。可选：参数检测；主实体对象相关的子对象的导入处理等。 <br>
     * 可选这里可以进行合法性检测，剔除格式错误的行，返回正确格式的行 ，然后在Message中记录错误的提示。如果选择图略错误进行保存正确格式的数据，则不抛出异常，否则抛出异常，终止批量保存。
     * <br>
     * 默认实现是返回传入的集合，不做任何处理
     *
     * @param lines
     */
    protected List<List<String>> beforeUnmarshal(List<List<String>> lines) {
        return lines;
    }

    /**
     * 转换为主对象后，批量保存前，对象预处理，可选：参数检测和相关对象处理
     *
     * @param entities
     */
    protected void afterUnmarshal(List<T> entities) {
    }

    /**
     * 导入时: 批量转换读取的数据为实体
     *
     * @param lines
     */
    protected List<T> unmarshal(List<List<String>> lines, HttpServletRequest request) {
        List<T> entities = new LinkedList<T>();
        for (int i = 0; i < lines.size(); i++) {
            if (i == 0 && isIgnoreTitle(request)) {
                continue;
            }
            entities.add(doImportEntity(lines.get(i), request));
        }
        return entities;
    }

    /**
     * 忽略导入到标题行
     *
     * @param request
     * @return
     */
    protected boolean isIgnoreTitle(HttpServletRequest request) {
        return Strings.equals(
                request.getParameter(WebRequestParameterEnum.importIgnoreTitle.code()),
                Boolean.TRUE.toString());
    }

    /**
     * 导入实体
     * <p>
     * <p>导入操作的核心程序员方法, 需要程序员在具体子类复写该方法。根据具体的实体和逻辑,转换读取的一行数据为对应的实体对象
     *
     * @param fields
     * @return T
     */
    protected T doImportEntity(List<String> fields) {
        return null;
    }

    protected T doImportEntity(List<String> fields, HttpServletRequest request) {
        return doImportEntity(fields);
    }

    /**
     * 为保持原有持续结构，并合理处理性能：
     * 1、在导入入口对目标实体进行基于@Annotation的元数据解析，
     * 2、然后通本ThreadLocal变量进行线程类变量传输，不用在每行输出时都进行实体解析
     */
    private static ThreadLocal<ExportModelMeta> exportResultLocal = new ThreadLocal<>();

    /**
     * 导出主方法
     *
     * @param request
     * @param response
     * @throws Exception
     */
    protected void doExport(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        FileType fileType = getFileType(request);
        if (fileType == FileType.CSV) {
            doExportCsv(request, response);
        } else if (fileType == FileType.EXCEL) {
            doExportExcel(request, response);
        }
    }


    protected FileType getFileType(HttpServletRequest request) throws Exception {
        String fType = request.getParameter(WebRequestParameterEnum.importFileType.code());
        FileType fileType = FileType.findOf(fType);
        if (fileType == null) {
            fileType = FileType.EXCEL;
        }
        return fileType;
    }

    /**
     * 导出Excel模板方法
     * <p>
     * <p>本模板方法提供EXCEL导出的程序框架，子类需要实现doExportXls：根据业务要求，编码选择输出操作内容和方式。
     *
     * @param request
     * @param response
     * @return
     */
    protected void doExportExcel(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            // 1、提前解析T的导出元数据，缓冲到线程变量中
            Class<T> clazz = getEntityClass();
            ExportModelMeta exportModelMeta = Exports.parse(clazz);
            if (exportModelMeta != null) {
                exportResultLocal.set(exportModelMeta);
            }

            // 2、导出数据
            doExportExcelHeader(request, response);
            doExportExcelBody(request, response, getExportBatchSize());
        } finally {
            // 3、清理线程变量
            exportResultLocal.remove();
        }
    }

    private ExportModelMeta getExportResult() {
        return exportResultLocal.get();
    }


    /**
     * 导出CSV操作
     *
     * @param request
     * @param response
     * @return
     */
    protected void doExportCsv(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            // 1、提前解析T的导出元数据，缓冲到线程变量中
            Class<T> clazz = getEntityClass();
            ExportModelMeta exportModelMeta = Exports.parse(clazz);
            if (exportModelMeta != null) {
                exportResultLocal.set(exportModelMeta);
            }

            // 2、导出数据
            doExportCsvHeader(request, response);
            doExportCsvBody(request, response, getExportBatchSize());
        } finally {
            // 3、清理线程变量
            exportResultLocal.remove();
        }

    }

    /**
     * 设置导出Excel文件的Http头，只要是设置下载文件的文件名和MITA信息
     */
    protected void doExportExcelHeader(HttpServletRequest request, HttpServletResponse response) {
        String fileName = getExportFileName(request);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment");
        response.setHeader("Content-Disposition", "filename=\"" + Encodes.urlEncode(fileName) + ".xlsx\"");
    }

    /**
     * 导出Excel实现。 <br>
     * <li>可选使用jxl,poi方式直接输出流到reponse,不需要导出页面。该方法返回fase;
     * <li>可选使用页面方式实现，需要返回到导出页面。该方法返回true
     *
     * @param request
     * @param response
     * @param batchSize
     * @return
     */
    protected void doExportExcelBody(
            HttpServletRequest request, HttpServletResponse response, int batchSize) {
        SXSSFWorkbook workbook = null;
        OutputStream out = null;
        try {
            List<String> headerNames = getExportTitles();
            workbook = new SXSSFWorkbook(batchSize);
            workbook.setCompressTempFiles(true);
            ExportModelMeta exportModelMeta = getExportResult();
            Sheet sheet = workbook.createSheet();
            int rowNum = 0;
            // header
            Row row = sheet.createRow(rowNum);
            // header高度
            if (exportModelMeta != null && exportModelMeta.getHeaderStyleMeta() != null
                    && exportModelMeta.getHeaderStyleMeta().getRowHeight() != -1) {
                row.setHeight(exportModelMeta.getHeaderStyleMeta().getRowHeight());
            }
            if (headerNames != null) {
                // 构建通用CellStyle，并缓存到线程变量中
                commonExcelCellStyle(sheet);
                // 构建Header的CellStyle
                CellStyle headerStyle = excelHeaderCellStyle(sheet);
                for (int cellnum = 0; cellnum < headerNames.size(); cellnum++) {
                    row.createCell(cellnum).setCellValue(headerNames.get(cellnum));
                    if (headerStyle != null) {
                        row.getCell(cellnum).setCellStyle(headerStyle);
                    }
                    updateExcelColumnWidth(cellnum, headerNames.get(cellnum), sheet);
                }
                rowNum++;
            }
            // 写数据
            PageInfo<T> pageInfo = new PageInfo<T>(batchSize, 1);
            pageInfo = getEntityService().query(pageInfo, getSearchParams(request), getSortMap(request));
            rowNum = doExportExcelPage(pageInfo.getPageResults(), rowNum, sheet);
            long totalPage = pageInfo.getTotalPage();
            if (totalPage > 1) {
                for (int i = 2; i <= totalPage; i++) {
                    pageInfo.setCurrentPage(i);
                    pageInfo = getEntityService().query(pageInfo, getSearchParams(request), getSortMap(request));
                    rowNum = doExportExcelPage(pageInfo.getPageResults(), rowNum, sheet);
                }
            }

            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            logger.warn("do export excel failure -> " + e.getMessage(), e);
            throw new BusinessException("FILE_EXPORT_EXCEL_FAIL", "EXCEL文件输出失败", e.getMessage());
        } finally {
            Streams.close(out);
            Streams.close(workbook);
            workbook.dispose();
        }
    }


    private CellStyle excelHeaderCellStyle(Sheet sheet) {
        ExportModelMeta exportModelMeta = getExportResult();
        if (exportModelMeta == null) {
            return null;
        }
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


    private CellStyle commonExcelCellStyle(Sheet sheet) {
        ExportModelMeta exportModelMeta = getExportResult();
        if (exportModelMeta == null) {
            return null;
        }
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


    protected int doExportExcelPage(List<T> list, int startRow, Sheet sheet) throws Exception {
        int rowNum = startRow;
        Object value;
        Row row;
        Cell cell;
        CellStyle cellStyle = commonExcelCellStyle(sheet);
        ExportModelMeta exportModelMeta = getExportResult();
        for (T entity : list) {
            List data = doExportRow(entity);
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
                    updateExcelColumnWidth(cellNum, (String) value, sheet);
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

    private void updateExcelColumnWidth(int colNum, String value, Sheet sheet) throws Exception {
        int cellColumnWidth = (value.getBytes("UTF-8").length + 1) * 256;
        if (cellColumnWidth > sheet.getColumnWidth(colNum)) {
            if (cellColumnWidth <= 255 * 256) {
                sheet.setColumnWidth(colNum, cellColumnWidth);
            } else {
                sheet.setColumnWidth(colNum, 100 * 255);
            }
        }
    }

    /**
     * 设置导出Excel文件的Http头，只要是设置下载文件的文件名和MITA信息
     */
    protected void doExportCsvHeader(HttpServletRequest request, HttpServletResponse response) {
        String fileName = getExportFileName(request);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + Encodes.urlEncode(fileName) + ".csv\"");
    }

    /**
     * 实现导出输出的程序模板，子类可以直接使用或根据自己的需求覆写
     *
     * @param request
     * @param response
     * @param batchSize
     * @throws Exception
     */
    protected void doExportCsvBody(
            HttpServletRequest request, HttpServletResponse response, int batchSize) throws Exception {
        PrintWriter p = null;
        try {
            p = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), getCharsetName(request)));
            List<String> headers = getExportTitles();
            if (headers != null) {
                p.println(CsvMapper.marshal(headers));
                p.flush();
            }

            PageInfo<T> pageInfo = new PageInfo<T>(batchSize, 1);
            pageInfo = getEntityService().query(pageInfo, getSearchParams(request), getSortMap(request));
            doExportCsvPage(pageInfo.getPageResults(), p);
            long totalPage = pageInfo.getTotalPage();
            if (totalPage > 1) {
                for (int i = 2; i <= totalPage; i++) {
                    pageInfo.setCurrentPage(i);
                    pageInfo = getEntityService().query(pageInfo, getSearchParams(request), getSortMap(request));
                    doExportCsvPage(pageInfo.getPageResults(), p);
                }
            }
            p.close();
        } catch (Exception e) {
            logger.warn("do export csv failure -> " + e.getMessage(), e);
            throw new BusinessException("FILE_EXPORT_CSV_FAIL", "CSV文件输出失败", e.getMessage());
        } finally {
            Streams.close(p);
        }
    }

    protected void doExportCsvPage(List<T> list, PrintWriter p) throws Exception {
        for (T entity : list) {
            p.println(CsvMapper.marshal(convertToStringList(doExportRow(entity))));
        }
        p.flush();
    }

    private List<String> convertToStringList(List<Object> list) {
        ExportModelMeta exportModelMeta = getExportResult();
        List<String> data = Lists.newArrayList();
        for (int i = 0; i < list.size(); i++) {
            String e = null;
            Object value = list.get(i);
            // 空值直接返回
            if (value == null) {
                data.add(null);
                continue;
            }

            // Money转数字
            if (value instanceof Money) {
                e = ((Money) value).toString();
            } else if (value instanceof Messageable) {
                // 枚举转字符串
                e = ((Messageable) value).message();
                if (exportModelMeta != null && exportModelMeta.getExportItem(i) != null) {
                    ExportColumnMeta item = exportModelMeta.getExportItem(i);
                    if (!item.isShowMapping()) {
                        e = ((Messageable) value).code();
                    }
                }
            } else if (value instanceof Date) {
                // 默认日期时间格式化foramt
                String foramt = Dates.CHINESE_DATETIME_FORMAT_LINE;
                if (Dates.isDate((Date) value)) {
                    foramt = Dates.CHINESE_DATE_FORMAT_SLASH;
                }
                // 如果@anno设置了format，则按设置的格式进行转换
                if (exportModelMeta != null && exportModelMeta.getExportItem(i) != null) {
                    ExportColumnMeta item = exportModelMeta.getExportItem(i);
                    if (Strings.isNotBlank(item.getFormat())) {
                        foramt = item.getFormat();
                    }
                }
                e = Dates.format((Date) value, foramt);
            } else {
                e = conversionService.convert(value, String.class);
            }
            data.add(e);
        }
        return data;
    }

    /**
     * 编列实体对象为List<String>待输出格式
     * <p>
     * <p>这里为简化操作，要求改方法内完成属性的类型转换为输出String类型。如果需要对字段类型进行精确控制，可以考虑复写doExportXls方法实现
     *
     * @param entity
     */
    protected List<String> doExportEntity(T entity) {
        Set<String> simplePropertyNames = Reflections.getSimpleFieldNames(entity.getClass());
        String[] propertyNames = simplePropertyNames.toArray(new String[]{});
        return Reflections.invokeGetterToString(entity, propertyNames);
    }

    protected List<Object> doExportRow(T entity) {
        ExportModelMeta exportModelMeta = getExportResult();
        if (exportModelMeta != null && Collections3.isNotEmpty(exportModelMeta.getItems())) {
            exportModelMeta = Exports.parse(exportModelMeta, entity);
            return exportModelMeta.getRow();
        }
        // 为空，则走原有流程
        return doExportEntity(entity).stream().collect(Collectors.toList());
    }

    /**
     * 返回导出文件内容的标题行，默认为空，不导出标题行
     *
     * @return
     */
    protected List<String> getExportTitles() {
        ExportModelMeta exportModelMeta = getExportResult();
        if (exportModelMeta != null && exportModelMeta.isHeaderShow()) {
            return exportModelMeta.getHeaders();
        }
        return null;
    }

    /**
     * 导出文件的文件名
     *
     * @return
     */
    protected String getExportFileName(HttpServletRequest request) {
        String fileName = getEntityName();
        // @ExportModel.fileName优先级高于实体名称
        ExportModelMeta result = getExportResult();
        if (result != null && Strings.isNotBlank(result.getName())) {
            fileName = result.getName();
        }
        String exportFileName = request.getParameter(WebRequestParameterEnum.exportFileName.code());
        if (StringUtils.isNotBlank(exportFileName)) {
            fileName = exportFileName;
        }
        return fileName;
    }


    /**
     * 上传处理
     *
     * @param request
     * @return key --> 请求的表单参数名称, value ---> 上传结果
     */
    protected Map<String, UploadResult> doUpload(HttpServletRequest request) {
        Map<String, UploadResult> uploadResults = new HashMap<String, UploadResult>();
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> multipartFiles = multiRequest.getMultiFileMap();
        for (Map.Entry<String, List<MultipartFile>> entry : multipartFiles.entrySet()) {
            List<MultipartFile> listFile = entry.getValue();
            String key = entry.getKey();
            for (int i = 0, j = listFile.size(); i < j; i++) {
                if (listFile.size() > 1) {
                    key = entry.getKey() + i;
                }
                loadUploadResult(request, key, listFile.get(i), uploadResults);
            }
        }
        return uploadResults;
    }

    /**
     * 上传文件并绑定到entity对应的属性
     *
     * <p>
     * 规则约定：
     * <li>文件上传表单名称命名为：${entity.propertyName}_uploadFile</li>
     * <li>请设置uploadConfig上传的参数控制 @see #getUploadConfig()</li>
     * </p>
     *
     * @param request
     * @param entity
     */
    protected void doUpload(HttpServletRequest request, T entity) {
        Map<String, UploadResult> uploadResults = doUpload(request);
        if (Collections3.isEmpty(uploadResults.values())) {
            return;
        }
        // 反射方式设置上传的path，可以根据项目情况手动调整为直接设值，效率更高。
        // 规则：文件上传的属性的表单名称的命名规则为：${propertyName}File
        for (UploadResult uploadResult : uploadResults.values()) {
            String paramName = uploadResult.getParameterName();
            if (!Strings.endsWith(paramName, "_uploadFile")) {
                log.warn("文件上传 警告:{}, 规则：${propertyName}_uploadFile", FileOperateErrorCodes.File_INPUT_NAME_ILLEGAL.message());
                continue;
            }
            String fieldName = Strings.removeEnd(paramName, "_uploadFile");
            Field field = Reflections.getAccessibleField(entity, fieldName);
            if (field == null) {
                log.warn("文件上传 警告:{}, 属性名：", FileOperateErrorCodes.File_PATH_FIELD_NOT_EXIST.message(), fieldName);
                continue;
            }
            String exist = (String) Reflections.getFieldValue(entity, field);

            // 如果上传的文件与原文件相同，则不用对原数据进行处理。文件内容已上传覆盖
            if (Strings.equals(exist, uploadResult.getRelativeFile())) {
                return;
            }
            // 文件删除风险大，除程序BUG外，需考虑手动修改数据库值造成的风险
            // 1、如果数据库存在值exist为空或根(/)，保障不会删除整个storageRoot。
            if (Strings.isNotBlank(exist) && !Strings.equals(exist, "/")) {
                File file = new File(uploadConfig.getStorageRoot() + "/" + exist);
                // 2.不会删除目录，上传文件不会是目录，保障安全（其实也解决了1的问题，双重）
                if (file.isFile()) {
                    // 3.保证不删除文件系统根目录，操作系统保留目录下的文件。
                    Files.deleteSafely(file);
                }
            }
            Reflections.setFieldValue(entity, field, uploadResult.getRelativeFile());
        }
    }

    protected void loadUploadResult(HttpServletRequest request, String key, MultipartFile mfile, Map<String, UploadResult> uploadResults) {
        UploadResult result = null;
        if (mfile == null || mfile.getSize() <= 0) {
            return;
        }
        UploadConfig uploadConfig = getUploadConfig();
        String filename = mfile.getOriginalFilename();
        if (mfile.getSize() > uploadConfig.getMaxSize()) {
            log.error("文件上传 失败:{}。fileSize:{}, allowMaxSize:{}", FileOperateErrorCodes.FILE_UPLOAD_SIZE_LIMIT,
                    mfile.getSize(), uploadConfig.getMaxSize());
            throw new BusinessException(FileOperateErrorCodes.FILE_UPLOAD_SIZE_LIMIT, "最大限制:" + uploadConfig.getMaxSize() / 1024 / 1024 + "M");
        }
        String fileExtention = getFileExtention(filename);
        if (!StringUtils.containsIgnoreCase(uploadConfig.getAllowExtentions(), fileExtention)) {
            log.error("文件上传 失败:{}。fileExt:{}, allowExts:{}", FileOperateErrorCodes.FILE_UPLOAD_TYPE_LIMIT,
                    fileExtention, uploadConfig.getAllowExtentions());
            throw new BusinessException(FileOperateErrorCodes.FILE_UPLOAD_TYPE_LIMIT, "支持类型:" + uploadConfig.getAllowExtentions());
        }


        if (uploadConfig.isUseMemery()) {
            // 内存方式，不转存到服务器存储，直接返回流给调用端
            try {
                result = new UploadResult(key, filename, mfile.getSize(), mfile.getInputStream());
            } catch (Exception e) {
                //ig
            }

        } else {
            File destFile = doTransfer(mfile, request);
            File thumFile = doThumbnail(destFile, request);
            result = new UploadResult(key, filename, mfile.getSize(), destFile);
            result.setRelativeFile(getRelativePath(destFile, request));
            result.setThumb(thumFile);
            if (thumFile != null) {
                result.setRelativeThumb(getRelativePath(thumFile, request));
            }
        }
        uploadResults.put(key, result);
    }


    protected String getRelativePath(File file, HttpServletRequest request) {
        String filePath = file.getPath();
        filePath = filePath.replaceAll("\\\\", "/");
        // 通过File对象清洗为规范path，防止配置重复文件分隔符(//)造成后续字符串处理BUG。
        File storageRootFile = new File(this.uploadConfig.getStorageRoot());
        String prefix = storageRootFile.getPath().replaceAll("\\\\", "/");
        filePath = StringUtils.substringAfter(filePath, prefix);
        if (!Strings.startsWith(filePath, "/")) {
            filePath = "/" + filePath;
        }
        return filePath;
    }

    protected File doTransfer(MultipartFile mfile, HttpServletRequest request) {
        // 转存到服务器，返回服务器文件
        File destFile = new File(getUploadFileName(mfile.getOriginalFilename(), uploadConfig, request));
        // modify by zhangpu on 20141026
        // for：移动文件前，检查目标文件所在文件夹是否存在，不存在在创建. update to version: 2.2.4
        File pathFile = destFile.getParentFile();
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        try {
            mfile.transferTo(destFile);
        } catch (Exception e) {
            log.error("文件上传 失败: {}。from:{},to:{}", FileOperateErrorCodes.FILE_UPLOAD_TRANSFER_ERROR,
                    mfile.getName(), destFile.getPath());
            throw new BusinessException(FileOperateErrorCodes.FILE_UPLOAD_TRANSFER_ERROR);
        }

        return destFile;
    }

    /**
     * 生成缩略图
     *
     * @param sourceFile
     */
    protected File doThumbnail(File sourceFile, HttpServletRequest request) {
        if (!Images.isImage(sourceFile.getName()) || !uploadConfig.thumbnailEnable) {
            return null;
        }
        try {
            // 如果是图片文件，并打开了生成缩略图开关，则生成缩略图（文件名为:正常文件_thumb）
            File destFile = getThumbnailFile(sourceFile, request);
            Images.resize(sourceFile.getPath(), destFile.getPath(), uploadConfig.getThumbnailSize(), uploadConfig.getThumbnailSize(), true);
            return destFile;
        } catch (Exception e) {
            logger.warn("上传文件 缩略图生成失败，使用原图。file:{}", sourceFile);
            return sourceFile;
        }
    }

    /**
     * 根据原始文件生成缩略图文件
     *
     * @param sourceFile
     * @param request
     * @return
     */
    protected File getThumbnailFile(File sourceFile, HttpServletRequest request) {
        String destName = Strings.substringBeforeLast(sourceFile.getName(), ".") + uploadConfig.getThumbnailExt() + "." + getFileExtention(sourceFile.getName());
        return new File(sourceFile.getParent(), destName);
    }


    protected String getUploadFileName(
            String originalFileName, UploadConfig uploadConfig, HttpServletRequest request) {
        String fileName = originalFileName;
        if (uploadConfig.isNeedRemaneToTimestamp()) {
            fileName = buildUploadFileName() + "." + getFileExtention(fileName);
        }

        //增加上传存储文件命名空间
        String storagePath;
        if (StringUtils.isNotBlank(uploadConfig.getStorageNameSpace())) {
            String storageRoot = uploadConfig.getStorageRoot();
            storageRoot = storageRoot.endsWith(File.separator) ? storageRoot : storageRoot + File.separator;
            storagePath = storageRoot + uploadConfig.getStorageNameSpace() + File.separator;
        } else {
            storagePath = uploadConfig.getStorageRoot();
        }
        return buildUploadStoragePath(
                storagePath, uploadConfig.isNeedTimePartPath(), null)
                + File.separator
                + fileName;
    }

    protected String buildUploadFileName() {
        return Dates.format(new Date(), Dates.DATETIME_NOT_SEPARATOR)
                + RandomStringUtils.randomNumeric(4);
    }

    protected String buildUploadStoragePath(
            String storageRoot, boolean needTimePartPath, Date baseDate) {
        String path = storageRoot;
        try {
            if (needTimePartPath) {
                Date d = baseDate;
                if (d == null) {
                    d = new Date();
                }
                String pathTimestamp = Dates.format(d, Dates.DATETIME_NOT_SEPARATOR);
                String yearPart = StringUtils.left(pathTimestamp, 4);
                String monthPart = StringUtils.substring(pathTimestamp, 4, 6);
                String dayPart = StringUtils.substring(pathTimestamp, 6, 8);
                String subPath =
                        File.separator + yearPart + File.separator + monthPart + File.separator + dayPart;

                path += subPath;
            }
            File pathFile = new File(path);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
        } catch (Exception e) {
            logger.warn("Build path failure --> " + e);
        }
        return path;
    }

    protected UploadConfig getUploadConfig() {
        return this.uploadConfig;
    }

    protected void setUploadConfig(UploadConfig uploadConfig) {
        this.uploadConfig = uploadConfig;
    }

    private String getFileExtention(String filePath) {
        return StringUtils.substringAfterLast(filePath, ".");
    }

    protected String getCharsetName(HttpServletRequest request) {
        String charset = "UTF-8";
        String requestCharset = StringUtils.trimToEmpty(request.getParameter("charset"));
        if (Strings.isBlank(requestCharset)) {
            requestCharset =
                    StringUtils.trimToEmpty(
                            request.getParameter(WebRequestParameterEnum.exportCharset.code()));
        }
        if (StringUtils.isNotBlank(requestCharset)) {
            charset = requestCharset;
        }
        return charset;
    }

    public int getExportBatchSize() {
        return defaultExportBatchSize;
    }

    public void setDefaultExportBatchSize(int defaultExportBatchSize) {
        this.defaultExportBatchSize = defaultExportBatchSize;
    }

    public static enum FileType {
        EXCEL,
        CSV;

        public static FileType findOf(String code) {
            for (FileType ft : FileType.values()) {
                if (ft.name().equalsIgnoreCase(code)) {
                    return ft;
                }
            }
            return null;
        }
    }

    @Getter
    @Setter
    protected static class UploadResult {
        private String name;
        private long size;
        private File file;
        private File thumb;
        /**
         * 当文件方式时，相对于storageRoot的相对路径
         * 并对windows/linux的文件分隔符进行处理，可以用于直接存储到数据库。
         */
        private String relativeFile;
        private String relativeThumb;
        private String parameterName;
        private InputStream inputStream;

        public UploadResult() {
            super();
        }

        public UploadResult(String parameterName, String name, long size, File file) {
            super();
            this.name = name;
            this.size = size;
            this.file = file;
            this.parameterName = parameterName;
        }

        public UploadResult(String parameterName, String name, long size, InputStream inputStream) {
            super();
            this.name = name;
            this.size = size;
            this.parameterName = parameterName;
            this.inputStream = inputStream;
        }

    }

    @Getter
    @Setter
    protected static class UploadConfig {
        //1024 * 1024 * 32 = 33554432
        private long maxSize = 33554432;
        private String allowExtentions = "txt,zip,csv,xls,xlsx,pdf,jpg,jpeg,gif,png";
        private String storageRoot = System.getenv("java.tmp");
        private String storageNameSpace;
        private boolean useMemery = true;
        private boolean needTimePartPath = true;
        private boolean needRemaneToTimestamp = true;
        private boolean thumbnailEnable = false;
        /**
         * 缩略图与原始图文件后新增的后缀
         */
        private String thumbnailExt = "_thum";
        /**
         * 缩率图的大小（长宽取最大的做等比例缩放）
         */
        private int thumbnailSize = 200;
    }

}
