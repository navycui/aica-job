import * as React from 'react';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Lnbnavbar from './Lnbnavbar';
import Grid from '@mui/material/Grid';
import { styled } from '@mui/material/styles';
import Paper from '@mui/material/Paper';
import Breadcrumbs from './Breadcrumbs';

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}
const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
  ...theme.typography.body2,
  textAlign: 'center',
  color: theme.palette.text.secondary,
}));

function TabPanel(props: TabPanelProps) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box sx={{ p: 0 }}>
          <Typography>{children}</Typography>
        </Box>
      )}
    </div>
  );
}

function a11yProps(index: number) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}

export default function TabNavbar() {
  const [value, setValue] = React.useState(0);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  return (
    <Box sx={{ width: '100%' }}>
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs value={value} onChange={handleChange} aria-label="basic tabs example">
          <Tab label="사업정보관리" {...a11yProps(0)} />
          <Tab label="공고/접수"    {...a11yProps(1)} />
          <Tab label="평가/선정"    {...a11yProps(2)} />
          <Tab label="협약"         {...a11yProps(3)} />
          <Tab label="과제관리"     {...a11yProps(4)} />
          <Tab label="성과관리"     {...a11yProps(5)} />
          <Tab label="교육관리"     {...a11yProps(6)} />
          <Tab label="입주시설관리" {...a11yProps(7)} />
          <Tab label="자원할당관리" {...a11yProps(8)} />
          <Tab label="운영관리"     {...a11yProps(9)} />
          <Tab label="시스템관리"   {...a11yProps(10)} />
        </Tabs>
      </Box>
      <TabPanel value={value} index={0}>
        <Grid container sx={{padding:0}}>
          <Grid item xs={2} md={2}>
            <Item>
              <Lnbnavbar />
            </Item>
          </Grid>
          <Grid item xs={10} md={10}>
            <Item>          
              <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
                <Breadcrumbs />
                <Typography paragraph>
                  Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod
                  tempor incididunt ut labore et dolore magna aliqua. Rhoncus dolor purus non
                  enim praesent elementum facilisis leo vel. Risus at ultrices mi tempus
                  imperdiet. Semper risus in hendrerit gravida rutrum quisque non tellus.
                  Convallis convallis tellus id interdum velit laoreet id donec ultrices.
                  Odio morbi quis commodo odio aenean sed adipiscing. Amet nisl suscipit
                  adipiscing bibendum est ultricies integer quis. Cursus euismod quis viverra
                  nibh cras. Metus vulputate eu scelerisque felis imperdiet proin fermentum
                  leo. Mauris commodo quis imperdiet massa tincidunt. Cras tincidunt lobortis
                  feugiat vivamus at augue. At augue eget arcu dictum varius duis at
                  consectetur lorem. Velit sed ullamcorper morbi tincidunt. Lorem donec massa
                  sapien faucibus et molestie ac.
                </Typography>
              </Box>
            </Item>
          </Grid>
        </Grid>
      </TabPanel>
      <TabPanel value={value} index={1}>
        <Grid container sx={{padding:0}}>
          <Grid item xs={2} md={2}>
            <Item>
              <Lnbnavbar />
            </Item>
          </Grid>
          <Grid item xs={10} md={10}>
            <Item>          
              <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
                <Breadcrumbs />
                <Typography paragraph>
                  Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod
                  tempor incididunt ut labore et dolore magna aliqua. Rhoncus dolor purus non
                  enim praesent elementum facilisis leo vel. Risus at ultrices mi tempus
                  imperdiet. Semper risus in hendrerit gravida rutrum quisque non tellus.
                  Convallis convallis tellus id interdum velit laoreet id donec ultrices.
                  Odio morbi quis commodo odio aenean sed adipiscing. Amet nisl suscipit
                  adipiscing bibendum est ultricies integer quis. Cursus euismod quis viverra
                  nibh cras. Metus vulputate eu scelerisque felis imperdiet proin fermentum
                  leo. Mauris commodo quis imperdiet massa tincidunt. Cras tincidunt lobortis
                  feugiat vivamus at augue. At augue eget arcu dictum varius duis at
                  consectetur lorem. Velit sed ullamcorper morbi tincidunt. Lorem donec massa
                  sapien faucibus et molestie ac.
                </Typography>
              </Box>
            </Item>
          </Grid>
        </Grid>
      </TabPanel>
      <TabPanel value={value} index={2}>
        Item Three
      </TabPanel>
      <TabPanel value={value} index={3}>
        Item four
      </TabPanel>
      <TabPanel value={value} index={4}>
        Item five
      </TabPanel>
      <TabPanel value={value} index={5}>
        Item six
      </TabPanel>
      <TabPanel value={value} index={6}>
        Item seven
      </TabPanel>
      <TabPanel value={value} index={7}>
        Item eight
      </TabPanel>
      <TabPanel value={value} index={8}>
        Item night
      </TabPanel>
      <TabPanel value={value} index={9}>
        Item Ten
      </TabPanel>
      <TabPanel value={value} index={10}>
        Item eleven
      </TabPanel>
    </Box>
  );
}
