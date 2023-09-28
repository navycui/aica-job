import {Loader, middleware, RouteType} from "shared/utils/RouteUtiles";
import React from "react";


export const MainRoutes: RouteType[] = [
  {
      path: '/tsp-admin',
    index: true,
    element: (
      <Loader
        route={middleware(
          {
            label: 'dashboard',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/Dashboard/Dashboard")),
          },
          ['auth']
        )}
      />
    ),
  }, {
    path: '/tsp-admin/EquipmentMgt/InfomationMgt/:id',
    label: '장비정보상세',
    element: (
      <Loader
        route={middleware(
          {
            label: 'dashboard',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/EquipmentMgt/InfomationMgt/EquipmentInformationDetail")),
          },
          []
        )}
      />
    ),
  }, {
    path: '/tsp-admin/EquipmentMgt/EquipmentRegist',
    label: '장비등록',
    element: (
      <Loader
        route={middleware(
          {
            label: 'dashboard',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/EquipmentMgt/EquipmentRegist")),
          },
          []
        )}
      />
    ),
  }, {
    path: '/tsp-admin/UseMgt/ResourceMgt/ApplyMgt/:id',
    label: '컴퓨팅자원신청상세',
    element: (
      <Loader
        route={middleware(
          {
            label: 'ApplyResourceDetail',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/UseMgt/ResourceMgt/ApplyMgt/ApplyResourceMgtDetail")),
          },
          []
        )}
      />
    ),
  }, {
    path: '/tsp-admin/UseMgt/ResourceMgt/UseMgt/:id',
    label: '컴퓨팅자원사용상세',
    element: (
      <Loader
        route={middleware(
          {
            label: 'UseResourceDetail',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/UseMgt/ResourceMgt/UseMgt/UseResourceMgtDetail")),
          },
          []
        )}
      />
    ),
  }, {
    path: '/tsp-admin/UseMgt/EquipmentMgt/EstimationMgt/:id',
    label: '견적 요청 관리 상세',
    element: (
      <Loader
        route={middleware(
          {
            label: 'EstimationMgtDetail',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/UseMgt/EquipmentMgt/EstimationMgt/UseEquipmentEstimationDetail")),
          },
          []
        )}
      />
    )
  }, {
    path: '/tsp-admin/UseMgt/EquipmentMgt/UseMgt/:id',
    label: '장비 사용 상세',
    element: (
      <Loader
        route={middleware(
          {
            label: 'EquipmentUseMgtDetail',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/UseMgt/EquipmentMgt/UseMgt/UseEquipmentMgtDetail")),
          },
          []
        )}
      />
    ),
  }, {
    path: '/tsp-admin/UseMgt/EquipmentMgt/ApplyMgt/:id',
    label: '장비신청 상세',
    element: (
      <Loader
        route={middleware(
          {
            label: 'dashboard',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/UseMgt/EquipmentMgt/ApplyMgt/UseEquipmentApplyDetail")),
          },
          []
        )}
      />
    ),
  },
  {
    path: '/tsp-admin/UseMgt/EquipmentMgt/PeriodExtendMgt/:id',
    label: '기간 연장 신청 상세',
    element: (
      <Loader
        route={middleware(
          {
            label: 'dashboard',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/UseMgt/EquipmentMgt/PeriodExtendMgt/UseEquipmentPeriodExtendDetail")),
          },
          []
        )}
      />
    ),
  },
  {
    path: '/tsp-admin/UseMgt/EquipmentMgt/ReportMgt/:id',
    label: '결과보고서 상세',
    element: (
      <Loader
        route={middleware(
          {
            label: 'dashboard',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/UseMgt/EquipmentMgt/ReportMgt/UseEquipmentReportDetail")),
          },
          []
        )}
      />
    ),
  },
  {
    path: '/tsp-admin/UseMgt/AnalysisMgt/:id',
    label: '분석환경 상세',
    element: (
      <Loader
        route={middleware(
          {
            label: 'dashboard',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/UseMgt/AnalysisMgt/UseEquipmentAnalysisDetail")),
          },
          []
        )}
      />
    ),
  },
  {
    path: '/tsp-admin/Operation/About/Notice',
    label: '공지사항 관리',
    element: (
      <Loader
        route={middleware(
          {
            label: 'dashboard',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/Operation/CustomerSupportMgt/NoticeInfoMgt")),
          },
          []
        )}
      />
    ),
  },
  {
    path: '/tsp-admin/Operation/About/Notice/:id',
    label: '공지사항 상세',
    element: (
      <Loader
        route={middleware(
          {
            label: 'dashboard',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/Operation/CustomerSupportMgt/NoticeInfoMgt/NoticeInfoMgtDetail")),
          },
          []
        )}
      />
    ),
  },
  {
    path: '/tsp-admin/Operation/About/Notice/Register',
    label: '공지사항 등록',
    element: (
      <Loader
        route={middleware(
          {
            label: 'dashboard',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/Operation/CustomerSupportMgt/NoticeInfoMgt/NoticeInfoMgtRegi")),
          },
          []
        )}
      />
    ),
  },
  {
    path: '/tsp-admin/Operation/About/FAQ',
    label: '자주묻는질문관리',
    element: (
      <Loader
        route={middleware(
          {
            label: 'dashboard',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/Operation/CustomerSupportMgt/FrequentlyAskedQut")),
          },
          []
        )}
      />
    ),
  },
  {
    path: '/tsp-admin/Operation/About/FAQ/Register',
    label: '자주묻는질문 등록',
    element: (
      <Loader
        route={middleware(
          {
            label: 'dashboard',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/Operation/CustomerSupportMgt/FrequentlyAskedQut/FrequentlyAskedQutRegi")),
          },
          []
        )}
      />
    ),
  },
  {
    path: '/tsp-admin/Operation/About/FAQ/:id',
    label: '자주묻는질문 상세',
    element: (
      <Loader
        route={middleware(
          {
            label: 'dashboard',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/Operation/CustomerSupportMgt/FrequentlyAskedQut/FrequentlyAskedQutDetail")),
          },
          []
        )}
      />
    ),
  },
  {
    path: '/tsp-admin/Operation/About/Inquiry',
    label: '1:1문의 관리',
    element: (
      <Loader
        route={middleware(
          {
            label: 'dashboard',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/Operation/CustomerSupportMgt/InquiryMgt")),
          },
          []
        )}
      />
    ),
  },
  {
    path: '/tsp-admin/Operation/About/Inquiry/:id',
    label: '1:1문의 관리 상세',
    element: (
      <Loader
        route={middleware(
          {
            label: 'dashboard',
            layout: 'adminLayout',
            element: React.lazy(() => import("../pages/Operation/CustomerSupportMgt/InquiryMgt/InquiryMgtDetail")),
          },
          []
        )}
      />
    ),
  },
].map((route: Partial<RouteType>) => ({
  ...route,
  layout: 'space',
})) as RouteType[];