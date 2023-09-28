import React from 'react'
import {
  CustomTabs,
  TitleContents
} from "shared/components/LayoutComponents";
import {TabPanel} from "@mui/lab";
import {EquipmentMgtInfo} from "../../../pages/EquipmentMgt/InfomationMgt/Detail/EquipmentMgtInfo";
import {EquipmentDetailInfo} from "./Detail/EquipmentDetailInfo";
import {EquipmentMgtHist} from "~/pages/EquipmentMgt/InfomationMgt/Detail/EquipmentMgtHist";
import {EquipmentHistInfo} from "../../../pages/EquipmentMgt/InfomationMgt/Detail/EquipmentHistInfo";

/* 장비 정보 관리 상세 */
const EquipmentInformationDetail = () => {

  return <TitleContents title={"장비정보 상세"}>
    <CustomTabs tabs={["관리정보", "상세정보", "장비관리이력", "처리이력"]}>
      <TabPanel value="관리정보" sx={{padding: "0", height: '100%'}}>
        <EquipmentMgtInfo/>
      </TabPanel>
      <TabPanel value="상세정보" sx={{padding: "0", height: '100%'}}>
        <EquipmentDetailInfo/>
      </TabPanel>
      <TabPanel value="장비관리이력" sx={{padding: "0", height: '100%'}}>
        <EquipmentMgtHist />
      </TabPanel>
      <TabPanel value="처리이력" sx={{padding: "0", height: '100%'}}>
        <EquipmentHistInfo />
      </TabPanel>
    </CustomTabs>
  </TitleContents>
}


export default EquipmentInformationDetail;