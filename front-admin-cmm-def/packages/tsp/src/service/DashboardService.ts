import {UseQueryResult} from "react-query";
import {BoardDataResponse, DashboardCount} from "~/service/Model";
import {GetQuery} from "shared/libs/axios";

export class DashboardService {
  static Notice(param: { page: number, rowsPerPage: number}): UseQueryResult<BoardDataResponse, any> {
    return GetQuery("/dashboard/list", {page: param.page + 1, itemsPerPage: param.rowsPerPage})
  }
  static DashboardData(): UseQueryResult<DashboardCount> {
    return GetQuery("/dashboard/count")
  }
}