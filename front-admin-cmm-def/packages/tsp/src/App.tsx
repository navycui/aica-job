import {ThemeProvider} from '@mui/material';
import {BrowserRouter} from 'react-router-dom';
import ErrorBoundary from 'shared/ErrorBoundary';
import theme from 'shared/theme';
import {QueryClient, QueryClientProvider} from "react-query";
import {LicenseInfo} from "@mui/x-license-pro";
import {MainRoutes} from "~/Routes/MainRoutes";
import {useEffect, useLayoutEffect, useState} from "react";
import {DynamicRouter} from "shared/DynamicRouter";
import Layout from "shared/layout";
import {useRouteStore} from "shared/Store/RouteConfig";
import {GlobalModals} from "~/pages/GlobalModals";
import {SignRoutes} from "~/Routes/SignRoutes";

LicenseInfo.setLicenseKey('4710a3119913b38e70c5bdde0aad559eT1JERVI6NDMwNTgsRVhQSVJZPTE2ODMyNTE3MDAwMDAsS0VZVkVSU0lPTj0x');
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnReconnect: false,
      refetchOnWindowFocus: false,
    }
  }
});

function App() {
  const config = useRouteStore();

  //test
  useEffect(() => {
    config.setMemberServer(`${process.env.REACT_APP_DOMAIN}`)
    config.setLayout('adminLayout')
    config.setDefaultLayout('adminLayout')
  }, [])

  return (
    <ErrorBoundary>
      <QueryClientProvider client={queryClient}>
        <ThemeProvider theme={theme}>
          <BrowserRouter>
            <Layout>
              <DynamicRouter
                portalType={"PORTAL_TAM"}
                addRoutes={[
                  ...MainRoutes,
                  ...SignRoutes,
                ]}/>
            </Layout>
          </BrowserRouter>
          <GlobalModals/>
        </ThemeProvider>
      </QueryClientProvider>
    </ErrorBoundary>
  );
}

export default App;
