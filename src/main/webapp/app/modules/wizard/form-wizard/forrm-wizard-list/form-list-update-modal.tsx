import React, { useState } from 'react';

import { Box, Button, ButtonGroup, Dialog, DialogActions, DialogContent, Switch, Typography } from '@mui/material';
import useFormWizardConfig from 'app/modules/wizard/form-wizard/form-wizard.config';

import { DragDropContext, Draggable, Droppable } from 'react-beautiful-dnd';
import { reorder } from 'app/shared/util/dnd-utils';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useTheme } from '@mui/material/styles';
import useConfig from 'app/berry/hooks/useConfig';

const FormListUpdateModal = React.forwardRef((props, ref) => {
  React.useImperativeHandle(ref, () => ({
    open: handleOpen,
    close: handleClose,
  }));

  const theme = useTheme();

  const { borderRadius } = useConfig();

  const dndGap = 10;

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
    const newItems = reorder(localItems, source.index, destination.index);
    setLocalItems(newItems);
  };

  return (
    <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
      <DialogContent
        style={{
          padding: 0,
        }}
      >
        <DragDropContext onDragEnd={onDragEnd}>
          <Droppable droppableId="form-droppable">
            {(provided, snapshot) => (
              <Box
                ref={provided.innerRef}
                style={{
                  background: snapshot.isDraggingOver ? theme.palette.grey[200] : theme.palette.background.default,
                  padding: dndGap,
                  width: 500,
                  minHeight: 500,
                }}
                {...provided.droppableProps}
              >
                {localItems.map((item, index) => (
                  <Draggable key={item.id} draggableId={item.id.toString()} index={index}>
                    {(provided, snapshot) => (
                      <Box
                        ref={provided.innerRef}
                        {...provided.draggableProps}
                        {...provided.dragHandleProps}
                        style={{
                          userSelect: 'none',
                          padding: dndGap,
                          margin: `0 0 ${dndGap}px 0`,
                          minHeight: '50px',
                          backgroundColor: snapshot.isDragging ? theme.palette.grey[300] : theme.palette.grey[100],
                          border: '1px solid #ccc',
                          borderRadius: borderRadius,
                          ...provided.draggableProps.style,
                        }}
                        display="flex"
                        justifyContent="space-between"
                        alignItems="center"
                        sx={{
                          '.MuiTypography-root': {
                            color: item.activated ? theme.palette.text.primary : theme.palette.text.disabled,
                            textDecoration: item.activated ? 'none' : 'line-through',
                          },
                        }}
                      >
                        <Typography variant="h6">{item.title}</Typography>
                        <Switch
                          size="small"
                          checked={item.activated}
                          onChange={e => {
                            const data = { ...localItems[index] };
                            data.activated = e.target.checked;

                            setLocalItems([...localItems.slice(0, index), data, ...localItems.slice(index + 1)]);
                          }}
                          name="activated"
                        />
                      </Box>
                    )}
                  </Draggable>
                ))}
                {provided.placeholder}
              </Box>
            )}
          </Droppable>
        </DragDropContext>
      </DialogContent>
      <DialogActions>
        <ButtonGroup size="small" variant="text" aria-label="text button group">
          <Button
            onClick={() => {
              setLocalItems(items);
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
