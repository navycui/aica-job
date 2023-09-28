package bnet.library.excel.report;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.jxls.area.Area;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.formula.StandardFormulaProcessor;
import org.jxls.transform.poi.PoiTransformer;

import bnet.library.util.CoreUtils.string;

public class CustomExcelBuilder {
    private String templatePath;
    private String targetCell = "Sheet1!A1";
    private Map<String, Object> data = new HashMap<>();

    public CustomExcelBuilder addData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public CustomExcelBuilder templatePath(String templatePath) {
        this.templatePath = templatePath;
        return this;
    }

    public CustomExcelBuilder targetCell(String targetCell) {
        this.targetCell = targetCell;
        return this;
    }

    public Workbook getWorkbook() {
        this.assertTemplatePathRequired();
        Workbook workbook = null;
        try (InputStream in = CustomExcelBuilder.class.getResourceAsStream(this.templatePath)) {
            PoiTransformer pt = PoiTransformer.createTransformer(in);
            XlsCommentAreaBuilder areaBuilder = new XlsCommentAreaBuilder(pt);
            List<Area> xlsAreaList = areaBuilder.build();
            this.processXlsArea(xlsAreaList.get(0), this.targetCell, this.data);
            workbook = pt.getWorkbook();
        } catch (IOException | InvalidFormatException e) {
            throw new IllegalStateException("Failed to write an excel report", e);
        }

        return workbook;
    }

    private void assertTemplatePathRequired() {
        if (string.isEmpty(this.templatePath)) {
            throw new IllegalArgumentException("templatePath is required");
        }
    }

    private void processXlsArea(Area xlsArea, String targetCell, Map<String, Object> dataMap) {
        xlsArea.setFormulaProcessor(new StandardFormulaProcessor());
        Context context = new Context();
        context.putVar("data", dataMap);
        xlsArea.applyAt(new CellRef(targetCell), context);
        xlsArea.processFormulas();
    }
}