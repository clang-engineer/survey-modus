import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './group.reducer';

export const GroupDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const groupEntity = useAppSelector(state => state.group.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="groupDetailsHeading">
          <Translate contentKey="exformmakerApp.group.detail.title">Group</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{groupEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="exformmakerApp.group.title">Title</Translate>
            </span>
          </dt>
          <dd>{groupEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="exformmakerApp.group.description">Description</Translate>
            </span>
          </dt>
          <dd>{groupEntity.description}</dd>
          <dt>
            <span id="activated">
              <Translate contentKey="exformmakerApp.group.activated">Activated</Translate>
            </span>
          </dt>
          <dd>{groupEntity.activated ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="exformmakerApp.group.user">User</Translate>
          </dt>
          <dd>{groupEntity.user ? groupEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/group" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/group/${groupEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GroupDetail;
