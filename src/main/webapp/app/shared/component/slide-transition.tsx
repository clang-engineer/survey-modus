import React from 'react';

import { TransitionProps } from '@mui/material/transitions';
import { Dialog, Slide } from '@mui/material';

const SlideTransition = React.forwardRef(function Transition(
  props: TransitionProps & {
    children: React.ReactElement<unknown>;
  },
  ref: React.Ref<unknown>
) {
  return <Slide direction="up" ref={ref} {...props} />;
});

export default SlideTransition;
