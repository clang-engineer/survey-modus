import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-company.reducer';

export const UserCompanyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userCompanyEntity = useAppSelector(state => state.userCompany.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userCompanyDetailsHeading">
          <Translate contentKey="exformmakerApp.userCompany.detail.title">UserCompany</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userCompanyEntity.id}</dd>
          <dt>
            <Translate contentKey="exformmakerApp.userCompany.user">User</Translate>
          </dt>
          <dd>{userCompanyEntity.user ? userCompanyEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="exformmakerApp.userCompany.company">Company</Translate>
          </dt>
          <dd>{userCompanyEntity.company ? userCompanyEntity.company.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-company" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-company/${userCompanyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserCompanyDetail;
