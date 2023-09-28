package bnet.library.view;


import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractView;

import bnet.library.util.CoreUtils.date;
import bnet.library.util.CoreUtils.string;

public class ReportExcelView extends AbstractView {
    private static String REPORT_EXCEL_VIEW = "reportExcelView";
    private static String REPORT_WORKBOOK = "reportWorkbook";
    private static String REPORT_FILENAME = "reportFilename";

    public static ModelAndView modelAndView(Workbook workbook, String fileName) {
    	if (string.isBlank(fileName)) {
    		fileName = "이름없음";
    	}
   		fileName = fileName + ".xlsx";
    	fileName = string.toDownloadFilename(fileName);
        ModelAndView view = new ModelAndView(REPORT_EXCEL_VIEW);
        view.addObject(REPORT_WORKBOOK, workbook);
        view.addObject(REPORT_FILENAME, fileName);
        return view;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (!model.containsKey(REPORT_WORKBOOK)) {
            throw new IllegalArgumentException(REPORT_WORKBOOK + " is required");
        } else {
            Workbook workbook = (Workbook) model.get(REPORT_WORKBOOK);
            String reportFileName = (String) model.get(REPORT_FILENAME);
            if (string.isBlank(reportFileName)) {
                reportFileName = "report-" + date.getCurrentDate("yyyyMMdd-HHmmss") + ".xls";
            }

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + reportFileName + "\"");
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
            out.flush();
        }
    }
}