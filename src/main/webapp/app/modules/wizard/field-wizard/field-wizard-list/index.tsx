import React, { useEffect } from 'react';

import { useLocation, useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IForm } from 'app/shared/model/form.model';
import { createAndUpdateEntities, getEntities as getFieldList } from 'app/entities/field/field.reducer';

import { Box, ButtonGroup, Grid, IconButton, Switch, Typography } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';

import { DragDropContext } from 'react-beautiful-dnd';
import { reorder } from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-dnd.utils';
import FieldWizardListLeft from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-list-left';
import FieldWizardListRight from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-list-right';
import SubCard from 'app/berry/ui-component/cards/SubCard';

import { IconArrowBackUp, IconChecklist, IconDeviceFloppy, IconEye, IconSettings } from '@tabler/icons';
import { useTheme } from '@mui/material/styles';

import { create } from 'react-modal-promise';
import SurveyModal from 'app/modules/survey-modal';
import { IField } from 'app/shared/model/field.model';
import useFieldWizardConfig from 'app/modules/wizard/field-wizard/field-wizard.config';
import FieldWizardUpdateModal from 'app/modules/wizard/field-wizard/field-wizard-list/component/field-wizard-update.modal';

const FieldWizardList = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useAppDispatch();
  const theme = useTheme();

  const { form } = location.state as { form: IForm };

  const fieldList = useAppSelector(state => state.field.entities);

  const { items, setItems } = useFieldWizardConfig();

  useEffect(() => {
    if (!form) {
      navigate('/wizard/form');
    }

    dispatch(getFieldList({ sort: 'id,desc', query: `formId.equals=${form.id}` }));
  }, []);

  useEffect(() => {
    setItems(
      fieldList
        .filter(a => a)
        .sort((a, b) => {
          return a.display?.orderNo - b.display?.orderNo;
        })
    );
  }, [fieldList]);

  const onDragEnd = event => {
    const { source, destination } = event;
    // dropped outside the list
    if (!event.destination) {
      return;
    }

    if (source.droppableId === 'right' && destination.droppableId === 'left') {
      const type = event.draggableId;
      const item = {
        id: Number(new Date()),
        title: null,
        description: null,
        activated: true,
        form,
        attribute: {
          type,
          defaultValue: null,
        },
        isNew: true,
      };

      const merged: IField[] = [...items.slice(0, event.destination.index), item, ...items.slice(event.destination.index, items.length)];

      const orderNumberReAssigned = getOrderNumberReAssignedList(merged);
      setItems(orderNumberReAssigned);

      create(
        FieldWizardUpdateModal({
          field: orderNumberReAssigned[event.destination.index],
          items: orderNumberReAssigned,
          setItems,
        })
      )();
    } else if (source.droppableId === 'left' && destination.droppableId === 'left') {
      const reordered: IField[] = reorder(items, event.source.index, event.destination.index);

      setItems(getOrderNumberReAssignedList(reordered));
    }
  };

  const getOrderNumberReAssignedList = (data: IField[]) => {
    return [...data].map((item, index) => {
      return {
        ...item,
        display: {
          ...item.display,
          orderNo: index,
        },
      };
    });
  };

  const onSaveButtonClick = () => {
    const fieldDTOs = items.map(d => {
      return d['isNew'] ? { ...d, id: null } : d;
    });
    dispatch(createAndUpdateEntities(fieldDTOs));
  };

  const LeftTitle = () => {
    return (
      <Box display="flex" justifyContent="space-between" alignItems="center" width="100%">
        <Box display="flex" justifyContent="flex-start" alignItems="center">
          <IconButton>
            <IconChecklist size={'1rem'} />
          </IconButton>
          <Typography>
            Fields for form {form.title} ({form.category.title})
          </Typography>
          <IconButton color={'primary'}>
            <Switch
              size="small"
              checked={items.filter(a => a.activated).length !== 0}
              color="secondary"
              onChange={e => {
                setItems(
                  items.map(a => {
                    return {
                      ...a,
                      activated: e.target.checked,
                    };
                  })
                );
              }}
            />
          </IconButton>
        </Box>
        <ButtonGroup size="small">
          <IconButton
            color={'primary'}
            onClick={() => {
              create(
                SurveyModal({
                  form,
                  fields: items.filter(a => a.activated),
                })
              )();
            }}
          >
            <IconEye size={'1rem'} />
          </IconButton>
          <IconButton color={'secondary'} onClick={onSaveButtonClick}>
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

  const RightTitle = () => {
    return (
      <Box display="flex" justifyContent="flex-start" alignItems="center" width="100%">
        <IconButton>
          <IconSettings size={'1rem'} />
        </IconButton>
        <Typography>Available fields</Typography>
      </Box>
    );
  };

  return (
    <Grid container spacing={gridSpacing}>
      <DragDropContext onDragEnd={onDragEnd}>
        <Grid item xs={12} md={8}>
          <SubCard title={<LeftTitle />}>
            <FieldWizardListLeft />
          </SubCard>
        </Grid>
        <Grid item xs={12} md={4}>
          <SubCard title={<RightTitle />}>
            <FieldWizardListRight />
          </SubCard>
        </Grid>
      </DragDropContext>
    </Grid>
  );
};

export default FieldWizardList;
