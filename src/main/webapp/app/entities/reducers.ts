import point from 'app/entities/point/point.reducer';
import group from 'app/entities/group/group.reducer';
import userPoint from 'app/entities/user-point/user-point.reducer';
import userGroup from 'app/entities/user-group/user-group.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  point,
  group,
  userPoint,
  userGroup,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
