import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-group.reducer';

export const UserGroupDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userGroupEntity = useAppSelector(state => state.userGroup.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userGroupDetailsHeading">
          <Translate contentKey="exformmakerApp.userGroup.detail.title">UserGroup</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userGroupEntity.id}</dd>
          <dt>
            <Translate contentKey="exformmakerApp.userGroup.user">User</Translate>
          </dt>
          <dd>{userGroupEntity.user ? userGroupEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="exformmakerApp.userGroup.group">Group</Translate>
          </dt>
          <dd>{userGroupEntity.group ? userGroupEntity.group.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-group" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-group/${userGroupEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserGroupDetail;
