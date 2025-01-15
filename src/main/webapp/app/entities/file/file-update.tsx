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
import { IFile } from 'app/shared/model/file.model';
import { level } from 'app/shared/model/enumerations/level.model';
import { getEntity, updateEntity, createEntity, reset } from './file.reducer';

export const FileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const fileEntity = useAppSelector(state => state.file.entity);
  const loading = useAppSelector(state => state.file.loading);
  const updating = useAppSelector(state => state.file.updating);
  const updateSuccess = useAppSelector(state => state.file.updateSuccess);
  const levelValues = Object.keys(level);

  const handleClose = () => {
    navigate('/entities/file' + location.search);
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
      ...fileEntity,
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
          ...fileEntity,
          user: fileEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="surveyModusApp.file.home.createOrEditLabel" data-cy="FileCreateUpdateHeading">
            <Translate contentKey="surveyModusApp.file.home.createOrEditLabel">Create or edit a File</Translate>
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
                  id="file-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('surveyModusApp.file.title')}
                id="file-title"
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
                label={translate('surveyModusApp.file.description')}
                id="file-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('surveyModusApp.file.activated')}
                id="file-activated"
                name="activated"
                data-cy="activated"
                check
                type="checkbox"
              />
              <ValidatedField label={translate('surveyModusApp.file.type')} id="file-type" name="type" data-cy="type" type="select">
                {levelValues.map(l => (
                  <option value={l} key={l}>
                    {translate('surveyModusApp.level.' + l)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="file-user"
                name="user"
                data-cy="user"
                label={translate('surveyModusApp.file.user')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/file" replace color="info">
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

export default FileUpdate;
