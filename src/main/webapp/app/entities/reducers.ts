import point from 'app/entities/point/point.reducer';
import userPoint from 'app/entities/user-point/user-point.reducer';
import group from 'app/entities/group/group.reducer';
import userGroup from 'app/entities/user-group/user-group.reducer';
import company from 'app/entities/company/company.reducer';
import userCompany from 'app/entities/user-company/user-company.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  point,
  userPoint,
  group,
  userGroup,
  company,
  userCompany,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
