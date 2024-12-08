// eslint-disable-next-line
import { customShadows } from 'app/berry/themes/shadows';
import { Palette } from '@mui/material/styles/createPalette';
import { Typography, TypographyOptions } from '@mui/material/styles/createTypography';

declare module '@mui/material/styles' {
  export interface ThemeOptions {
    customShadows?: customShadows;
    customization?: TypographyOptions | ((palette: Palette) => TypographyOptions);
    darkTextSecondary?: string;
    textDark?: string;
    darkTextPrimary?: string;
    grey500?: string;
  }
  interface Theme {
    customShadows: customShadows;
    customization: Typography;
    darkTextSecondary: string;
    textDark: string;
    grey500: string;
    darkTextPrimary: string;
  }
}
