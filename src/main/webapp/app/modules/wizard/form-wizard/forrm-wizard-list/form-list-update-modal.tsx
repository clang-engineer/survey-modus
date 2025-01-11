import React, { useState } from 'react';

import { ButtonGroup, Dialog, DialogActions, DialogContent, IconButton, Button } from '@mui/material';
import useFormWizardConfig from 'app/modules/wizard/form-wizard/form-wizard.config';

import { DragDropContext, Draggable, Droppable } from 'react-beautiful-dnd';
import { reorder } from 'app/shared/util/dnd-utils';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const grid = 8;

const FormListUpdateModal = React.forwardRef((props, ref) => {
  React.useImperativeHandle(ref, () => ({
    open: handleOpen,
    close: handleClose,
  }));

  const [open, setOpen] = React.useState(false);

  const [localItems, setLocalItems] = useState([]);

  const { items, setItems } = useFormWizardConfig();

  React.useEffect(() => {
    setLocalItems(items);
  }, [items]);

  const handleClose = () => {
    setOpen(false);
  };

  const handleOpen = () => {
    setOpen(true);
  };

  const onDragEnd = event => {
    const { source, destination } = event;
    // dropped outside the list
    if (!event.destination) {
      return;
    }
    const newItems = reorder(items, source.index, destination.index);
    setLocalItems(newItems);
  };

  return (
    <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
      <DialogContent>
        <DragDropContext onDragEnd={onDragEnd}>
          <Droppable droppableId="form-droppable">
            {(provided, snapshot) => (
              <div
                ref={provided.innerRef}
                style={{
                  background: snapshot.isDraggingOver ? 'lightblue' : 'lightgrey',
                  padding: grid,
                  width: 250,
                  minHeight: 500,
                }}
                {...provided.droppableProps}
              >
                {localItems.map((item, index) => (
                  <Draggable key={item.id} draggableId={item.id.toString()} index={index}>
                    {(provided, snapshot) => (
                      <div
                        ref={provided.innerRef}
                        {...provided.draggableProps}
                        {...provided.dragHandleProps}
                        style={{
                          userSelect: 'none',
                          padding: grid,
                          margin: `0 0 ${grid}px 0`,
                          minHeight: '50px',
                          backgroundColor: snapshot.isDragging ? '#263B4A' : '#456C86',
                          color: 'white',
                          ...provided.draggableProps.style,
                        }}
                      >
                        {item.title}
                      </div>
                    )}
                  </Draggable>
                ))}
                {provided.placeholder}
              </div>
            )}
          </Droppable>
        </DragDropContext>
      </DialogContent>
      <DialogActions>
        <ButtonGroup size="small" variant="text" aria-label="text button group">
          <Button
            onClick={() => {
              setLocalItems(items);
              handleClose();
            }}
          >
            <FontAwesomeIcon icon="undo" />
          </Button>
          <Button
            onClick={() => {
              setItems(localItems);
              handleClose();
            }}
          >
            <FontAwesomeIcon icon="save" />
          </Button>
        </ButtonGroup>
      </DialogActions>
    </Dialog>
  );
});

export default FormListUpdateModal;
