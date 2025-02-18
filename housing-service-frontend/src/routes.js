import HouseDetails from './components/housing/HouseDetails';
import HouseForm from './components/housing/HouseForm';
import HouseList from './components/housing/HouseList';
import LandlordDetails from './components/landlord/LandlordDetails';
import LandlordForm from './components/landlord/LandlordForm';
import LandlordList from './components/landlord/LandlordList';
import FacilityReportList from './components/reports/FacilityReportList';
import ReportDetails from './components/reports/ReportDetails';
import ReportForm from './components/reports/ReportForm';

const routes = [
  // House Routes
  {
    path: '/houses',
    component: HouseList,
    protected: true
  },
  {
    path: '/houses/:id',
    component: HouseDetails,
    protected: true
  },
  {
    path: '/houses/new',
    component: HouseForm,
    protected: true
  },
  {
    path: '/houses/:id/edit',
    component: HouseForm,
    protected: true
  },

  {
    path: '/houses/new',
    element: <HouseForm isEditing={false} />,
    protected: true
  },
  {
    path: '/houses/:id/edit',
    element: <HouseForm isEditing={true} />,
    protected: true
  },

  // Report Routes
  {
    path: '/reports',
    component: FacilityReportList,
    protected: true
  },
  {
    path: '/reports/:id',
    component: ReportDetails,
    protected: true
  },
  {
    path: '/reports/new',
    component: ReportForm,
    protected: true
  },
  {
    path: '/houses/:houseId/reports',
    component: FacilityReportList,
    protected: true
  },

  // Landlord Routes
  {
    path: '/landlords',
    component: LandlordList,
    protected: true
  },
  {
    path: '/landlords/:id',
    component: LandlordDetails,
    protected: true
  },
  {
    path: '/landlords/new',
    component: LandlordForm,
    protected: true
  },
  {
    path: '/landlords/:id/edit',
    component: LandlordForm,
    protected: true
  }
];

export default routes;