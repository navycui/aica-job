import React, {Fragment, useEffect} from 'react';
import Toolbar from "./Toolbar";
import Footer from "./Footer";
import {Stack} from "@mui/material";
import Top from "../../components/Top/Top";
import Portal from "../../components/Portal";
import {LeftMenuBar} from "./LeftMenuBar";

const AdminLayout: React.FC<{}> = props => {
  const init = () => {
    document.querySelector('body')!.classList.add('layout--admin');
    return () => {
      document.querySelector('body')!.classList.remove('layout--admin');
    };
  };
  useEffect(init, []);
  return <Stack
    // justifyContent={"space-between"}
    style={{
      display: "flex",
      width: "100%",
      minHeight: "100vh",
    }}>
    <Toolbar/>
    <Stack flexDirection={"row"} style={{
      width: '100%',
      height: "100%",
      flexGrow: 1,
      overflow: 'auto'
      // overflow: 'auto'
    }}>
      <LeftMenuBar/>
      {props.children}
    </Stack>

    <Footer/>
    {/*<Portal>*/}
    {/*  <Top/>*/}
    {/*</Portal>*/}
  </Stack>
}

export default AdminLayout;
