import React from 'react'
import {CustomTabs, TitleContents} from "shared/components/LayoutComponents";
import {TabPanel} from "@mui/lab";
import {ReportEquipmentReportInfo} from "~/pages/UseMgt/EquipmentMgt/ReportMgt/Detail/ReportEquipmentReportInfo";
import {ReportEquipmentHistory} from "~/pages/UseMgt/EquipmentMgt/ReportMgt/Detail/ReportEquipmentHistory";

const UseEquipmentReportDetail = () => {
    return <TitleContents title={"결과보고서 상세"}>
     <CustomTabs tabs={['보고서정보', '처리이력']}>
        <TabPanel value="보고서정보" sx={{padding: "0", height:'100%'}}>
          <ReportEquipmentReportInfo/>
        </TabPanel>
        <TabPanel value="처리이력" sx={{padding: "0", height:'100%'}}>
          <ReportEquipmentHistory/>
        </TabPanel>
     </CustomTabs>
  </TitleContents>
}

export default UseEquipmentReportDetail