import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './group-company.reducer';

export const GroupCompanyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const groupCompanyEntity = useAppSelector(state => state.groupCompany.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="groupCompanyDetailsHeading">
          <Translate contentKey="exformmakerApp.groupCompany.detail.title">GroupCompany</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{groupCompanyEntity.id}</dd>
          <dt>
            <Translate contentKey="exformmakerApp.groupCompany.group">Group</Translate>
          </dt>
          <dd>{groupCompanyEntity.group ? groupCompanyEntity.group.title : ''}</dd>
          <dt>
            <Translate contentKey="exformmakerApp.groupCompany.company">Company</Translate>
          </dt>
          <dd>{groupCompanyEntity.company ? groupCompanyEntity.company.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/group-company" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/group-company/${groupCompanyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GroupCompanyDetail;
