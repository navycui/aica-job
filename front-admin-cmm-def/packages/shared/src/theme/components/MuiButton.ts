declare module '@mui/material/Button' {
  interface ButtonPropsVariantOverrides {
    test: true;
  }
}

import { Components } from '@mui/material';

const theme: Components['MuiButton'] = {
  variants: [
    {
      props: { variant: 'test' },
      style: {
        textTransform: 'none',
        border: `2px dashed red`,
      },
    },
  ],
};

export default theme;
