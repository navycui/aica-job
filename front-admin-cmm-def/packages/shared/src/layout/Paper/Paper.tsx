import { useEffect } from 'react';

type PropsType = {
  children?: any;
};

function Space({ children }: PropsType) {
  const init = () => {
    document.querySelector('body')!.classList.add('layout--paper');
    return () => {
      document.querySelector('body')!.classList.remove('layout--paper');
    };
  };
  useEffect(init, []);

  return children;
}

export default Space;
