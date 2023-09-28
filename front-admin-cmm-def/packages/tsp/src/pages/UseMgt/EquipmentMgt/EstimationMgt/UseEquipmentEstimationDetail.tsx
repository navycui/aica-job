import React from 'react'
import {CustomTabs, TitleContents} from "shared/components/LayoutComponents";
import {TabPanel} from "@mui/lab";
import {EstimationApplyInfo} from "~/pages/UseMgt/EquipmentMgt/EstimationMgt/Detail/EstimationApplyInfo";
import {EstimationProcessHistory} from "~/pages/UseMgt/EquipmentMgt/EstimationMgt/Detail/EstimationProcessHistory";

const UseEquipmentEstimationDetail = () => {
  return <TitleContents title={"견적요청 상세"}>
    <CustomTabs tabs={["신청정보", "처리이력"]}>
      <TabPanel value="신청정보" sx={{padding: "0", height:'100%'}}>
        <EstimationApplyInfo/>
      </TabPanel>
      <TabPanel value="처리이력" sx={{padding: "0",height:'100%'}}>
        <EstimationProcessHistory/>
      </TabPanel>
    </CustomTabs>
  </TitleContents>
}

export default UseEquipmentEstimationDetail