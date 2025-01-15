import { styled } from '@mui/material/styles';
import { Grid } from '@mui/material';

const WizardStyledGrid = styled(Grid)<{ activated: boolean }>(({ theme, activated }) => ({
  '& .MuiTypography-root, & svg': {
    color: activated ? theme.palette.text.primary : theme.palette.error.main,
    textDecoration: activated ? 'none' : 'line-through',
  },
}));

export default WizardStyledGrid;
