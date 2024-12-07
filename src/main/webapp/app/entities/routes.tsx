import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Point from './point';
import UserPoint from './user-point';
import Group from './group';
import UserGroup from './user-group';
import Company from './company';
import UserCompany from './user-company';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="point/*" element={<Point/>}/>
        <Route path="user-point/*" element={<UserPoint />} />
        <Route path="group/*" element={<Group />} />
        <Route path="user-group/*" element={<UserGroup />} />
        <Route path="company/*" element={<Company />} />
        <Route path="user-company/*" element={<UserCompany />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
