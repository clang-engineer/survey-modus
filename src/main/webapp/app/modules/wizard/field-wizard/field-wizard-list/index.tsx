import React, { useEffect, useState } from 'react';

import { useLocation, useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IForm } from 'app/shared/model/form.model';
import { getEntities as getFieldList } from 'app/entities/field/field.reducer';

import { Grid } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';

import { DragDropContext } from 'react-beautiful-dnd';
import { reorder } from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-dnd.utils';
import FieldWizardListLeft from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-list-left';

const FieldWizardList = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useAppDispatch();

  const { form } = location.state as { form: IForm };

  const fieldList = useAppSelector(state => state.field.entities);

  const [items, setItems] = useState([]);

  useEffect(() => {
    if (!form) {
      navigate('/wizard/form');
    }

    dispatch(getFieldList({ sort: 'id,desc', query: `formId.equals=${form.id}` }));
  }, []);

  useEffect(() => {
    setItems(fieldList);
  }, [fieldList]);

  const onDragEnd = result => {
    // dropped outside the list
    if (!result.destination) {
      return;
    }

    const reordered = reorder(items, result.source.index, result.destination.index);

    setItems(reordered as any);
  };

  return (
    <Grid container spacing={gridSpacing}>
      <DragDropContext onDragEnd={onDragEnd}>
        <Grid item xs={6}>
          <FieldWizardListLeft items={items} />
        </Grid>
      </DragDropContext>
    </Grid>
  );
};

export default FieldWizardList;
