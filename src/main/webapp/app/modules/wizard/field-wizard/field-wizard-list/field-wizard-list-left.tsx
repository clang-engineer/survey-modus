import React from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { Box, ButtonGroup, Grid, IconButton, List, ListItem, ListItemText, Typography } from '@mui/material';

import { Draggable, Droppable } from 'react-beautiful-dnd';
import { getItemStyle, getListStyle } from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-dnd.utils';

import { IconCircle, IconPencil, IconTrash } from '@tabler/icons';

import { create } from 'react-modal-promise';

import PromiseModal from 'app/shared/component/promise-modal';
import FieldWizardUpdateModal from 'app/modules/wizard/field-wizard/field-wizard-list/component/field-wizard-update.modal';

import { useTheme } from '@mui/material/styles';
import NoContentBox from 'app/shared/component/no-content-box';
// import IFieldItem from "app/modules/wizard/field-wizard/field-wizard-list/field-item.model";
import { IField } from 'app/shared/model/field.model';
import useFieldWizardConfig from 'app/modules/wizard/field-wizard/field-wizard.config';
import AnimateButton from 'app/berry/ui-component/extended/AnimateButton';
import FieldWizardListLeftTitle from 'app/modules/wizard/field-wizard/field-wizard-list/component/field-wizard-list-left-title';

const EmptyDndBox = () => {
  return (
    <Grid item xs={12}>
      <NoContentBox title="Drag and drop fields from the right side" height={150} />
    </Grid>
  );
};

const deleteModal = create(
  PromiseModal({
    title: 'Delete Field',
    content: 'Do you want to delete this field?',
    rejectButtonText: 'Cancel',
    resolveButtonText: 'Delete',
  })
);

const FieldWizardListLeft = () => {
  const theme = useTheme();

  const { items, setItems } = useFieldWizardConfig();

  const handleDelete = id => {
    setItems(items.filter(item => item.id !== id));
  };

  return (
    <Droppable droppableId="left">
      {(provided, snapshot) => (
        <Grid container {...provided.droppableProps} ref={provided.innerRef} style={getListStyle(snapshot.isDraggingOver)}>
          {items.length === 0 ? (
            <Grid item xs={12}>
              {' '}
              <EmptyDndBox />{' '}
            </Grid>
          ) : (
            items.map((item, index) => (
              <Draggable key={item.id} draggableId={`draggable-left-${item.id}`} index={index}>
                {(provided, snapshot) => (
                  <Grid
                    item
                    xs={12}
                    ref={provided.innerRef}
                    {...provided.draggableProps}
                    {...provided.dragHandleProps}
                    style={getItemStyle(snapshot.isDragging, provided.draggableProps.style)}
                  >
                    <Box display="flex" justifyContent="space-between" alignItems="center">
                      <FieldWizardListLeftTitle item={item} index={index} />
                      <ButtonGroup size="small">
                        <IconButton
                          onClick={() => {
                            create(
                              FieldWizardUpdateModal({
                                field: item,
                                items,
                                setItems,
                              })
                            )();
                          }}
                        >
                          <IconPencil size={'1rem'} />
                        </IconButton>
                        <IconButton
                          onClick={() => {
                            deleteModal().then(result => {
                              if (result) handleDelete(item.id);
                            });
                          }}
                        >
                          <IconTrash size={'0.9rem'} />
                        </IconButton>
                      </ButtonGroup>
                    </Box>
                  </Grid>
                )}
              </Draggable>
            ))
          )}
          {provided.placeholder}
        </Grid>
      )}
    </Droppable>
  );
};

export default FieldWizardListLeft;
