import '../src/styles/global.scss';

import theme from '../src/theme';
import { ThemeProvider } from '@mui/material';

export const parameters = {};
export const decorators = [
  (Story) => (
    <>
      <ThemeProvider theme={theme}>{Story()}</ThemeProvider>
    </>
  ),
];
