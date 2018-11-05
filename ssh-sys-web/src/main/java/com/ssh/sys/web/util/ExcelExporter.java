package com.ssh.sys.web.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.beans.PropertyDescriptor;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class ExcelExporter {

    private static final String DATETIME_PATTERN = "dd/MM/yyyy HH:mm:ss";

    private static HSSFWorkbook workbook;               // 工作簿對象
    private static Map<String, CellStyle> cellStyles;   // 样式列表

    private static void initialize() {
        workbook = new HSSFWorkbook();
        cellStyles = createCellStyles(workbook);
    }

    public static Map<String, CellStyle> createCellStyles(Workbook workbook) {
        Map<String, CellStyle> cellStyles = new HashMap<>();
        // 設置標題樣式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setFillBackgroundColor(IndexedColors.SKY_BLUE.index);
        Font font = workbook.createFont();
        font.setFontName("華文楷體");
        font.setFontHeightInPoints((short) 20);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setCharSet(Font.DEFAULT_CHARSET);
        font.setColor(IndexedColors.BLUE_GREY.index);
        cellStyle.setFont(font);
        cellStyles.put("title", cellStyle);
        // 設置表頭樣式
        cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setFillBackgroundColor(IndexedColors.YELLOW.index);
        cellStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLUE.index);
        cellStyle.setBottomBorderColor(IndexedColors.BLUE.index);
        cellStyle.setLeftBorderColor(IndexedColors.BLUE.index);
        cellStyle.setRightBorderColor(IndexedColors.BLUE.index);
        font = workbook.createFont();
        font.setFontName("宋體");
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setCharSet(Font.DEFAULT_CHARSET);
        font.setColor(IndexedColors.BLUE_GREY.index);
        cellStyle.setFont(font);
        cellStyles.put("header", cellStyle);
        // 設置表體樣式
        cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true); // 字段换行
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLUE.index);
        cellStyle.setBottomBorderColor(IndexedColors.BLUE.index);
        cellStyle.setLeftBorderColor(IndexedColors.BLUE.index);
        cellStyle.setRightBorderColor(IndexedColors.BLUE.index);
        font = workbook.createFont();
        font.setFontName("宋體");
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        font.setCharSet(Font.DEFAULT_CHARSET);
        font.setColor(IndexedColors.BLUE_GREY.index);
        cellStyle.setFont(font);
        cellStyles.put("content", cellStyle);
        // 設置表尾樣式
        cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setFillBackgroundColor(IndexedColors.SKY_BLUE.index);
        font = workbook.createFont();
        font.setFontName("華文楷體");
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setCharSet(Font.DEFAULT_CHARSET);
        font.setColor(IndexedColors.BLUE_GREY.index);
        cellStyle.setFont(font);
        cellStyles.put("footer", cellStyle);
        return cellStyles;
    }


    /**
     * 創建標題(需合併單元格)
     */
    private static void createTableTitleRow(ExcelInfo excelInfo, HSSFSheet[] sheets, int sheetNum, int rowNum) {
        CellRangeAddress titleRange = new CellRangeAddress(rowNum, rowNum, 0, excelInfo.getFields().get(sheetNum).length);
        sheets[sheetNum].addMergedRegion(titleRange);
        HSSFRow titleRow = sheets[sheetNum].createRow(rowNum);
        titleRow.setHeight((short) 800);
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(cellStyles.get("title"));
        titleCell.setCellValue(excelInfo.getTitles()[sheetNum]);
    }

    /**
     * 創建表頭
     */
    private static void creatTableHeaderRow(ExcelInfo excelInfo, HSSFSheet[] sheets, int sheetNum, int rowNum) {
        HSSFRow headRow = sheets[sheetNum].createRow(rowNum);
        headRow.setHeight((short) 350);
        HSSFCell snCell = headRow.createCell(0);
        CellStyle headerStyle = cellStyles.get("header");
        snCell.setCellStyle(headerStyle);
        snCell.setCellValue("序號");
        for (int num = 1, len = excelInfo.getHeader().get(sheetNum).length; num <= len; num++) {
            HSSFCell headCell = headRow.createCell(num);
            headCell.setCellStyle(headerStyle);
            headCell.setCellValue(excelInfo.getHeader().get(sheetNum)[num - 1]);
        }
    }

    /**
     * 創建表尾(需合併單元格)
     */
    private static void createTableFooterRow(ExcelInfo excelInfo, HSSFSheet[] sheets, int sheetNum, int rowNum) {
        CellRangeAddress footerRange = new CellRangeAddress(rowNum, rowNum, 0, excelInfo.getFields().get(sheetNum).length);
        sheets[sheetNum].addMergedRegion(footerRange);
        HSSFRow footerRow = sheets[sheetNum].createRow(rowNum);
        footerRow.setHeight((short) 350);
        HSSFCell footerCell = footerRow.createCell(0);
        footerCell.setCellStyle(cellStyles.get("footer"));
        footerCell.setCellValue(excelInfo.getFooter()[sheetNum]);
    }

    /**
     * 獲取所有Sheet
     */
    private static HSSFSheet[] getSheets(int num, String[] names) {
        HSSFSheet[] sheets = new HSSFSheet[num];
        for (int i = 0; i < num; i++) {
            sheets[i] = workbook.createSheet(names[i]);
        }
        return sheets;
    }

    /**
     * 獲取每一列內容
     */
    private static HSSFCell[] getCells(HSSFRow contentRow, int num) {
        HSSFCell[] cells = new HSSFCell[num + 1];
        CellStyle contentStyle = cellStyles.get("content");
        for (int i = 0, len = cells.length; i < len; i++) {
            cells[i] = contentRow.createCell(i);
            cells[i].setCellStyle(contentStyle);
        }
        cells[0].setCellValue(contentRow.getRowNum() - 1); // 除去標題行所以為-1
        return cells;
    }

    /**
     * 自動調整列寬
     */
    private static void autoSizeColumn(HSSFSheet[] sheets, int sheetNum, String[] fieldNames) {
        for (int i = 0; i < fieldNames.length + 1; i++) {
            sheets[sheetNum].autoSizeColumn(i, true);
        }
    }

    /**
     * 將集合數據輸出到輸出流
     */
    public static void export(ExcelInfo excelInfo, OutputStream outputStream) throws Exception {
        initialize();
        Set<Entry<String, Collection>> set = excelInfo.getContent().entrySet();
        String[] sheetNames = new String[excelInfo.getContent().size()];
        int sheetNameNum = 0;
        for (Entry<String, Collection> entry : set) {
            sheetNames[sheetNameNum] = entry.getKey();
            sheetNameNum++;
        }
        HSSFSheet[] sheets = getSheets(excelInfo.getContent().size(), sheetNames);
        int sheetNum = 0;
        for (Entry<String, Collection> entry : set) {
            // Sheet
            Collection objects = entry.getValue();
            // 標題行
            createTableTitleRow(excelInfo, sheets, sheetNum, 0);
            // 表頭
            creatTableHeaderRow(excelInfo, sheets, sheetNum, 1);
            // 內容
            String[] fields = excelInfo.getFields().get(sheetNum);
            int rowNum = 2;
            for (Object obj : objects) {
                HSSFRow contentRow = sheets[sheetNum].createRow(rowNum);
                contentRow.setHeight((short) 300);
                HSSFCell[] cells = getCells(contentRow, excelInfo.getFields().get(sheetNum).length);
                int cellNum = 1;    // 去掉序號列所以從1開始
                if (fields != null) {
                    for (String field : fields) {
                        Object value = new PropertyDescriptor(field, obj.getClass()).getReadMethod().invoke(obj);
                        if (value != null) {
                            // 對日期格式進行處理
                            if (value.getClass().equals(Date.class)) {
                                cells[cellNum].setCellValue(new SimpleDateFormat(DATETIME_PATTERN).format((Date) value));
                            } else {
                                cells[cellNum].setCellValue(value.toString());
                            }
                        }
                        cellNum++;
                    }
                }
                rowNum++;
            }
            // 腳註
            createTableFooterRow(excelInfo, sheets, sheetNum, rowNum);
            // 自動調整列寬
            autoSizeColumn(sheets, sheetNum, fields);
            sheetNum++;
        }
        workbook.write(outputStream);
    }

    /**
     * 封裝Excel導出的設置信息
     */
    public static class ExcelInfo {

        private String[] titles;                            // 標題
        private List<String[]> header;                      // 表頭
        private List<String[]> fields;                      // 表頭屬性名稱
        private LinkedHashMap<String, Collection> content;  // 表體(Sting:代表Sheet名稱 List:代表Sheet中的行數據)
        private String[] footer;                            // 表尾

        public String[] getTitles() {
            return titles;
        }

        public ExcelInfo setTitles(String[] titles) {
            this.titles = titles;
            return this;
        }

        public String[] getFooter() {
            return footer;
        }

        public ExcelInfo setFooter(String[] footer) {
            this.footer = footer;
            return this;
        }

        public List<String[]> getHeader() {
            return header;
        }

        public ExcelInfo setHeader(List<String[]> header) {
            this.header = header;
            return this;
        }

        public List<String[]> getFields() {
            return fields;
        }

        public ExcelInfo setFields(List<String[]> fields) {
            this.fields = fields;
            return this;
        }

        public LinkedHashMap<String, Collection> getContent() {
            return content;
        }

        public ExcelInfo setContent(LinkedHashMap<String, Collection> content) {
            this.content = content;
            return this;
        }
    }

}
