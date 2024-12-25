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
import FieldWizardListRight from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-list-right';

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

  const onDragEnd = event => {
    const { source, destination } = event;
    // dropped outside the list
    if (!event.destination) {
      return;
    }

    if (source.droppableId == 'right' && destination.droppableId == 'left') {
      const type = event.draggableId;
      const item = {
        id: items.length + 1,
        title: `new ${type} field (update title)`,
        description: null,
        activated: true,
        form: form,
        attribute: {
          type: type,
        },
      };

      setItems([...items.slice(0, event.destination.index), item, ...items.slice(event.destination.index, items.length)]);
    } else if (source.droppableId == 'left' && destination.droppableId == 'left') {
      const reordered = reorder(items, event.source.index, event.destination.index);
      setItems(reordered as any);
    }
  };

  const handleDelete = id => {
    setItems(items.filter(item => item.id !== id));
  };

  return (
    <Grid container spacing={gridSpacing}>
      <DragDropContext onDragEnd={onDragEnd}>
        <Grid item xs={8}>
          <FieldWizardListLeft items={items} handleDelete={handleDelete} />
        </Grid>
        <Grid item xs={4}>
          <FieldWizardListRight />
        </Grid>
      </DragDropContext>
    </Grid>
  );
};

export default FieldWizardList;
