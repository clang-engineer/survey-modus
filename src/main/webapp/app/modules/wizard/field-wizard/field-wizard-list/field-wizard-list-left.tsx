import React from 'react';

import { Box, ButtonGroup, Grid, IconButton, Typography } from '@mui/material';

import { Draggable, Droppable } from 'react-beautiful-dnd';
import { getItemStyle, getListStyle } from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-dnd.utils';
import { IField } from 'app/shared/model/field.model';

import { IconEdit, IconTrash } from '@tabler/icons';

import { create } from 'react-modal-promise';

import PromiseModal from 'app/shared/component/promise-modal';
import FieldWizardUpdateModal from 'app/modules/wizard/field-wizard/field-wizard-list/component/field-wizard-update.modal';

import { useTheme } from '@mui/material/styles';

interface IFieldItem extends IField {
  isNew: boolean;
}

interface IFieldWizardListLeftProps {
  items: IFieldItem[];
  handleDelete: (id: number) => void;
}

const EmptyDndBox = () => {
  return (
    <Grid item xs={12}>
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        sx={{
          height: 150,
          backgroundColor: 'rgba(0, 0, 0, 0.04)',
          borderRadius: 1,
          borderStyle: 'dashed',
          borderColor: 'rgba(0, 0, 0, 0.12)',
        }}
      >
        <Typography variant="h5">Drag and drop fields from the right side</Typography>
      </Box>
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

const FieldWizardListLeft = (props: IFieldWizardListLeftProps) => {
  const theme = useTheme();

  const { items } = props;

  const ItemTitle = (item: IFieldItem) => {
    return (
      <Box
        sx={{
          '& .MuiTypography-root': {
            color: item.isNew ? theme.palette.error.main : 'inherit',
          },
        }}
      >
        <Typography variant="h5">
          {item.title ? item.title : 'Untitled'}
          &nbsp;(type: {item.attribute.type})
        </Typography>
        <Typography variant="caption" color="text.secondary">
          {item.description ? item.description : 'No description'},
        </Typography>
      </Box>
    );
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
                      <ItemTitle {...item} />
                      <ButtonGroup size="small">
                        <IconButton
                          onClick={() => {
                            create(FieldWizardUpdateModal({ field: item }))().then(result => {
                              if (result) props.handleDelete(item.id);
                            });
                          }}
                        >
                          <IconEdit size={'1rem'} />
                        </IconButton>
                        <IconButton
                          onClick={() => {
                            deleteModal().then(result => {
                              if (result) props.handleDelete(item.id);
                            });
                          }}
                        >
                          <IconTrash size={'1rem'} />
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
