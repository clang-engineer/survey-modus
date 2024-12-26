import React, { useEffect, useState } from 'react';

import { useLocation, useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IForm } from 'app/shared/model/form.model';
import { getEntities as getFieldList } from 'app/entities/field/field.reducer';

import { Box, ButtonGroup, Grid, IconButton, Typography } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';

import { DragDropContext } from 'react-beautiful-dnd';
import { reorder } from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-dnd.utils';
import FieldWizardListLeft from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-list-left';
import FieldWizardListRight from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-list-right';
import SubCard from 'app/berry/ui-component/cards/SubCard';

import { IconArrowBackUp, IconDeviceFloppy, IconEye } from '@tabler/icons';
import { useTheme } from '@mui/material/styles';

import { create } from 'react-modal-promise';
import FieldWizardPreviewModal from 'app/modules/wizard/field-wizard/field-wizard-list/component/field-wizard-preview.modal';

const FieldWizardList = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useAppDispatch();
  const theme = useTheme();

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
        id: 'new' + Math.random(),
        title: null,
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

  const LeftTitle = () => {
    return (
      <Box display="flex" justifyContent="space-between" alignItems="center" width="100%">
        <Typography>
          Fields for form {form.title} ({form.category.title})
        </Typography>
        <ButtonGroup size="small">
          <IconButton
            color={'primary'}
            onClick={() => {
              create(
                FieldWizardPreviewModal({
                  form: form,
                  fields: items,
                })
              )().then(() => {
                console.log('modal closed');
              });
            }}
          >
            <IconEye size={'1rem'} />
          </IconButton>
          <IconButton color={'secondary'}>
            <IconDeviceFloppy size={'1rem'} />
          </IconButton>
          <IconButton
            style={{
              color: theme.palette.grey[800],
            }}
            onClick={() => {
              setItems(fieldList);
            }}
          >
            <IconArrowBackUp size={'1rem'} />
          </IconButton>
        </ButtonGroup>
      </Box>
    );
  };

  return (
    <Grid container spacing={gridSpacing}>
      <DragDropContext onDragEnd={onDragEnd}>
        <Grid item xs={8}>
          <SubCard title={<LeftTitle />}>
            <FieldWizardListLeft items={items} handleDelete={handleDelete} />
          </SubCard>
        </Grid>
        <Grid item xs={4}>
          <SubCard title={`Available fields`}>
            <FieldWizardListRight />
          </SubCard>
        </Grid>
      </DragDropContext>
    </Grid>
  );
};

export default FieldWizardList;
