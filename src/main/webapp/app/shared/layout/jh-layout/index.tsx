import React, { useEffect } from 'react';
import ErrorBoundary from 'app/shared/error/error-boundary';
import Header from 'app/shared/layout/header/header';
import Footer from 'app/shared/layout/footer/footer';
import { useAppDispatch } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import { getProfile } from 'app/shared/reducers/application-profile';
import { Card } from 'reactstrap';
import { Outlet } from 'react-router-dom';

const JhLayout = () => {
  const dispatch = useAppDispatch();

  const paddingTop = '60px';

  useEffect(() => {
    dispatch(getSession());
    dispatch(getProfile());
  }, []);

  return (
    <div className="app-container" style={{ paddingTop }}>
      <ErrorBoundary>
        <Header />
      </ErrorBoundary>
      <div className="container-fluid view-container" id="app-view-container">
        <Card className="jh-card">
          <ErrorBoundary>
            <Outlet />
          </ErrorBoundary>
        </Card>
        <Footer />
      </div>
    </div>
  );
};

export default JhLayout;
