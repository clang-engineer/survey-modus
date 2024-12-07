import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Point from './point';
import Group from './group';
import UserPoint from './user-point';
import UserGroup from './user-group';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="point/*" element={<Point/>}/>
        <Route path="group/*" element={<Group />} />
        <Route path="user-point/*" element={<UserPoint />} />
        <Route path="user-group/*" element={<UserGroup />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
