import React, { useState } from 'react';
import MainCard from 'app/berry/ui-component/cards/MainCard';

import { Draggable, Droppable } from 'react-beautiful-dnd';

import { Box, Grid } from '@mui/material';
import { type } from 'app/shared/model/field.model';

import { useTheme } from '@mui/material/styles';

const FieldWizardListRight = () => {
  const theme = useTheme();

  const [items, setItems] = useState(
    Object.values(type).map((item, index) => {
      return {
        id: index,
        title: item,
        type: item,
      };
    })
  );

  return (
    <Box
      padding={1}
      sx={{
        borderRadius: 1,
        backgroundColor: theme.palette.secondary.light,
      }}
    >
      <Droppable droppableId="right" isDropDisabled={true}>
        {(provided, snapshot) => (
          <Grid container {...provided.droppableProps} ref={provided.innerRef} spacing={1}>
            {items.map((item, index) => (
              <Grid item xs={6} key={item.id}>
                <Draggable key={item.id} draggableId={item.title} index={index}>
                  {(provided, snapshot) => (
                    <MainCard
                      ref={provided.innerRef}
                      {...provided.draggableProps}
                      {...provided.dragHandleProps}
                      // style={getItemStyle(snapshot.isDragging, provided.draggableProps.style)}
                      style={{
                        userSelect: 'none',
                        ...provided.draggableProps.style,
                      }}
                    >
                      {item.title}
                    </MainCard>
                  )}
                </Draggable>
              </Grid>
            ))}
            {provided.placeholder}
          </Grid>
        )}
      </Droppable>
    </Box>
  );
};

export default FieldWizardListRight;
