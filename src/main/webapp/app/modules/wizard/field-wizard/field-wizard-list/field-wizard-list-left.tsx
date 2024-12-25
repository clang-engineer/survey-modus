import React from 'react';
import MainCard from 'app/berry/ui-component/cards/MainCard';
import { IField } from 'app/shared/model/field.model';

import { Box, Grid } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';

import { Droppable, Draggable } from 'react-beautiful-dnd';
import { getItemStyle, getListStyle } from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-dnd.utils';

interface IFieldWizardListLeftProps {
  items: any[];
}

const FieldWizardListLeft = (props: IFieldWizardListLeftProps) => {
  const { items } = props;

  return (
    <MainCard title={'left'}>
      <Grid container spacing={gridSpacing}>
        <Droppable droppableId="droppable">
          {(provided, snapshot) => (
            <div {...provided.droppableProps} ref={provided.innerRef} style={getListStyle(snapshot.isDraggingOver)}>
              {items.map((item, index) => (
                <Draggable key={item.id} draggableId={item.id} index={index}>
                  {(provided, snapshot) => (
                    <div
                      ref={provided.innerRef}
                      {...provided.draggableProps}
                      {...provided.dragHandleProps}
                      style={getItemStyle(snapshot.isDragging, provided.draggableProps.style)}
                    >
                      {item.content}
                    </div>
                  )}
                </Draggable>
              ))}
              {provided.placeholder}
            </div>
          )}
        </Droppable>
      </Grid>
    </MainCard>
  );
};

export default FieldWizardListLeft;
