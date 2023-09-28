package bnet.library.excel.report;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.jxls.area.Area;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.command.GridCommand;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.formula.StandardFormulaProcessor;
import org.jxls.template.SimpleExporter;
import org.jxls.transform.poi.PoiTransformer;

import bnet.library.util.CoreUtils.string;

public class GridExcelBuilder {
    private Iterable<?> list;
    private List<String> headers;
    private List<String> beanProps;
    private String templatePath;
    private String targetCell = "Sheet1!A1";

    public GridExcelBuilder list(Iterable<?> list) {
        this.list = list;
        return this;
    }

    public GridExcelBuilder headers(String... headers) {
        this.headers = Arrays.asList(headers);
        return this;
    }

    public GridExcelBuilder beanProps(String... beanProps) {
        this.beanProps = Arrays.asList(beanProps);
        return this;
    }

    public GridExcelBuilder templatePath(String templatePath) {
        this.templatePath = templatePath;
        return this;
    }

    public GridExcelBuilder targetCell(String targetCell) {
        this.targetCell = targetCell;
        return this;
    }

    public Workbook getWorkbook() {
        InputStream in;
        if (string.isNotEmpty(this.templatePath)) {
            in = GridExcelBuilder.class.getResourceAsStream(this.templatePath);
        } else {
            in = SimpleExporter.class.getResourceAsStream("grid_template.xls");
        }

        Context context = new Context();
        context.putVar("headers", this.headers);
        context.putVar("data", this.list);

        try {
            String e = string.join(this.beanProps, ",");
            PoiTransformer transformer = PoiTransformer.createTransformer(in);
            XlsCommentAreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);
            areaBuilder.setTransformer(transformer);
            List<Area> xlsAreaList = areaBuilder.build();
            Area firstArea = xlsAreaList.get(0);
            CellRef targetCellRef = new CellRef(this.targetCell);
            GridCommand gridCommand = (GridCommand) firstArea.getCommandDataList().get(0).getCommand();
            gridCommand.setProps(e);
            firstArea.applyAt(targetCellRef, context);
            firstArea.setFormulaProcessor(new StandardFormulaProcessor());
            firstArea.processFormulas();
            String sourceSheetName = firstArea.getStartCellRef().getSheetName();
            if (!sourceSheetName.equalsIgnoreCase(targetCellRef.getSheetName())) {
                transformer.deleteSheet(sourceSheetName);
            }

            return transformer.getWorkbook();
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException("Failed to write to output stream", e);
        }
    }
}