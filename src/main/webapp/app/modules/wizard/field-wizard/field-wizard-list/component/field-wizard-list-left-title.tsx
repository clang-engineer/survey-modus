import React from 'react';
import { IField } from 'app/shared/model/field.model';
import AnimateButton from 'app/berry/ui-component/extended/AnimateButton';

import { Box, Collapse, IconButton, List, ListItem, ListItemIcon, ListItemText, Typography } from '@mui/material';

import { IconCircle } from '@tabler/icons';

import { useTheme } from '@mui/material/styles';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const FieldWizardListLeftTitle = (props: { item: IField; index: number }) => {
  const theme = useTheme();
  const { item, index } = props;

  const [color, setColor] = React.useState('inherit');
  const [visible, setVisible] = React.useState(false);

  React.useEffect(() => {
    if (item.activated === false) {
      setColor(theme.palette.error.main);
    } else if (item['isNew']) {
      setColor(theme.palette.primary.main);
    }
  }, [item.activated, item['isNew']]);

  const RENDER_ATTRIBUTES = [
    'id',
    'title',
    'description',
    'activated',
    // 'form',
    'attribute',
    'display',
    'lookups',
    // 'isNew'
  ];

  return (
    <Box
      sx={{
        '& .MuiTypography-root, & svg': {
          color: color,
          textDecoration: item.activated ? 'none' : 'line-through',
        },
      }}
    >
      <Typography variant="h5" sx={{ display: 'flex', alignItems: 'center' }}>
        <IconCircle size={'0.5rem'} fill={item.activated ? theme.palette.success.main : theme.palette.error.main} /> &nbsp; #{index + 1}
        .&nbsp;
        {item.title ? item.title : 'Untitled'} &nbsp;
        <AnimateButton>
          <IconButton
            onClick={() => {
              setVisible(!visible);
            }}
          >
            {visible ? (
              <FontAwesomeIcon icon={['far', 'folder-open']} size="2xs" />
            ) : (
              <FontAwesomeIcon icon={['far', 'folder']} size="2xs" />
            )}
          </IconButton>
        </AnimateButton>
      </Typography>
      <Collapse in={visible} timeout="auto" unmountOnExit>
        <List sx={{ padding: 0 }}>
          {Object.keys(item)
            .filter(key => RENDER_ATTRIBUTES.includes(key))
            .map((key, index) => (
              <ListItem key={index} sx={{ padding: 0 }}>
                <ListItemIcon
                  title={key}
                  sx={{
                    marginLeft: '10px',
                    minWidth: '20px',
                  }}
                >
                  <FontAwesomeIcon icon={['fas', 'check']} size="2xs" />
                </ListItemIcon>
                <ListItemText>
                  {key}: {JSON.stringify(item[key])}
                </ListItemText>
              </ListItem>
            ))}
        </List>
      </Collapse>
    </Box>
  );
};

export default FieldWizardListLeftTitle;
