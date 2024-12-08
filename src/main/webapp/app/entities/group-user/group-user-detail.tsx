import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './group-user.reducer';

export const GroupUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const groupUserEntity = useAppSelector(state => state.groupUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="groupUserDetailsHeading">
          <Translate contentKey="exformmakerApp.groupUser.detail.title">groupUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{groupUserEntity.id}</dd>
          <dt>
            <Translate contentKey="exformmakerApp.groupUser.user">User</Translate>
          </dt>
          <dd>{groupUserEntity.user ? groupUserEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="exformmakerApp.groupUser.group">Group</Translate>
          </dt>
          <dd>{groupUserEntity.group ? groupUserEntity.group.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/group-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/group-user/${groupUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GroupUserDetail;
