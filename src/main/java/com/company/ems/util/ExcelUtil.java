package com.company.ems.util;

import com.company.ems.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Excel导出工具类
 * 使用Apache POI实现Excel文件生成和导出
 * 
 * @author EMS Team
 */
@Slf4j
public class ExcelUtil {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 导出Excel文件
     * 
     * @param headers 表头数组
     * @param dataList 数据列表（每行是一个Object数组）
     * @param fileName 文件名（不含扩展名）
     * @param response HTTP响应对象
     */
    public static void exportExcel(String[] headers, List<Object[]> dataList, 
                                   String fileName, HttpServletResponse response) {
        Workbook workbook = null;
        OutputStream outputStream = null;
        
        try {
            // 1. 创建工作簿
            workbook = new XSSFWorkbook();
            
            // 2. 创建工作表
            Sheet sheet = workbook.createSheet("Sheet1");
            
            // 3. 创建样式
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            
            // 4. 创建表头行
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 5. 填充数据
            for (int i = 0; i < dataList.size(); i++) {
                Row dataRow = sheet.createRow(i + 1);
                Object[] rowData = dataList.get(i);
                
                for (int j = 0; j < rowData.length; j++) {
                    Cell cell = dataRow.createCell(j);
                    setCellValue(cell, rowData[j]);
                    cell.setCellStyle(dataStyle);
                }
            }
            
            // 6. 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                // 设置最小宽度
                int columnWidth = sheet.getColumnWidth(i);
                sheet.setColumnWidth(i, Math.max(columnWidth, 3000));
            }
            
            // 7. 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()) + ".xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
            
            // 8. 写入响应流
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            
            log.info("Excel文件导出成功：{}.xlsx，共{}行数据", fileName, dataList.size());
            
        } catch (IOException e) {
            log.error("Excel文件导出失败：{}", e.getMessage(), e);
            throw new BusinessException("Excel文件导出失败：" + e.getMessage());
        } finally {
            // 9. 关闭资源
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                log.error("关闭Excel资源失败：{}", e.getMessage(), e);
            }
        }
    }
    
    /**
     * 创建表头样式
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // 设置背景色
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // 设置边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        // 设置对齐方式
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        // 设置字体
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        
        return style;
    }
    
    /**
     * 创建数据样式
     */
    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        // 设置边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        // 设置对齐方式
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        // 设置字体
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        
        return style;
    }
    
    /**
     * 设置单元格值（根据数据类型自动转换）
     */
    private static void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }
        
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue(((LocalDate) value).format(DATE_FORMATTER));
        } else if (value instanceof LocalTime) {
            cell.setCellValue(((LocalTime) value).format(TIME_FORMATTER));
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue(((LocalDateTime) value).format(DATETIME_FORMATTER));
        } else if (value instanceof java.math.BigDecimal) {
            cell.setCellValue(((java.math.BigDecimal) value).doubleValue());
        } else {
            cell.setCellValue(value.toString());
        }
    }
}
