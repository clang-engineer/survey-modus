import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-point.reducer';

export const UserPointDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userPointEntity = useAppSelector(state => state.userPoint.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userPointDetailsHeading">
          <Translate contentKey="surveyModusApp.userPoint.detail.title">UserPoint</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userPointEntity.id}</dd>
          <dt>
            <Translate contentKey="surveyModusApp.userPoint.user">User</Translate>
          </dt>
          <dd>{userPointEntity.user ? userPointEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="surveyModusApp.userPoint.point">Point</Translate>
          </dt>
          <dd>{userPointEntity.point ? userPointEntity.point.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-point" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-point/${userPointEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserPointDetail;
