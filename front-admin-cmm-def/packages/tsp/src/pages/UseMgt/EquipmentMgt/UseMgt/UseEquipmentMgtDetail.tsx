import React, {useState} from 'react'
import {CustomTabs, TitleContents} from "shared/components/LayoutComponents";
import {TabPanel} from "@mui/lab";
import {UseEquipmentExtendPeriodMgt} from "~/pages/UseMgt/EquipmentMgt/UseMgt/Detail/UseEquipmentExtendPeriodMgt";
import {UseEquipmentApplyInfo} from "~/pages/UseMgt/EquipmentMgt/UseMgt/Detail/UseEquipmentApplyInfo";
import {UseEquipmentHistory} from "~/pages/UseMgt/EquipmentMgt/UseMgt/Detail/UseEquipmentHistory";
import {UseEquipmentUsePaymentHistory} from "~/pages/UseMgt/EquipmentMgt/UseMgt/Detail/UseEquipmentUsePaymentHistory";
import {EqpmnUseDetail} from "~/service/Model";

const UseEquipmentMgtDetail = () => {
  const [state, setState] = useState<EqpmnUseDetail>()
  return <TitleContents title={"장비사용 상세"}>
    <CustomTabs tabs={["신청정보", "기간연장관리", "사용료부과내역", "처리이력"]}>
      <TabPanel value="신청정보" sx={{padding: "0",height: '100%'}}>
        <UseEquipmentApplyInfo setState={setState}/>
      </TabPanel>
      <TabPanel value="기간연장관리" sx={{padding: "0",height: '100%'}}>
        <UseEquipmentExtendPeriodMgt state={state}/>
      </TabPanel>
      <TabPanel value="사용료부과내역" sx={{padding: "0",height: '100%'}}>
        <UseEquipmentUsePaymentHistory/>
      </TabPanel>
      <TabPanel value="처리이력" sx={{padding: "0",height: '100%'}}>
        <UseEquipmentHistory/>
      </TabPanel>
    </CustomTabs>
  </TitleContents>
}

export default UseEquipmentMgtDetail