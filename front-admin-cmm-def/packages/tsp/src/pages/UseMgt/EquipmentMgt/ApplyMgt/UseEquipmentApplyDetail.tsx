import React from 'react'
import {CustomTabs, TitleContents} from "shared/components/LayoutComponents";
import {TabPanel} from "@mui/lab";
import {ApplyEquipmentApplyInfo} from './Detail/ApplyEquipmentApplyInfo';
import {ApplyEquipmentHistory} from './Detail/ApplyEquipmentHistory';


const UseEquipmentApplyDetail = () => {
  return <TitleContents title={"장비신청 상세"}>
    <CustomTabs tabs={["신청정보", "처리이력"]}>
      <TabPanel value="신청정보" sx={{padding: "0", height: '100%'}}>
        <ApplyEquipmentApplyInfo/>
      </TabPanel>
      <TabPanel value="처리이력" sx={{padding: "0", height: '100%'}}>
        <ApplyEquipmentHistory/>
      </TabPanel>
    </CustomTabs>
  </TitleContents>
}

export default UseEquipmentApplyDetail