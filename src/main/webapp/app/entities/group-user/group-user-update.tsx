import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getGroups } from 'app/entities/group/group.reducer';
import { createEntity, getEntity, reset, updateEntity } from './group-user.reducer';

export const GroupUserUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const groups = useAppSelector(state => state.group.entities);
  const groupUserEntity = useAppSelector(state => state.groupUser.entity);
  const loading = useAppSelector(state => state.groupUser.loading);
  const updating = useAppSelector(state => state.groupUser.updating);
  const updateSuccess = useAppSelector(state => state.groupUser.updateSuccess);

  const handleClose = () => {
    navigate('/group-user' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getGroups({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...groupUserEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      group: groups.find(it => it.id.toString() === values.group.toString()),
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
          ...groupUserEntity,
          user: groupUserEntity?.user?.id,
          group: groupUserEntity?.group?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="exformmakerApp.groupUser.home.createOrEditLabel" data-cy="groupUserCreateUpdateHeading">
            <Translate contentKey="exformmakerApp.groupUser.home.createOrEditLabel">Create or edit a groupUser</Translate>
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
                  id="group-user-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                id="group-user-user"
                name="user"
                data-cy="user"
                label={translate('exformmakerApp.groupUser.user')}
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
              <ValidatedField
                id="group-user-group"
                name="group"
                data-cy="group"
                label={translate('exformmakerApp.groupUser.group')}
                type="select"
                required
              >
                <option value="" key="0" />
                {groups
                  ? groups.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/group-user" replace color="info">
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

export default GroupUserUpdate;
