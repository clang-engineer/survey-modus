import React, { useState, useEffect } from 'react';
import { DragDropContext, Draggable, Droppable } from 'react-beautiful-dnd';
import { Box, Grid, IconButton, Typography } from '@mui/material';
import SubCard from 'app/berry/ui-component/cards/SubCard';
import { gridSpacing } from 'app/berry/store/constant';
import { CustomWidthTooltip } from 'app/shared/component/custom-toolip';
import { useAppSelector } from 'app/config/store';
import FormSubcardTitle from 'app/modules/wizard/form-wizard/forrm-wizard-list/form-subcard-title';
import { IconBook } from '@tabler/icons';

const slotProps = {
  tooltip: {
    sx: {
      color: '#514E6A',
      backgroundColor: '#ffff',
      padding: '10px',
      boxShadow: '0px 4px 16px rgba(0, 0, 0, 0.08)',
    },
  },
};

interface IDraggableGridProps {}

const FormDraggableGrid = () => {
  const formList = useAppSelector(state => state.form.entities);
  const [localForms, setForms] = useState(formList);

  useEffect(() => {
    setForms(formList);
  }, [formList]);

  const handleDragEnd = result => {
    if (!result.destination || result.source.index === result.destination.index) return;

    const reorderedForms = Array.from(localForms);
    const [movedItem] = reorderedForms.splice(result.source.index, 1);
    reorderedForms.splice(result.destination.index, 0, movedItem);

    setForms(reorderedForms);
  };

  return (
    <DragDropContext onDragEnd={handleDragEnd}>
      <Droppable droppableId="formList" direction="horizontal">
        {provided1 => (
          <Grid container spacing={gridSpacing} {...provided1.droppableProps} ref={provided1.innerRef}>
            {localForms.map((form, i) => (
              <Draggable key={form.id} draggableId={String(form.id)} index={i}>
                {provided2 => (
                  <Grid item xs={3} key={form.id} ref={provided2.innerRef} {...provided2.draggableProps} {...provided2.dragHandleProps}>
                    <SubCard title={<FormSubcardTitle form={form} />}>
                      <Grid container spacing={gridSpacing}>
                        <Grid item xs={12} marginBottom={2}>
                          <Typography variant="body1" color="text.primary">
                            {form.description}
                          </Typography>
                        </Grid>
                        <Grid item xs={12} display={'flex'} justifyContent={'flex-end'}>
                          <Box display="flex" alignItems="center">
                            <CustomWidthTooltip
                              title={
                                <>
                                  {form.category.title} <br />
                                  {form.category.description}
                                </>
                              }
                              sx={{ fontSize: '30' }}
                              slotProps={slotProps}
                            >
                              <IconButton>
                                <IconBook size={'15px'} strokeWidth={1.2} />
                              </IconButton>
                            </CustomWidthTooltip>
                            <Typography variant="caption" color="text.primary">
                              {form.category.title}
                            </Typography>
                          </Box>
                        </Grid>
                      </Grid>
                    </SubCard>
                  </Grid>
                )}
              </Draggable>
            ))}
          </Grid>
        )}
      </Droppable>
    </DragDropContext>
  );
};

export default FormDraggableGrid;
