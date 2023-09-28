import { TabPanel } from '@mui/lab';
import React from 'react'
import { CustomTabs, TitleContents } from '~/../../shared/src/components/LayoutComponents';
import { UseResourceinfo } from './Detail/UseResourceDetailinfo';
import { UseResourcehistinfo } from './Detail/UseResourcehistinfo';

const UseResourceInformationDetail = () => {
  return <TitleContents title={"컴퓨팅자원사용 상세"}>
    <CustomTabs tabs={["신청정보", "처리이력"]}>  
      <TabPanel value="신청정보" sx={{padding: "0", height:"100%"}}>
        <UseResourceinfo/>
      </TabPanel>
      <TabPanel value="처리이력" sx={{padding: "0", height:"100%"}}>
        <UseResourcehistinfo/>
      </TabPanel>
    </CustomTabs>
  </TitleContents>
}

export default UseResourceInformationDetail;