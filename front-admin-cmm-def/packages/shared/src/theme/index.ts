import { createTheme } from '@mui/material';
import breakpoints from './breakpoints';
import MuiButton from './components/MuiButton';
export default createTheme({
  breakpoints,
  components: {
    MuiButton,
  },
});
