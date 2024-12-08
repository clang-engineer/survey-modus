import point from 'app/entities/point/point.reducer';
import userPoint from 'app/entities/user-point/user-point.reducer';
import group from 'app/entities/group/group.reducer';
import groupUser from 'app/entities/group-user/group-user.reducer';
import company from 'app/entities/company/company.reducer';
import userCompany from 'app/entities/user-company/user-company.reducer';
import category from 'app/entities/category/category.reducer';
import form from 'app/entities/form/form.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  point,
  userPoint,
  group,
  groupUser,
  company,
  userCompany,
  category,
  form,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
