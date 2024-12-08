// eslint-disable-next-line

declare module '@mui/material/styles/createTypography' {
  export interface FontStyle
    extends Required<{
      textTransform: TextTransform;
      fontSize: string | number; // added string
    }> {}
  export interface FontStyleOptions extends Partial<FontStyle> {
    fontSize?: string | number; // added string
  }
  export type CustomVariant =
    | 'customInput'
    | 'mainContent'
    | 'menuCaption'
    | 'subMenuCaption'
    | 'commonAvatar'
    | 'smallAvatar'
    | 'mediumAvatar'
    | 'largeAvatar';

  export interface TypographyOptions extends Partial<Record<CustomVariant, TypographyStyleOptions> & FontStyleOptions> {
    customInput?: TypographyStyleOptions;
    mainContent?: TypographyStyleOptions;
    menuCaption?: TypographyStyleOptions;
    subMenuCaption?: TypographyStyleOptions;
    commonAvatar?: TypographyStyleOptions;
    smallAvatar?: TypographyStyleOptions;
    mediumAvatar?: TypographyStyleOptions;
    largeAvatar?: TypographyStyleOptions;
  }

  export interface Typography extends Record<CustomVariant, TypographyStyle>, FontStyle, TypographyUtils {
    customInput: TypographyStyle;
    mainContent: TypographyStyle;
    menuCaption: TypographyStyleOptions;
    subMenuCaption: TypographyStyleOptions;
    commonAvatar: TypographyStyle;
    smallAvatar: TypographyStyle;
    mediumAvatar: TypographyStyle;
    largeAvatar: TypographyStyle;
  }

  export interface TextTransform {}
}
