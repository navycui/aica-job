import { useEffect } from 'react';

type PropsType = {
  children?: any;
};

function Space({ children }: PropsType) {
  const init = () => {
    document.querySelector('body')!.classList.add('layout--space');
    return () => {
      document.querySelector('body')!.classList.remove('layout--space');
    };
  };
  useEffect(init, []);

  return children;
}

export default Space;
