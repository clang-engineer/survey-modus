import React from 'react';
import { Route, useLocation } from 'react-router-dom';
import Loadable from 'react-loadable';

import Login from 'app/modules/login/login';
import Register from 'app/modules/account/register/register';
import Activate from 'app/modules/account/activate/activate';
import PasswordResetInit from 'app/modules/account/password-reset/init/password-reset-init';
import PasswordResetFinish from 'app/modules/account/password-reset/finish/password-reset-finish';
import Logout from 'app/modules/login/logout';
import Home from 'app/modules/home/home';
import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import PageNotFound from 'app/shared/error/page-not-found';
import { AUTHORITIES } from 'app/config/constants';
import { sendActivity } from 'app/config/websocket-middleware';
import MainLayout from 'app/berry/layout/MainLayout';
import JhLayout from 'app/shared/layout/jh-layout';
import Login3 from 'app/berry/views/pages/authentication/authentication3/Login3';

const loading = <div>loading ...</div>;

const AccountRoutes = Loadable({
  loader: () => import(/* webpackChunkName: "account" */ 'app/modules/account'),
  loading: () => loading,
});

const AdministrationRoutes = Loadable({
  loader: () => import(/* webpackChunkName: "administration" */ 'app/modules/administration'),
  loading: () => loading,
});

const WizardRoutes = Loadable({
  loader: () => import(/* webpackChunkName: "wizard" */ 'app/modules/wizard'),
  loading: () => loading,
});

const EntityRoutes = Loadable({
  loader: () => import(/* webpackChunkName: "entity" */ 'app/entities/routes'),
  loading: () => loading,
});

const GateRoutes = Loadable({
  loader: () => import(/* webpackChunkName: "gate" */ 'app/modules/gate'),
  loading: () => loading,
});

const AppRoutes = () => {
  const location = useLocation();
  React.useEffect(() => {
    sendActivity(location.pathname);
  }, [location]);
  return (
    <div className="view-routes">
      <ErrorBoundaryRoutes>
        <Route element={<JhLayout />}>
          <Route index element={<Home />} />
          <Route path="login" element={<Login />} />
          <Route path="logout" element={<Logout />} />
          <Route path="account">
            <Route
              path="*"
              element={
                <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER]}>
                  <AccountRoutes />
                </PrivateRoute>
              }
            />
            <Route path="register" element={<Register />} />
            <Route path="activate" element={<Activate />} />
            <Route path="reset">
              <Route path="request" element={<PasswordResetInit />} />
              <Route path="finish" element={<PasswordResetFinish />} />
            </Route>
          </Route>
        </Route>
        {/* management routes */}
        <Route
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER]}>
              <MainLayout />
            </PrivateRoute>
          }
        >
          <Route
            path="wizard/*"
            element={
              <PrivateRoute hasAnyAuthorities={[AUTHORITIES.USER]}>
                <WizardRoutes />
              </PrivateRoute>
            }
          />
          <Route
            path="admin/*"
            element={
              <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN]}>
                <AdministrationRoutes />
              </PrivateRoute>
            }
          />
          <Route
            path="entities/*"
            element={
              <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN]}>
                <EntityRoutes />
              </PrivateRoute>
            }
          />
        </Route>
        <Route
          path="gate/*"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN]}>
              <GateRoutes />
            </PrivateRoute>
          }
        />
        <Route path="gate/login" element={<Login3 />} />
        <Route path="*" element={<PageNotFound />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};

export default AppRoutes;
