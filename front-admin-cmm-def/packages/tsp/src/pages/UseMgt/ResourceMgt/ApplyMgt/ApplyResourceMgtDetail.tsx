import { TabPanel } from '@mui/lab';
import React from 'react'
import { CustomTabs, SubContents, TitleContents } from '~/../../shared/src/components/LayoutComponents';
import { ApplyResourceinfo } from './Detail/ApplyResourceDetailInfo';
import { ApplyResourcehistinfo } from './Detail/ApplyResourcehistinfo';

const ApplyResourceInformationDetail = () => {
  return <TitleContents title={"컴퓨팅자원신청 상세"}>
    <CustomTabs tabs={["신청정보", "처리이력"]}>
      <TabPanel value="신청정보" sx={{padding: "0", height:"100%"}}>
        <ApplyResourceinfo/>
      </TabPanel>
      <TabPanel value="처리이력" sx={{padding: "0", height:"100%"}}>
        <ApplyResourcehistinfo/>
      </TabPanel>
    </CustomTabs>
  </TitleContents>
}

export default ApplyResourceInformationDetail;