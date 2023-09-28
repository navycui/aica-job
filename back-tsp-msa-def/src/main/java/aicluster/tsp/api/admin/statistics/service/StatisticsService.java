package aicluster.tsp.api.admin.statistics.service;

import aicluster.tsp.api.admin.statistics.param.StatisticsParam;
import aicluster.tsp.api.common.param.CommonReturn;
import aicluster.tsp.common.dao.TsptStatisticsDao;
import aicluster.tsp.common.dto.StatisticsDto;
import bnet.library.excel.dto.ExcelMergeRows;
import bnet.library.excel.dto.ExcelSheet;
import bnet.library.excel.dto.ExcelWorkbook;
import bnet.library.view.ExcelView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Service
public class StatisticsService {

	@Autowired
	private TsptStatisticsDao tsptStatisticsDao;

	// 통계 조회
	public StatisticsParam getStatistics(StatisticsParam param) {
		List<StatisticsDto> data = tsptStatisticsDao.selectStatistics(param.getBeginDt(), param.getEndDt()) ;
		StatisticsParam result = new StatisticsParam(data);

		result.setEntrprsCount(tsptStatisticsDao.selectEntrprsCount(param.getBeginDt(), param.getEndDt()));
		// 목록 조회
		return result;
	}

	public ModelAndView getListExcelDownload(StatisticsParam param) {
		//boolean isExcel = true;
		List<StatisticsDto> list = tsptStatisticsDao.selectStatistics(param.getBeginDt(), param.getEndDt());
		ExcelWorkbook wb = new ExcelWorkbook();
		wb.setFilename("장비사용 현황");
//		for (int i = 0; i < list.size(); i++)
//		{
//
//			String title = TspCode.statistics.values()[i].getTitle();
//			if (title != "장비상태"){
//				continue;
//			}
//			List<StatisticsDto> exList = list.get(i);
//			ExcelSheet<StatisticsDto> sheet = new ExcelSheet<>();
//			ExcelMergeRows mergeRegions = new ExcelMergeRows();
//			sheet.setMergeRegions(mergeRegions);
//			sheet.addRows(exList);
//			sheet.setHeaders("상태", "건수");
//			sheet.setProperties("exSttus", "count");
//			sheet.setTitle(title);
//			sheet.setSheetName(title);
//			wb.addSheet(sheet);
//		}
		for (int i = 0; i < list.size(); i++){
			ExcelSheet<StatisticsDto> sheet = new ExcelSheet<>();
			ExcelMergeRows mergeRegions = new ExcelMergeRows();
			sheet.setMergeRegions(mergeRegions);
			sheet.addRow(list.get(i));
			sheet.setHeaders("title", "rate");
			sheet.setProperties("title", "rate");
			sheet.setTitle(list.get(0).getTitle());
			sheet.setSheetName(list.get(i).getTitle());
			wb.addSheet(sheet);
		}
		CommonReturn<Integer> entrprsCount =  new CommonReturn<>(tsptStatisticsDao.selectEntrprsCount(param.getBeginDt(), param.getEndDt()));
		ExcelSheet<CommonReturn<Integer>> sheet = new ExcelSheet<>();
		ExcelMergeRows mergeRegions = new ExcelMergeRows();
		sheet.setMergeRegions(mergeRegions);
		sheet.addRow(entrprsCount);
		sheet.setHeaders("건수", "빈칸");
		sheet.setProperties("value"," ");
		sheet.setTitle("장비 사용업체 수 ");
		sheet.setSheetName("사용업체 수");
		wb.addSheet(sheet);

		return ExcelView.getView(wb);
	}

}

