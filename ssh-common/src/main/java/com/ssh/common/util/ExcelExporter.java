package com.ssh.common.util;

import com.ssh.common.annotation.ExcelField;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelExporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExporter.class);

    private HSSFWorkbook workbook;                                   // 工作簿對象
    private Sheet sheet;                                             // 工作表對象
    private Map<String, CellStyle> cellStyles;                       // 樣式列表
    private int rowNum;                                              // 當前行號
    private List<Object[]> annotations = new ArrayList<Object[]>();         // 註解列表

    /**
     * @param title 表格標題
     * @param clazz 實體對象
     */
    public ExcelExporter(String title, Class<?> clazz) {
        this(title, clazz, 1);
    }

    /**
     * @param title  表格標題
     * @param clazz  實體對象
     * @param type   導出類型(1:導出數據 2:導出模板)
     * @param groups 導入分組
     */
    public ExcelExporter(String title, Class<?> clazz, int type, int... groups) {
        // Get annotation field
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if (excelField != null && (excelField.type() == 0 || excelField.type() == type)) {
                if (groups != null && groups.length > 0) {
                    boolean inGroup = false;
                    for (int group : groups) {
                        if (inGroup) {
                            break;
                        }
                        for (int g : excelField.groups()) {
                            if (group == g) {
                                inGroup = true;
                                annotations.add(new Object[]{excelField, field});
                                break;
                            }
                        }
                    }
                } else {
                    annotations.add(new Object[]{excelField, field});
                }
            }
        }
        // Get annotation method
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            ExcelField excelField = method.getAnnotation(ExcelField.class);
            if (excelField != null && (excelField.type() == 0 || excelField.type() == type)) {
                if (groups != null && groups.length > 0) {
                    boolean inGroup = false;
                    for (int group : groups) {
                        if (inGroup) {
                            break;
                        }
                        for (int g : excelField.groups()) {
                            if (group == g) {
                                inGroup = true;
                                annotations.add(new Object[]{excelField, method});
                                break;
                            }
                        }
                    }
                } else {
                    annotations.add(new Object[]{excelField, method});
                }
            }
        }
        // Field sorting
        Collections.sort(annotations, new Comparator<Object[]>() {
            public int compare(Object[] obj1, Object[] obj2) {
                Integer value1 = ((ExcelField) obj1[0]).sort();
                Integer value2 = ((ExcelField) obj2[0]).sort();
                return value1.compareTo(value2);
            }
        });
        // Initialize
        List<String> headerList = new ArrayList<String>();
        for (Object[] arr : annotations) {
            String t = ((ExcelField) arr[0]).title();
            // 如果是導出則去掉註釋
            if (type == 1) {
                String[] ss = StringUtils.split(t, "**", 2);
                if (ss.length == 2) {
                    t = ss[0];
                }
            }
            headerList.add(t);
        }
        initialize(title, headerList);
    }

    /**
     * @param title   標題
     * @param headers 表頭數組
     */
    public ExcelExporter(String title, String[] headers) {
        initialize(title, Arrays.asList(headers));
    }

    /**
     * @param title      標題
     * @param headerList 表頭列表
     */
    public ExcelExporter(String title, List<String> headerList) {
        initialize(title, headerList);
    }

    /**
     * @param title      標題
     * @param headerList 表頭列表
     */
    private void initialize(String title, List<String> headerList) {

        if (headerList == null || headerList.isEmpty()) {
            throw new IllegalArgumentException("headerList can't be empty!");
        }

        this.workbook = new HSSFWorkbook();
        this.sheet = workbook.createSheet("Sheet1");
        this.cellStyles = createCellStyles(workbook);

        // Create title
        if (StringUtils.isNotBlank(title)) {
            Row titleRow = sheet.createRow(rowNum++);
            titleRow.setHeightInPoints(30);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(cellStyles.get("title"));
            titleCell.setCellValue(title);
            sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), titleRow.getRowNum(), headerList.size() - 1));
        }

        // Create header
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.setHeightInPoints(16);
        for (int i = 0, len = headerList.size(); i < len; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellStyle(cellStyles.get("header"));
            String[] ss = StringUtils.split(headerList.get(i), "**", 2);
            if (ss.length == 2) {
                cell.setCellValue(ss[0]);
                Comment comment = this.sheet.createDrawingPatriarch().createCellComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
                comment.setString(new HSSFRichTextString(ss[1]));
                cell.setCellComment(comment);
            } else {
                cell.setCellValue(headerList.get(i));
            }
            sheet.autoSizeColumn(i);
        }
        for (int i = 0, len = headerList.size(); i < len; i++) {
            int colWidth = sheet.getColumnWidth(i) * 2;
            sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
        }
        LOGGER.debug("Initialize success.");
    }

    private Map<String, CellStyle> createCellStyles(Workbook workbook) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        // 標題字體樣式
        Font titleFont = workbook.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        // 數據區字體樣式
        Font dataFont = workbook.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        // 表頭字體樣式
        Font headerFont = workbook.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        // 表尾字體樣式
        Font footerFont = workbook.createFont();
        footerFont.setFontName("Arial");
        footerFont.setFontHeightInPoints((short) 10);
        // 紅色字體樣式
        Font redFont = workbook.createFont();
        redFont.setFontName("Arial");
        redFont.setFontHeightInPoints((short) 10);
        redFont.setColor(Font.COLOR_RED);
        // 標題
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(titleFont);
        styles.put("title", style);
        // 數據區
        style = workbook.createCellStyle();
        style.setWrapText(true);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFont(dataFont);
        styles.put("data", style);
        // 表頭
        style = workbook.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(headerFont);
        styles.put("header", style);
        // 表尾
        style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(footerFont);
        styles.put("footer", style);
        // 預設樣式一(左對齊)
        style = workbook.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_LEFT);
        styles.put("data1", style);
        // 預設樣式二(居中對齊)
        style = workbook.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_CENTER);
        styles.put("data2", style);
        // 預設樣式三(右對齊)
        style = workbook.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        styles.put("data3", style);
        // 預設樣式四(左對齊紅色字體)
        style = workbook.createCellStyle();
        style.cloneStyleFrom(styles.get("data1"));
        style.setFont(redFont);
        styles.put("data4", style);
        // 預設樣式五(居中對齊紅色字體)
        style = workbook.createCellStyle();
        style.cloneStyleFrom(styles.get("data2"));
        style.setFont(redFont);
        styles.put("data5", style);
        // 預設樣式六(右對齊紅色字體)
        style = workbook.createCellStyle();
        style.cloneStyleFrom(styles.get("data3"));
        style.setFont(redFont);
        styles.put("data6", style);
        return styles;
    }

    public Row createRow() {
        return sheet.createRow(rowNum++);
    }

    public Cell addCell(Row row, int column, Object val) {
        return this.addCell(row, column, val, 0);
    }

    public Cell addCell(Row row, int column, Object val, int style) {
        return this.addCell(row, column, val, style, Class.class);
    }

    /**
     * 添加一個單元格
     *
     * @param row       添加行
     * @param column    添加列
     * @param value     添加值
     * @param style     對齊方式(0 自動 1 左對齊 2 居中對齊 3 右對齊 4 左對齊紅色字體 5 居中對齊紅色字體 6 右對齊紅色字體)
     * @param fieldType 字段类型
     */
    public Cell addCell(Row row, int column, Object value, int style, Class<?> fieldType) {
        Cell cell = row.createCell(column);
        CellStyle cellStyle = cellStyles.get("data" + (style >= 1 && style <= 6 ? style : ""));
        try {
            if (value == null) {
                cell.setCellValue("");
            } else if (value instanceof String) {
                cell.setCellValue((String) value);
            } else if (value instanceof Integer) {
                cell.setCellValue((Integer) value);
            } else if (value instanceof Long) {
                cell.setCellValue((Long) value);
            } else if (value instanceof Double) {
                cell.setCellValue((Double) value);
            } else if (value instanceof Float) {
                cell.setCellValue((Float) value);
            } else if (value instanceof Date) {
                cell.setCellValue(DateFormatUtils.format((Date) value, Constant.DEFAULT_DATE_PATTERN));
            } else {
                if (fieldType != Class.class) {
                    cell.setCellValue((String) fieldType.getMethod("setValue", Object.class).invoke(null, value));
                } else {
                    cell.setCellValue((String) Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(),
                            "fieldtype." + value.getClass().getSimpleName() + "Type")).getMethod("setValue", Object.class).invoke(null, value));
                }
            }
        } catch (Exception e) {
            LOGGER.info("Set cell value [" + row.getRowNum() + "," + column + "] error: " + e.toString());
            cell.setCellValue(String.valueOf(value));
        }
        cell.setCellStyle(cellStyle);
        return cell;
    }

    public <E> ExcelExporter setDataList(List<E> list) {
        for (E element : list) {
            int column = 0;
            Row row = this.createRow();
            StringBuilder sb = new StringBuilder();
            for (Object[] arr : annotations) {
                ExcelField excelField = (ExcelField) arr[0];
                Object value = null;
                try {
                    String propertyName = excelField.value();
                    if (StringUtils.isBlank(propertyName)) {
                        Object obj = arr[1];
                        if (obj instanceof Field) {
                            propertyName = ((Field) obj).getName();
                        } else if (obj instanceof Method) {
                            propertyName = ((Method) obj).getName();
                        }
                    }
                    value = new PropertyDescriptor(propertyName, element.getClass()).getReadMethod().invoke(element);
                } catch (Exception e) {
                    // Failure to ignore
                    LOGGER.info(e.toString());
                }
                this.addCell(row, column++, value, excelField.style(), excelField.fieldType());
                sb.append(value).append(", ");
            }
            LOGGER.debug("Write success: [" + row.getRowNum() + "] " + sb.toString());
        }
        return this;
    }

    public ExcelExporter createFooter(String footer) {
        Row footerRow = sheet.createRow(rowNum++);
        footerRow.setHeightInPoints(16);
        Cell footerCell = footerRow.createCell(0);
        footerCell.setCellStyle(cellStyles.get("footer"));
        footerCell.setCellValue(footer);
        sheet.addMergedRegion(new CellRangeAddress(footerRow.getRowNum(), footerRow.getRowNum(), 0, sheet.getRow(1).getLastCellNum() - 1));
        return this;
    }

    public ExcelExporter write(OutputStream outputStream) throws IOException {
        workbook.write(outputStream);
        return this;
    }

}
