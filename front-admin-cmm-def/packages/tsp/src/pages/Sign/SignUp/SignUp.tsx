import React, {ChangeEvent, useState} from "react"
import {useLocation, useNavigate} from "react-router-dom";
import {Box, Link, Select, Stack} from "@mui/material";
import styled from '@emotion/styled';
import {Color} from "shared/components/StyleUtils";

const SignUp = () => {
  const navigate = useNavigate();
  const location = useLocation();

  return <Box
    component="form"
    sx={{
      backgroundColor: Color.mainNavy,
      width: "100%", height: "100vh"
    }}>
    <SignUpContainer>

    </SignUpContainer>
  </Box>
}

const SignUpContainer = styled(Stack)`
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

export default SignUp