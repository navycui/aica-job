import React from 'react'
import {CustomTabs, TitleContents} from "shared/components/LayoutComponents";
import {TabPanel} from "@mui/lab";
import {AnalysisApplyInfo} from "~/pages/UseMgt/AnalysisMgt/Detail/AnalysisApplyInfo";
import {AnalysisProcessHistory} from "~/pages/UseMgt/AnalysisMgt/Detail/AnalysisProcessHistory";

const UseEquipmentAnalysisDetail = () => {
  return <TitleContents title={"분석환경 이용신청 상세"}>
    <CustomTabs tabs={["신청정보", "처리이력"]}>
      <TabPanel value="신청정보" sx={{padding: "0", height:'100%'}}>
        <AnalysisApplyInfo/>
      </TabPanel>
      <TabPanel value="처리이력" sx={{padding: "0",height:'100%'}}>
        <AnalysisProcessHistory/>
      </TabPanel>
    </CustomTabs>
  </TitleContents>
}

export default UseEquipmentAnalysisDetail