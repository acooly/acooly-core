package com.acooly.core.common.web;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.domain.Entityable;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityService;
import com.acooly.core.utils.*;
import com.acooly.core.utils.mapper.CsvMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * 文件上传下载，数据导入导出操作封装
 *
 * @param <T>
 * @param <M>
 * @author zhangpu
 */
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

    /**
     * 批量导入保存模板数据基础实现
     *
     * <p>该方法用于子类实际的导入Action方法实现完整的数据导入框架功能，子类需要补充：错误处理，消息处理，选择返回视图模式（Json还是页面跳转）
     * 最简使用方法：子类中实现每行的反序列化：unmarshal(List<String[]> lines)<br>
     *
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
                for (Row r : sheet) {
                    row = new ArrayList<String>(r.getLastCellNum());
                    for (Cell cell : r) {
                        cell.setCellType(CellType.STRING);
                        row.add(cell.getStringCellValue());
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
                IOUtils.closeQuietly(workBook);
                IOUtils.closeQuietly(in);
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
                IOUtils.closeQuietly(reader);
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
     *
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

    protected void doExport(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        FileType fileType = getFileType(request);
        if (fileType == FileType.CSV) {
            doExportCsv(request, response);
        } else if (fileType == FileType.EXCEL) {
            doExportExcel(request, response);
        } else {
            throw new UnsupportedOperationException("不支持的导出文件类型:" + fileType);
        }
    }

    protected FileType getFileType(HttpServletRequest request) throws Exception {
        String fType = request.getParameter(WebRequestParameterEnum.importFileType.code());
        FileType fileType = FileType.findOf(fType);
        if (fileType == null) {
            fileType = FileType.CSV;
        }
        return fileType;
    }

    /**
     * 导出Excel模板方法
     *
     * <p>本模板方法提供EXCEL导出的程序框架，子类需要实现doExportXls：根据业务要求，编码选择输出操作内容和方式。
     *
     * @param request
     * @param response
     * @return
     */
    protected void doExportExcel(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        doExportExcelHeader(request, response);
        doExportExcelBody(request, response, getExportBatchSize());
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
        doExportCsvHeader(request, response);
        doExportCsvBody(request, response, getExportBatchSize());
    }

    /**
     * 设置导出Excel文件的Http头，只要是设置下载文件的文件名和MITA信息
     */
    protected void doExportExcelHeader(HttpServletRequest request, HttpServletResponse response) {
        String fileName = getExportFileName(request);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment");
        response.setHeader(
                "Content-Disposition", "filename=\"" + Encodes.urlEncode(fileName) + ".xlsx\"");
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
            Sheet sheet = workbook.createSheet();
            int rowNum = 0;
            // 写入header
            Row row = sheet.createRow(rowNum);
            if (headerNames != null) {
                for (int cellnum = 0; cellnum < headerNames.size(); cellnum++) {
                    row.createCell(cellnum).setCellValue(headerNames.get(cellnum));
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
                    pageInfo =
                            getEntityService().query(pageInfo, getSearchParams(request), getSortMap(request));
                    rowNum = doExportExcelPage(pageInfo.getPageResults(), rowNum, sheet);
                }
            }

            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            logger.warn("do export excel failure -> " + e.getMessage(), e);
            throw new BusinessException("执行导出过程失败[" + e.getMessage() + "]");
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(workbook);
            workbook.dispose();
        }
    }

    protected int doExportExcelPage(List<T> list, int startRow, Sheet sheet) throws Exception {
        int rowNum = startRow;
        String value;
        Row row;
        Cell cell;
        for (T entity : list) {
            List<String> entityData = doExportEntity(entity);
            row = sheet.createRow(rowNum);
            for (int cellNum = 0; cellNum < entityData.size(); cellNum++) {
                value = Strings.trimToEmpty(entityData.get(cellNum));
                cell = row.createCell(cellNum);
                // 简单特殊操作处理一些特殊的数字，不采用反射和类型判断处理。
//                if (Strings.isNumber(value)) {
//                    if (Strings.length(value) > 1
//                            && Strings.startsWith(value, "0")
//                            && !Strings.startsWith(value, "0.")) {
//                        cell.setCellValue(value);
//                    } else if (Strings.length(value) > 11) {
//                        cell.setCellValue(value);
//                    } else {
//                        cell.setCellValue(Double.valueOf(value));
//                    }
//                } else {
//                    cell.setCellValue(value);
//                }
                //防止出现excel科学计算法，都用string代替
                cell.setCellValue(value);
            }
            rowNum = rowNum + 1;
        }
        return rowNum;
    }

    /**
     * 设置导出Excel文件的Http头，只要是设置下载文件的文件名和MITA信息
     */
    protected void doExportCsvHeader(HttpServletRequest request, HttpServletResponse response) {
        String fileName = getExportFileName(request);
        response.setContentType("application/octet-stream");
        response.setHeader(
                "Content-Disposition", "attachment;filename=\"" + Encodes.urlEncode(fileName) + ".csv\"");
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
            p =
                    new PrintWriter(
                            new OutputStreamWriter(response.getOutputStream(), getCharsetName(request)));
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
                    pageInfo =
                            getEntityService().query(pageInfo, getSearchParams(request), getSortMap(request));
                    doExportCsvPage(pageInfo.getPageResults(), p);
                }
            }
            p.close();
        } catch (Exception e) {
            logger.warn("do export csv failure -> " + e.getMessage(), e);
            throw new BusinessException("CSV输出失败[" + e.getMessage() + "]");
        } finally {
            IOUtils.closeQuietly(p);
        }
    }

    protected void doExportCsvPage(List<T> list, PrintWriter p) throws Exception {
        for (T entity : list) {
            p.println(CsvMapper.marshal(doExportEntity(entity)));
        }
        p.flush();
    }

    /**
     * 编列实体对象为List<String>待输出格式
     *
     * <p>这里为简化操作，要求改方法内完成属性的类型转换为输出String类型。如果需要对字段类型进行精确控制，可以考虑复写doExportXls方法实现
     *
     * @param entity
     */
    protected List<String> doExportEntity(T entity) {
        Set<String> simplePropertyNames = Reflections.getSimpleFieldNames(entity.getClass());
        String[] propertyNames = simplePropertyNames.toArray(new String[]{});
        return Reflections.invokeGetterToString(entity, propertyNames);
    }

    /**
     * 返回导出文件内容的标题行，默认为空，不导出标题行
     *
     * @return
     */
    protected List<String> getExportTitles() {
        return null;
    }

    /**
     * 导出文件的文件名
     *
     * @return
     */
    protected String getExportFileName(HttpServletRequest request) {
        String fileName = getEntityName();
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
    protected Map<String, UploadResult> doUpload(HttpServletRequest request) throws Exception {
        UploadConfig uploadConfig = getUploadConfig();
        Map<String, UploadResult> uploadResults = new HashMap<String, UploadResult>();
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> multipartFiles = multiRequest.getFileMap();
        UploadResult result = null;
        for (Map.Entry<String, MultipartFile> entry : multipartFiles.entrySet()) {
            MultipartFile mfile = entry.getValue();
            if (mfile == null || mfile.getSize() <= 0) {
                continue;
            }
            String filename = mfile.getOriginalFilename();
            if (mfile.getSize() > uploadConfig.getMaxSize()) {
                throw new BusinessException(
                        "文件[" + filename + "]大小操作限制，最大限制:" + uploadConfig.getMaxSize() / 1024 / 1024 + "M");
            }
            String fileExtention = getFileExtention(filename);
            if (!StringUtils.containsIgnoreCase(uploadConfig.getAllowExtentions(), fileExtention)) {
                throw new BusinessException(
                        "文件[" + filename + "]类型不支持，支持类型:" + uploadConfig.getAllowExtentions());
            }

            if (uploadConfig.isUseMemery()) {
                // 内存方式，不转存到服务器存储，直接返回流给调用端
                result =
                        new UploadResult(entry.getKey(), filename, mfile.getSize(), mfile.getInputStream());
            } else {
                File destFile = doTransfer(mfile, request);
                File thumFile = doThumbnail(destFile, request);
                result = new UploadResult(entry.getKey(), filename, mfile.getSize(), destFile);
                result.setRelativeFile(getRelativePath(destFile, request));
                result.setThumb(thumFile);
                if (thumFile != null) {
                    result.setRelativeThumb(getRelativePath(thumFile, request));
                }
            }
            uploadResults.put(entry.getKey(), result);
        }
        return uploadResults;
    }


    protected String getRelativePath(File file, HttpServletRequest request) {
        String filePath = file.getPath();
        filePath = filePath.replaceAll("\\\\", "/");
        filePath = StringUtils.substringAfter(filePath, this.uploadConfig.getStorageRoot().replaceAll("\\\\", "/"));
        if (!Strings.startsWith(filePath, "/")) {
            filePath = "/" + filePath;
        }


        return filePath;
    }

    protected File doTransfer(MultipartFile mfile, HttpServletRequest request) throws IOException {
        // 转存到服务器，返回服务器文件
        File destFile = new File(getUploadFileName(mfile.getOriginalFilename(), uploadConfig, request));
        // modify by zhangpu on 20141026
        // for：移动文件前，检查目标文件所在文件夹是否存在，不存在在创建. update to version: 2.2.4
        File pathFile = destFile.getParentFile();
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        mfile.transferTo(destFile);
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

    private String getCharsetName(HttpServletRequest request) {
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
        private String allowExtentions = "txt,zip,csv,xls,xlsx,jpg,gif,png";
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
