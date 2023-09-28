import { Fragment, useEffect } from 'react';
import Portal from 'shared/components/Portal';
import Top from 'shared/components/Top';
import Header from './Header';
import Footer from './Footer';

type PropsType = {
  children?: any;
};

function Studio({ children }: PropsType) {
  const init = () => {
    document.querySelector('body')!.classList.add('layout--studio');
    return () => {
      document.querySelector('body')!.classList.remove('layout--studio');
    };
  };
  useEffect(init, []);

  return (
    <Fragment>
      <Header />
      {children}
      <Footer />
      <Portal>
        <Top />
      </Portal>
    </Fragment>
  );
}
export default Studio;
