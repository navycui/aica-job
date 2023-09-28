import {UseQueryResult} from "react-query";
import {UseEquipmentData, WithPagination} from "~/service/Model";
import {GetQuery} from "shared/libs/axios";

export class UseEquipmentMgtService {
    // 장비 사용목록 리스트 조회
    static getList(param: { page: number; rowCount: number; rowsPerPage: number}) : UseQueryResult<WithPagination<UseEquipmentData>, any> {
        param.page = param.page + 1;
        return GetQuery("/", param)
    }
}