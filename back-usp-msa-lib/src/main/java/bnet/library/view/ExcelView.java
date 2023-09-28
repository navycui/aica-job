package bnet.library.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.ModelAndView;

import bnet.library.excel.ExcelWriter;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.util.CoreUtils.string;

public class ExcelView extends AbstractPoiExcelView {
    public static final String EXCEL_VIEW_NAME = "excelView";
    public static final String EXCEL_WORKBOOK_NAME = "excelWorkbook";

    @Override
    protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExcelWorkbook ew = (ExcelWorkbook)model.get(EXCEL_WORKBOOK_NAME);
        String fileName = ew.getFilename();
        if (string.isBlank(fileName)) {
            fileName = "이름없음";
        }
        fileName = fileName + ".xlsx";
        fileName = string.toDownloadFilename(fileName);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");

        ExcelWriter.makeXSSFWorkbook(ew, workbook);
    }

    public static ModelAndView getView(ExcelWorkbook workbook) {
        ModelAndView mav = new ModelAndView(EXCEL_VIEW_NAME);
        mav.addObject(EXCEL_WORKBOOK_NAME, workbook);
        return mav;
    }
}
