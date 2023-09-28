import { useEffect } from 'react';

type PropsType = {
  children?: any;
};

function Promotion({ children }: PropsType) {
  const init = () => {
    document.querySelector('body')!.classList.add('layout--promotion');
    return () => {
      document.querySelector('body')!.classList.remove('layout--promotion');
    };
  };
  useEffect(init, []);

  return children;
}

export default Promotion;
