import React from 'react'
// import useMediaQuery from '@mui/material/useMediaQuery';
// import Mobile from './Mobile';
import Desktop from './Desktop';
import { useEffect } from 'react';

function Header() {
  // const isMobile = useMediaQuery('(max-width:850px)');
  const init = () => {
    return () => {};
  };
  useEffect(init, []);
  //* 1280 미만 모바일 버전
  // return <>{isMobile ? <Mobile /> : <Desktop />}</>;
  return <Desktop />;
}

export default Header;
