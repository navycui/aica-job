import {useEffect, useState} from "react";
import {CodeGroup, CommonCode} from "~/service/Model";
import {CommonService} from "~/service/CommonService";

export const useCommtCode = (codeGroups: CodeGroup[]) => {
  const [commtCode,setCommtCode] = useState<CommonCode>()
  const common = CommonService.CommonCode(codeGroups)

  useEffect(() => {
    if (!common.isLoading && !common.isFetching) {
      if (!!common.data){
        setCommtCode(common.data)
      }
    }

  }, [common.data, common.isLoading, common.isFetching])

  return {
    commtCode
  }
}