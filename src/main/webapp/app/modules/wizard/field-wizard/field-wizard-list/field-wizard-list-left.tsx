import React from 'react';
import MainCard from 'app/berry/ui-component/cards/MainCard';

import { Box, ButtonGroup, Grid, IconButton, Typography } from '@mui/material';

import { Draggable, Droppable } from 'react-beautiful-dnd';
import { getItemStyle, getListStyle } from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-dnd.utils';
import { IField } from 'app/shared/model/field.model';

import { IconEdit, IconTrash } from '@tabler/icons';

interface IFieldWizardListLeftProps {
  items: IField[];
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

const FieldWizardListLeft = (props: IFieldWizardListLeftProps) => {
  const { items } = props;

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
                      <Typography variant="h5">{item.title}</Typography>
                      <ButtonGroup size="small">
                        <IconButton>
                          <IconEdit size={'1rem'} />
                        </IconButton>
                        <IconButton
                          onClick={() => {
                            props.handleDelete(item.id);
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
