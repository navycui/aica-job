import Toolbar from './Toolbar';
import Menubar from './Menubar';

import { Fragment } from 'react';
import Lnbnavbar from './Lnbnavbar';
function Desktop() {
  return (
    <Fragment>
      <header>
        <Toolbar />
        {/*<Menubar />*/}
      </header>
      <Lnbnavbar />
    </Fragment>
  );
}
export default Desktop;
