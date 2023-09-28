package bnet.library.excel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bnet.library.excel.dto.ExcelMergeRange;
import bnet.library.excel.dto.ExcelMergeRows;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.CoreUtils.string;

public class ExcelWriter {

    protected static final Logger logger = LoggerFactory.getLogger(ExcelWriter.class);

    public static XSSFWorkbook makeXSSFWorkbook(ExcelWorkbook ew, XSSFWorkbook workbook) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<ExcelSheet<?>> list = ew.getSheets();

        /*
         * STYLE
         */
        XSSFCellStyle titleStyle = createTitleStyle(workbook);
        XSSFCellStyle headerStyle = createHeaderStyle(workbook);
        XSSFCellStyle contentStyle = createContentStyle(workbook);
        XSSFCellStyle footerStyle = createFooterStyle(workbook);

        for (ExcelSheet<?> vo: list ) {
            String title = vo.getTitle();
            String sheetName = vo.getSheetName();
            if (string.isBlank(sheetName)) {
                sheetName = "Data";
            }

            titleStyle.getFont().setFontName(vo.getFontName());
            headerStyle.getFont().setFontName(vo.getFontName());
            contentStyle.getFont().setFontName(vo.getFontName());
            footerStyle.getFont().setFontName(vo.getFontName());

            ExcelMergeRows header = vo.getHeaders();
            ExcelMergeRows footer = vo.getFooters();
            ExcelMergeRows mergeRegions = vo.getMergeRegions();
            Integer[] columnWidths = vo.getColumnWidths();
            String[] properties = vo.getProperties();
            List<?> rows = vo.getRows();

            XSSFSheet sheet = workbook.createSheet(sheetName.replaceAll("[/:*?\\\\\\[\\]]", ""));
            int r = 0;

            // TITLE
            if (string.isNotBlank(title)) {
                r = drawSheetTitle(sheet, title, titleStyle, properties.length, r);
            }
            // HEADER
            if (header != null && header.getValues() != null && header.getValues().size() > 0) {
                r = drawColumnMergeRows(sheet, header, headerStyle, r);
            }

            // DATA
            XSSFCell cell = null;
            Method[] methods = null;
            boolean first = true;
            boolean isMap = false;
            for (Object row: rows) {
                if (first) {
                    if (row instanceof Map) {
                        isMap = true;
                    } else {
                        methods = row.getClass().getMethods();
                    }
                }
                int c = -1;
                for (String property: properties) {
                    c++;
                    Object value = null;
                    Method method = null;
                    Class<?> returnType = null;
                    if (!isMap) {
                        method = getMethod(methods, property);
                        if (method == null) {
                            continue;
                        }
                        value = method.invoke(row);
                        returnType = method.getReturnType();
                    } else {
                        @SuppressWarnings("unchecked")
                        Map<String, ?> map = (Map<String, ?>)row;
                        value = map.get(property);
                        returnType = value.getClass();
                    }
                    cell = getCell(sheet, r, c);

                    if (Number.class.isAssignableFrom(returnType)) {
                        if (value == null) {
                            cell.setCellValue(0);
                        } else {
                            cell.setCellValue(Double.parseDouble(value.toString()));
                        }
                    } else if (Date.class.isAssignableFrom(returnType)) {
                        if (value == null) {
                            cell.setCellValue("");
                        } else {
                            Date date = string.toDate(value.toString());
                            cell.setCellValue(date);
                        }
                    } else {
                        if (value == null) {
                            cell.setCellValue("");
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                    cell.setCellStyle(contentStyle);
                }
                r++;
                first = false;
            }

            if (footer != null && footer.getValues() != null && footer.getValues().size() > 0) {
                r = drawColumnMergeRows(sheet, footer, footerStyle, r);
            }

            if (mergeRegions != null) {
                mergeRegions(sheet, mergeRegions);
            }

            if (columnWidths != null && columnWidths.length > 0) {
                for (int i = 0; i < columnWidths.length; i++) {
                    sheet.setColumnWidth(i, columnWidths[i]);
                }
            }
            else {
                int c = 0;
                int w = 0;
                int len = properties.length;
                for (c = 0; c < len; c++) {
                    sheet.autoSizeColumn(c);
                    w = sheet.getColumnWidth(c);
                    sheet.setColumnWidth(c, w + 512 * 2);
                }
            }
        }

        return workbook;
    }

    private static int drawSheetTitle(XSSFSheet sheet, String title, CellStyle style, int columnCount, int r) {
        XSSFCell cell = getCell(sheet, r, 0);
        cell.setCellValue(title);
        sheet.addMergedRegion(new CellRangeAddress(r, r, 0, columnCount - 1));
        cell.setCellStyle(style);
        return ++r;
    }

    private static int drawColumnMergeRows(XSSFSheet sheet, ExcelMergeRows mergeRows, CellStyle style, int r) {
        int row = r;
        List<Object[]> headers = mergeRows.getValues();
        for (Object[] values: headers) {
            XSSFCell cell = null;
            int c = 0;
            for (Object value: values) {
                cell = getCell(sheet, row, c++);
                if (value == null) {
                    cell.setCellValue("");
                }
                else if (Number.class.isAssignableFrom(value.getClass())) {
                    cell.setCellValue(Double.parseDouble(value.toString()));
                } else if (Date.class.isAssignableFrom(value.getClass())) {
                    Date date = string.toDate(value.toString());
                    cell.setCellValue(date);
                } else {
                    cell.setCellValue(value.toString());
                }
                cell.setCellStyle(style);
            }
            row++;
        }

        for (ExcelMergeRange range: mergeRows.getMergeRanges()) {
            sheet.addMergedRegion(new CellRangeAddress(
                    r + range.getBeginRow(),
                    r + range.getEndRow(),
                    range.getBeginColumn(), range.getEndColumn()));
        }
        return row;
    }

    private static void mergeRegions(XSSFSheet sheet, ExcelMergeRows mergeRegions) {
        for (ExcelMergeRange range: mergeRegions.getMergeRanges()) {
            sheet.addMergedRegion(new CellRangeAddress(
                    range.getBeginRow(),
                    range.getEndRow(),
                    range.getBeginColumn(),
                    range.getEndColumn()));
        }
    }

    private static Method getMethod(Method[] methods, String name) {
        String getMethodName = "get" + name.substring(0,1).toUpperCase();
        String isMethodName = "is" + name.substring(0,1).toUpperCase();
        if (name.length() > 1) {
            getMethodName += name.substring(1);
            isMethodName += name.substring(1);
        }
        for (Method method: methods) {
            if (method.getName().equals(getMethodName) || method.getName().equals(isMethodName)) {
                return method;
            }
        }

        return null;
    }

    private static XSSFCellStyle createTitleStyle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
        font.setBold(true);
        font.setFontHeightInPoints((short)13);

        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(font);
        return style;
    }

    private static XSSFCellStyle createHeaderStyle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
        font.setBold(true);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontHeightInPoints((short)10);

        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor((short) 0x2c);
        //style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setWrapText(true);
        style.setFont(font);
        return style;
    }

    private static XSSFCellStyle createFooterStyle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
        font.setBold(true);
        font.setFontHeightInPoints((short)10);

        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);

        style.setFont(font);
        return style;
    }

    private static XSSFCellStyle createContentStyle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
        font.setBold(false);
        font.setFontHeightInPoints((short)10);

        XSSFCellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);

        style.setFont(font);
        return style;
    }

    private static XSSFCell getCell(XSSFSheet sheet, int row, int col) {
        XSSFRow sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            sheetRow = sheet.createRow(row);
        }
        XSSFCell cell = sheetRow.getCell((short) col);
        if (cell == null) {
            cell = sheetRow.createCell((short) col);
        }
        return cell;
    }
}

