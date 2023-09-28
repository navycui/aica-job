import React from 'react'
import {CustomTabs, TitleContents} from "shared/components/LayoutComponents";
import {TabPanel} from "@mui/lab";
import {UseEquipmentPeriodExtendInfo}from "~/pages/UseMgt/EquipmentMgt/PeriodExtendMgt/Detail/UseEquipmentPeriodExtendInfo";
import {UseEquipmentPeriodExtendHistory} from "~/pages/UseMgt/EquipmentMgt/PeriodExtendMgt/Detail/UseEquipmentPeriodExtendHistory";
import UseEquipmentApplyDetail from "~/pages/UseMgt/EquipmentMgt/ApplyMgt/UseEquipmentApplyDetail";

const UseEquipmentPeriodExtendDetail = () => {
    return <TitleContents title={"기간연장신청 상세"}>
        <CustomTabs tabs={["신청정보", "처리이력"]}>
            <TabPanel value="신청정보" sx={{padding: "0",height: '100%'}}>
                <UseEquipmentPeriodExtendInfo/>
            </TabPanel>
            <TabPanel value="처리이력" sx={{padding: "0",height: '100%'}}>
                <UseEquipmentPeriodExtendHistory/>
            </TabPanel>
        </CustomTabs>
    </TitleContents>
}
export default UseEquipmentPeriodExtendDetail