import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Point from './point';
import UserPoint from './user-point';
import Group from './group';
import Company from './company';
import Category from './category';
import Form from './form';
import Field from './field';
import File from './file';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="category/*" element={<Category />} />
        <Route path="form/*" element={<Form />} />
        <Route path="field/*" element={<Field />} />
        <Route path="company/*" element={<Company />} />
        <Route path="group/*" element={<Group />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
        <Route path="point/*" element={<Point />} />
        <Route path="user-point/*" element={<UserPoint />} />
        <Route path="file/*" element={<File />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};
