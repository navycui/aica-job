package bnet.library.excel;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bnet.library.excel.dto.ExcelCellVO;
import bnet.library.excel.dto.ExcelInputCells;
import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;

public class ExcelReader {

    private static final Logger logger = LoggerFactory.getLogger(ExcelReader.class);
    private XSSFWorkbook workbook;
    private Boolean autoTrim = true;

    public static boolean isExcelFile(String path) {
        String ext = FilenameUtils.getExtension(path);
        if (string.isNotBlank(ext) && ext.equalsIgnoreCase("XLSX")) {
            return true;
        }
        return false;
    }

    public ExcelReader(String path) throws IOException {
        workbook = new XSSFWorkbook(path);
    }

    public ExcelReader(File file) throws InvalidFormatException, IOException {
        workbook = new XSSFWorkbook(file);
    }

    public ExcelReader(InputStream is) throws InvalidFormatException, IOException {
        workbook = new XSSFWorkbook(is);
    }

    public List<XSSFSheet> getAllSheets() {
        List<XSSFSheet> sheets = new ArrayList<>();
        int cnt = workbook.getNumberOfSheets();
        for (int i = 0; i < cnt; i++) {
            sheets.add(workbook.getSheetAt(i));
        }
        return sheets;
    }

    public XSSFSheet getSheet(String name) {
        return workbook.getSheet(name);
    }

    public XSSFSheet getSheetAt(int index) {
        return workbook.getSheetAt(index);
    }

    public XSSFRow getRowAt(XSSFSheet sheet, int rownum) {
    	return sheet.getRow(rownum);
    }

    public XSSFCell getCell(XSSFSheet sheet, int rownum, int cellnum) {
    	XSSFRow row = sheet.getRow(rownum);
    	if(row == null) {
    		return null;
    	}
    	return row.getCell(cellnum);
    }

    public String getCellValue(XSSFSheet sheet, int rownum, int cellnum) {
    	FormulaEvaluator eval = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
    	XSSFRow row = sheet.getRow(rownum);
    	if(row == null) {
    		return null;
    	}
    	XSSFCell cell = row.getCell(cellnum);
    	if (cell == null) {
    		return null;
    	}

    	String value = null;
        CellType cellType = cell.getCellType();
        switch (cellType) {
        case STRING: // XSSFCell. CELL_TYPE_STRING:
            value = cell.getStringCellValue();
            break;
        case NUMERIC: // XSSFCell.CELL_TYPE_NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                value = date.format(cell.getDateCellValue(), "yyyyMMddHHmmss");
            } else {
                Double d = cell.getNumericCellValue();
                NumberFormat f = NumberFormat.getInstance();
                f.setGroupingUsed(false);
                value = f.format(d);
            }
            break;
        case FORMULA: //XSSFCell.CELL_TYPE_FORMULA:
            String v = cell.toString();
            if ( v == null || v.length() == 0 ) {
                value = "";
                break;
            }
            CellType ev = eval.evaluateFormulaCell(cell);
            if (ev == CellType.NUMERIC) {
                Double d = cell.getNumericCellValue();
                value = String.valueOf(d);
            } else if (ev == CellType.STRING) {
                value = cell.getStringCellValue();
            } else if (ev == CellType.BOOLEAN) {
                value = String.valueOf(cell.getBooleanCellValue());
            }
            break;
        case BOOLEAN:
            value = "" + cell.getBooleanCellValue();
            break;
        case BLANK:
            value = "";
            break;
        case ERROR:
            value = "" + cell.getErrorCellValue();
            break;
        default:
            break;
        }

