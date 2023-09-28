import React, {ChangeEvent, Dispatch, SetStateAction, useState} from "react"
import {useLocation, useNavigate} from "react-router-dom";
import {Box, Link, Select, Stack} from "@mui/material";
import styled from '@emotion/styled';
import {Color} from "shared/components/StyleUtils";
import TextField from "@mui/material/TextField";
import {VerticalInterval} from "shared/components/LayoutComponents";
import {CustomCheckBoxs} from "shared/components/ButtonComponents";
import Button from "@mui/material/Button";
import authentication from "shared/authentication";
import {SignService} from "~/service/SignService";
import {ForgetPassword} from "~/pages/Sign/SignIn/ForgetPassword";

const SignIn = () => {
  const [isForgetPwd, setIsForgetPwd] = useState(false)

  return <Box
    component="form"
    sx={{
      backgroundColor: Color.mainNavy,
      width: "100%", height: "100vh"
    }}>
    <SignInContainer>
      {
        isForgetPwd ?
          <ForgetPassword setIsForgetPwd={setIsForgetPwd}/> :
          <SignInView setIsForgetPwd={setIsForgetPwd}/>
      }
    </SignInContainer>
  </Box>
}


const SignInView = ( props: {
  setIsForgetPwd: Dispatch<SetStateAction <boolean>>
}) => {
  const navigate = useNavigate();
  const location = useLocation();
  const [isRemember, setIsRemember] = useState(() => {
    const value = localStorage.getItem("isAdminIdRemember")
    if (value) {
      return value === "true" ? true : false;
    }
    return false;
  })
  const [account, setAccount] = useState<{ id: string, pwd: string }>(() => {
    const id = localStorage.getItem("adminId")
    if (isRemember && id)
      return {id: id, pwd: ""}
    else
      return {id: "", pwd: ""}
  })

  const handlerLogin = async (e: React.MouseEvent<HTMLElement>) => {
    if (!validate(account)) return
    if (isRemember) {
      localStorage.setItem("adminId", account.id)
    }

    e.preventDefault();
    try {
      const data = await SignService.SignIn({loginId: account.id, passwd: account.pwd})

      if (data.success) {
        authentication.set(data)

        const qs = new URLSearchParams(location.search);
        const next = qs.get('nextUrl');
        if (next) {
          const path = window.atob(next).split(window.location.host).at(-1)
          navigate(path || '/');
        } else {
          navigate('/tsp-admin');
        }
      }
    } catch (e: any) {
      if (!!e.response && !!e.response.data) alert(e.response?.data.message);
    }
  }

  const handlerChange = (e: ChangeEvent<HTMLInputElement>) => {
    const {value, name} = e.target;
    setAccount({
      ...account,
      [name]: value
    });
  }

  return <>
    <img width={"180px"} src="/tsp-admin/images/common/logo_signin.png"/>
    <h1>관리자 시스템 로그인</h1>

    <VerticalInterval size={"30px"}/>
    <TextField
      required autoFocus fullWidth
      id={"SignId"} name={"id"}
      value={account.id}
      placeholder={"로그인"}
      error={validation(account.id)}
      onChange={handlerChange}/>

    <VerticalInterval size={"10px"}/>
    <TextField
      required autoFocus fullWidth
      id={"SignPwd"} name={"pwd"}
      type={"password"}
      value={account.pwd}
      placeholder={"비밀번호"}
      onChange={handlerChange}/>

    <Stack flexDirection={"row"} justifyContent={"space-between"} alignItems={"center"}
           sx={{pl: "5px", width: "100%"}}>
      <CustomCheckBoxs
        checkbox={["아이디 기억하기"]}
        defaultSelected={isRemember ? ["아이디 기억하기"] : []}
        onClick={(selected: string[]) => {
          if (selected.length > 0) {
            localStorage.setItem("isAdminIdRemember", "true")
            setIsRemember(true);
          } else {
            localStorage.setItem("isAdminIdRemember", "false")
            setIsRemember(false);
          }
        }}/>
      <Link href="#" underline="always" onClick={() => {
        props.setIsForgetPwd(true)
      }}>
        비밀번호 찾기
      </Link>
    </Stack>

    <VerticalInterval size={"15px"}/>
    <Button
      fullWidth variant="contained" type="submit"
      sx={{height: 56, fontSize: "16px"}}
      onClick={handlerLogin}
    >
      로그인
    </Button>

    {/*<VerticalInterval size={"30px"}/>*/}
    {/*<Link*/}
    {/*  href="#" underline="always"*/}
    {/*  onClick={() => {*/}
    {/*  }}>*/}
    {/*  회원가입*/}
    {/*</Link>*/}
  </>
}

// login form validation check
const validate = (values: { id: string, pwd: string }) => {
  // id 확인
  if (!values.id) {
    alert("로그인 id 입력하세요!");
    return false;
  }
  //비밀번호  확인
  if (!values.pwd) {
    alert("passwd 입력하세요!");
    return false;
    //비밀번호의 길이(length)가 4글자 이하일 때
  } else if (values.pwd.length < 4) {
    alert("비밀번호는 4자리이상으로 입력하세요");
    return false;
  }
  return true;
};

const validation = (value: string) => {
  let check = /[~!@#$%^&*()_+|<>?:{}.,/;='"ㄱ-ㅎ | ㅏ-ㅣ |가-힣]/;
  return check.test(value);
}

const SignInContainer = styled(Stack)`
  position: absolute;
  top: 50%;
  left: 50%;
  width: 580px;
  background-color: #fff;
  align-items: center;
  padding: 70px 100px;
  transform: translate(-50%, -50%);
  border-radius: 20px;
`;

export default SignIn