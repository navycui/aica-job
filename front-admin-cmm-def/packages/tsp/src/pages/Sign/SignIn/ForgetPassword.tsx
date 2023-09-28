import React, {Dispatch, SetStateAction} from 'react'
import TextField from "@mui/material/TextField";
import {VerticalInterval} from "shared/components/LayoutComponents";
import Button from "@mui/material/Button";

export const ForgetPassword = ( props: {
  setIsForgetPwd: Dispatch<SetStateAction <boolean>>
}) => {
  return <>
    <h1>비밀번호 재설정</h1>
    <p>가입 시 입력한 회원정보를 입력해 주세요.</p>

    <VerticalInterval size={"10px"}/>
    <TextField
      fullWidth required id="id"
      variant="outlined"
      placeholder='아이디'
    />

    <VerticalInterval size={"10px"}/>
    <TextField
      fullWidth required id="name"
      variant="outlined"
      placeholder='이름'
    />

    <VerticalInterval size={"10px"}/>
    <TextField
      fullWidth required id="natalDay"
      variant="outlined"
      placeholder="생년월일 (입력 예:20220101)"
    />

    <VerticalInterval size={"10px"}/>
    <TextField
      fullWidth required id="phonNumber"
      variant="outlined"
      placeholder="휴대폰번호('-'제외한 숫자만 입력)"
    />

    <VerticalInterval size={"30px"}/>
    <Button
      fullWidth variant="contained" type="submit"
      sx={{height: 56, fontSize: "16px"}}
      onClick={() => {props.setIsForgetPwd(false)}}
    >
      확인
    </Button>
  </>
}