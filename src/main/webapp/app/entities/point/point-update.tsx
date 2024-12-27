import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IPoint } from 'app/shared/model/point.model';
import { level } from 'app/shared/model/enumerations/level.model';
import { getEntity, updateEntity, createEntity, reset } from './point.reducer';

export const PointUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const pointEntity = useAppSelector(state => state.point.entity);
  const loading = useAppSelector(state => state.point.loading);
  const updating = useAppSelector(state => state.point.updating);
  const updateSuccess = useAppSelector(state => state.point.updateSuccess);
  const levelValues = Object.keys(level);

  const handleClose = () => {
    navigate('/point' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...pointEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          type: 'EASY',
          ...pointEntity,
          user: pointEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="surveymodusApp.point.home.createOrEditLabel" data-cy="PointCreateUpdateHeading">
            <Translate contentKey="surveymodusApp.point.home.createOrEditLabel">Create or edit a Point</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="point-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('surveymodusApp.point.title')}
                id="point-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: {
                    value: 20,
                    message: translate('entity.validation.minlength', { min: 20 }),
                  },
                  maxLength: {
                    value: 100,
                    message: translate('entity.validation.maxlength', { max: 100 }),
                  },
                }}
              />
              <ValidatedField
                label={translate('surveymodusApp.point.description')}
                id="point-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('surveymodusApp.point.activated')}
                id="point-activated"
                name="activated"
                data-cy="activated"
                check
                type="checkbox"
              />
              <ValidatedField label={translate('surveymodusApp.point.type')} id="point-type" name="type" data-cy="type" type="select">
                {levelValues.map(l => (
                  <option value={l} key={l}>
                    {translate('surveymodusApp.level.' + l)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="point-user"
                name="user"
                data-cy="user"
                label={translate('surveymodusApp.point.user')}
                type="select"
                required
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/point" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PointUpdate;