        return value;
    }

    public void close() {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            logger.error("close() error", e);
        }
    }

    public <T> List<T> read(Class<T> cls, XSSFSheet sheet, String[] propertyNames, int startRowIndex) throws InstantiationException, IllegalAccessException {
        return this.read(cls, sheet, propertyNames, startRowIndex, 0);
    }

    public <T> List<T> read(Class<T> cls, XSSFSheet sheet, String[] propertyNames, int startRowIndex, int readRowCount) throws InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        Map<String, PropertyDescriptor> properties = new HashMap<>();

        T vo = cls.newInstance();
        for (String name: propertyNames) {
            if (string.isBlank(name)) {
                continue;
            }
            PropertyDescriptor pd = null;
            try {
                pd = PropertyUtils.getPropertyDescriptor(vo, name);
            } catch (InvocationTargetException | NoSuchMethodException e) {
                continue;
            }
            properties.put(name, pd);
        }

        FormulaEvaluator eval = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();

        XSSFRow row;
        XSSFCell cell;
        String value = null;
        PropertyDescriptor pd = null;
        int rows = sheet.getLastRowNum() + 1;
        if (readRowCount == 0) {
            readRowCount = rows;
        }
        int r = startRowIndex;
        int readCount = 0;
        while ( (r < rows) && (readCount < readRowCount) ) {
            row = sheet.getRow(r);
            r++;
            if (row == null) {
                continue;
            }
            readCount++;

            vo = cls.newInstance();
            int validCellCnt = 0;
            for (int c = 0; c < propertyNames.length; c++) {
                if (string.isBlank(propertyNames[c])) {
                    continue;
                }
                pd = properties.get(propertyNames[c]);
                if (pd == null) {
                    continue;
                }
                cell = row.getCell(c);
                if (cell == null) {
                    continue;
                }
                value = null;
                CellType cellType = cell.getCellType();
                switch (cellType) {
                case STRING: // XSSFCell. CELL_TYPE_STRING:
                    value = cell.getStringCellValue();
                    break;
                case NUMERIC: // XSSFCell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        value = date.format(cell.getDateCellValue(), "yyyyMMddHHmmss");
                    } else {
                        Double d = cell.getNumericCellValue();
                        NumberFormat f = NumberFormat.getInstance();
                        f.setGroupingUsed(false);
                        value = f.format(d);
                    }
                    break;
                case FORMULA: //XSSFCell.CELL_TYPE_FORMULA:
                    String v = cell.toString();
                    if ( v == null || v.length() == 0 ) {
                        value = "";
                        break;
                    }
                    CellType ev = eval.evaluateFormulaCell(cell);
                    if (ev == CellType.NUMERIC) {
                        Double d = cell.getNumericCellValue();
                        value = String.valueOf(d);
                    } else if (ev == CellType.STRING) {
                        value = cell.getStringCellValue();
                    } else if (ev == CellType.BOOLEAN) {
                        value = String.valueOf(cell.getBooleanCellValue());
                    }
                    break;
                case BOOLEAN:
                    value = "" + cell.getBooleanCellValue();
                    break;
                case BLANK:
                    value = "";
                    break;
                case ERROR:
                    value = "" + cell.getErrorCellValue();
                    break;
                default:
                    break;
                }
                if (string.isNotBlank(value)) {
                    validCellCnt++;
                }
                try {
                    PropertyUtils.setProperty(vo, propertyNames[c], convert(value, pd.getPropertyType()));
                } catch (InvocationTargetException | NoSuchMethodException e) {
                    continue;
                }
            }
            if (validCellCnt > 0) {
                list.add(vo);
            }
        }
        return list;
    }

    public <T> T read(XSSFSheet sheet, Class<T> cls, ExcelInputCells cells) throws InstantiationException, IllegalAccessException {
        Map<String, PropertyDescriptor> properties = new HashMap<>();

        T vo = cls.newInstance();
        for (ExcelCellVO c : cells.getList()) {
            if (string.isBlank(c.getPropertyName())) {
                continue;
            }
            PropertyDescriptor pd = null;
            try {
                pd = PropertyUtils.getPropertyDescriptor(vo, c.getPropertyName());
            } catch (InvocationTargetException | NoSuchMethodException e) {
                continue;
            }
            properties.put(c.getPropertyName(), pd);
        }

        FormulaEvaluator eval = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();

        XSSFRow row;
        XSSFCell cell;
        String value = null;
        PropertyDescriptor pd = null;
        for ( ExcelCellVO c : cells.getList() ) {
        	if (string.isBlank(c.getPropertyName())) {
        		continue;
        	}
        	pd = properties.get(c.getPropertyName());
            if (pd == null) {
                continue;
            }

            row = sheet.getRow(c.getRowNum());
            if (row == null) {
                continue;
            }

            cell = row.getCell(c.getCellNum());
            if (cell == null) {
            	continue;
            }

            value = null;
            CellType cellType = cell.getCellType();
            switch (cellType) {
            case STRING: // XSSFCell. CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            case NUMERIC: // XSSFCell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = date.format(cell.getDateCellValue(), "yyyyMMddHHmmss");
                } else {
                    Double d = cell.getNumericCellValue();
                    DecimalFormat f = new DecimalFormat("###.####");
                    f.setGroupingUsed(false);
                    value = f.format(d);
                }
                break;
            case FORMULA: //XSSFCell.CELL_TYPE_FORMULA:
                String v = cell.toString();
                if ( v == null || v.length() == 0 ) {
                    value = "";
                    break;
                }
                CellType ev = eval.evaluateFormulaCell(cell);
                if (ev == CellType.NUMERIC) {
                    Double d = cell.getNumericCellValue();
                    value = String.valueOf(d);
                } else if (ev == CellType.STRING) {
                    value = cell.getStringCellValue();
                } else if (ev == CellType.BOOLEAN) {
                    value = String.valueOf(cell.getBooleanCellValue());
                }
                break;
            case BOOLEAN:
                value = "" + cell.getBooleanCellValue();
                break;
            case BLANK:
                value = "";
                break;
            case ERROR:
                value = "" + cell.getErrorCellValue();
                break;
            default:
                break;
            }

            try {
                PropertyUtils.setProperty(vo, c.getPropertyName(), convert(value, pd.getPropertyType()));
            } catch (InvocationTargetException | NoSuchMethodException e) {
                continue;
            }
        }
        return vo;
    }

    private Object convert(String value, Class<?> cls) {
        Object obj = null;
        Double d = null;
        if (value == null) {
            return null;
        }

        if (cls.isAssignableFrom(String.class)) {
            if (this.autoTrim) {
                return value.trim();
            }
            return value;
        }
        else if (cls.isAssignableFrom(Date.class)) {
            obj = string.toDate(value);
        }
        else if (cls.isAssignableFrom(Double.class)) {
            try {
                obj = string.toDouble(value);
            } catch (NumberFormatException e) {
                obj = null;
            }
        }
        else if (cls.isAssignableFrom(Long.class)) {
            try {
                d = string.toDouble(value);
                if (d == null) {
                	obj = null;
                }
                else {
                	obj = d.longValue();
                }
            } catch (NumberFormatException e) {
                obj = null;
            }
        }
        else if (cls.isAssignableFrom(Integer.class)) {
            try {
                d = string.toDouble(value);
                if (d == null) {
                	obj = null;
                }
                else {
                	obj = d.intValue();
                }
            } catch (NumberFormatException e) {
                obj = null;
            }
        }
        else if (cls.isAssignableFrom(Boolean.class)) {
            obj = string.toBoolean(value);
        }
        else if (cls.isAssignableFrom(BigDecimal.class)) {
            obj = string.toBigDecimal(value);
        }
        else {
            obj = ConvertUtils.convert(value.trim(), cls);
        }

        return obj;
    }

    public Boolean getAutoTrim() {
        return autoTrim;
    }

    public void setAutoTrim(Boolean autoTrim) {
        this.autoTrim = autoTrim;
    }
}

