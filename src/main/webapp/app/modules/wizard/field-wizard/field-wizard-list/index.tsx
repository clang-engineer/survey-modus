import React, { useEffect } from 'react';

import { useLocation, useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IForm } from 'app/shared/model/form.model';
import { getEntities as getFieldList } from 'app/entities/field/field.reducer';

import { Grid } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';
import FieldWizardListLeft from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-list-left';
import FieldWizardListRight from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-list-right';

const FieldWizardList = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useAppDispatch();

  const { form } = location.state as { form: IForm };

  const fieldList = useAppSelector(state => state.field.entities);

  useEffect(() => {
    if (!form) {
      navigate('/wizard/form');
    }

    dispatch(getFieldList({ sort: 'id,desc', query: `formId.equals=${form.id}` }));
  }, []);

  return (
    <Grid container spacing={gridSpacing}>
      <Grid item xs={6}>
        <FieldWizardListLeft fieldList={fieldList} />
      </Grid>
      <Grid item xs={6}>
        <FieldWizardListRight />
      </Grid>
    </Grid>
  );
};

export default FieldWizardList;
