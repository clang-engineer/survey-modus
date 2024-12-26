import React from 'react';

import { Draggable, Droppable } from 'react-beautiful-dnd';

import { Box, Grid, Typography, IconButton } from '@mui/material';

import { useTheme } from '@mui/material/styles';
import SubCard from 'app/berry/ui-component/cards/SubCard';

import { IconDirectionHorizontal } from '@tabler/icons';
import type from 'app/shared/model/enumerations/type.model';

const FieldWizardListRight = () => {
  const theme = useTheme();

  const items = Object.values(type).map((item, index) => {
    return {
      id: index,
      title: item,
    };
  });

  return (
    <Box
      padding={1}
      sx={{
        borderRadius: 1,
        backgroundColor: theme.palette.warning.light,
      }}
    >
      <Droppable droppableId="right" isDropDisabled={true}>
        {(provided, snapshot) => (
          <Grid container {...provided.droppableProps} ref={provided.innerRef} spacing={1}>
            {items.map((item, index) => (
              <Grid
                item
                xs={12}
                md={6}
                xl={4}
                key={item.id}
                sx={{
                  '& .MuiCardContent-root ': {
                    padding: 2,
                  },
                }}
              >
                <Draggable key={item.id} draggableId={item.title} index={index}>
                  {(provided, snapshot) => (
                    <SubCard
                      ref={provided.innerRef}
                      {...provided.draggableProps}
                      {...provided.dragHandleProps}
                      // style={getItemStyle(snapshot.isDragging, provided.draggableProps.style)}
                      style={{
                        userSelect: 'none',
                        ...provided.draggableProps.style,
                      }}
                    >
                      <Typography
                        variant="body2"
                        sx={{
                          fontSize: 12,
                          fontWeight: 600,
                          // textDecoration: 'underline',
                          //italic, oblique, initial, inherit
                          fontStyle: 'oblique',
                          color: theme.palette.grey[800],
                        }}
                      >
                        <IconButton
                          size="small"
                          style={{
                            color: theme.palette.grey[800],
                          }}
                        >
                          <IconDirectionHorizontal />
                        </IconButton>
                        {item.title}
                      </Typography>
                    </SubCard>
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
