import React from 'react';
import MainCard from 'app/berry/ui-component/cards/MainCard';

import { Grid } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';

import { Draggable, Droppable } from 'react-beautiful-dnd';
import { getItemStyle, getListStyle } from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-dnd.utils';
import { IField } from 'app/shared/model/field.model';

interface IFieldWizardListLeftProps {
  items: IField[];
}

const FieldWizardListLeft = (props: IFieldWizardListLeftProps) => {
  const { items } = props;

  return (
    <MainCard title={'left'}>
      {/*<Grid container spacing={gridSpacing}>*/}
      <Droppable droppableId="droppable">
        {(provided, snapshot) => (
          <Grid container {...provided.droppableProps} ref={provided.innerRef} style={getListStyle(snapshot.isDraggingOver)}>
            {items.map((item, index) => (
              <Draggable key={item.id} draggableId={`draggable-${item.id}`} index={index}>
                {(provided, snapshot) => (
                  <Grid
                    item
                    xs={12}
                    ref={provided.innerRef}
                    {...provided.draggableProps}
                    {...provided.dragHandleProps}
                    style={getItemStyle(snapshot.isDragging, provided.draggableProps.style)}
                  >
                    {item.title}
                  </Grid>
                )}
              </Draggable>
            ))}
            {provided.placeholder}
          </Grid>
        )}
      </Droppable>
      {/*</Grid>*/}
    </MainCard>
  );
};

export default FieldWizardListLeft;
