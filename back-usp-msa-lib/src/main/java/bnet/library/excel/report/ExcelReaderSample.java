package bnet.library.excel.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelReaderSample {
	public static Logger logger = LoggerFactory.getLogger(ExcelReaderSample.class);

//    public static void main(String[] args) throws InterruptedException {
//    	ExcelReaderSample sample = new ExcelReaderSample();
//        ExcelReader reader = null;
//        try {
//        	InputStream is = sample.getClass().getResourceAsStream("/thenet/framework/library/excel/sample/sample.xlsx");
//            reader = new ExcelReader(is);
//            XSSFSheet sheet = reader.getSheetAt(0);
//            String[] properties = {
//                    "loginId", "userNm", "deptNm", "hireDtime", "salary"};
//            List<ExcelVO> list = reader.read(ExcelVO.class, sheet, properties, 1);
//            System.out.println(list);
//        } catch (InstantiationException | IllegalAccessException | IOException | InvalidFormatException e) {
//            e.printStackTrace();
//        } finally {
//            if (reader != null) {
//                reader.close();
//            }
//        }
//    }
}
