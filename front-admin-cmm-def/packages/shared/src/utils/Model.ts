
export interface ErrorMessage {
  message: string
  field?: string
}

export interface BaseResponse {
  success: boolean,
  headers: string,
  errors?: ErrorMessage[]
}

export type WithResponse<T> = T & {
  success: boolean,
  headers: string,
  errors?: ErrorMessage[]
}

// 서버에서 nhn게이트웨이에 등록을 위해 오브젝트로 내려야 해서 list 혹은 value값을 던질때 사용
export type WithListResponse<T> = T & {
  success: boolean,
  headers: string,
  errors?: ErrorMessage[]
  list: T[]
}

export type WithValueResponse<T> = T & {
  success: boolean,
  headers: string,
  errors?: ErrorMessage[]
  value: T
}