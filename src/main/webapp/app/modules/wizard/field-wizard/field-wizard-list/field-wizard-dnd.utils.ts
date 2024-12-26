import Palette from 'app/berry/themes/palette';
import config from 'app/berry/config';

// a little function to help us with reordering the result
const reorder = (list, startIndex, endIndex) => {
  const result = Array.from(list);
  const [removed] = result.splice(startIndex, 1);
  result.splice(endIndex, 0, removed);

  return result;
};

const grid = 8;

const getItemStyle = (isDragging, draggableStyle) => {
  const theme = Palette(config.navType, config.presetColor);

  return {
    // some basic styles to make the items look a bit nicer
    userSelect: 'none',
    padding: grid * 2,
    margin: `0 0 ${grid}px 0`,

    // change background colour if dragging
    // background: isDragging ? theme.palette.secondary.light : theme.palette.grey[50],
    background: theme.palette.background.paper,

    borderWidth: '1px',
    borderStyle: 'solid',
    borderRadius: '5px',
    borderColor: theme.palette.grey[300],

    ...draggableStyle,
  };
};

const getListStyle = isDraggingOver => {
  const theme = Palette(config.navType, config.presetColor);

  return {
    padding: grid,
    background: theme.palette.background.paper,
    borderWidth: '1px',
    borderStyle: 'dotted',
    borderRadius: '5px',
    shadow: '0 0 10px rgba(0, 0, 0, 0.5)',
    backgroundColor: isDraggingOver ? theme.palette.primary.light : theme.palette.grey[50],

    // background: isDraggingOver ? theme.palette.primary.light : theme.palette.grey[100],
  };
};

export { reorder, getItemStyle, getListStyle };
