package bnet.library.excel.report;

public class ExcelReportProvider {
    public static ExcelReportProvider excelReport() {
        return new ExcelReportProvider();
    }

    public GridExcelBuilder simpleGrid() {
        return new GridExcelBuilder();
    }

    public CustomExcelBuilder custom() {
        return new CustomExcelBuilder();
    }
}