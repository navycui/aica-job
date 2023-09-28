import { useEffect } from 'react';
import Box from "@mui/material/Box";

type PropsType = {
  children?: any;
};

function Empty({ children }: PropsType) {
  const init = () => {
    document.querySelector('body')!.classList.add('layout--empty');
    return () => {
      document.querySelector('body')!.classList.remove('layout--empty');
    };
  };
  useEffect(init, []);

  return <Box style={{
    display: "flex",
    width: "100%",
    minHeight: "100vh",
  }}>
    {children}
  </Box>;
}

export default Empty;
