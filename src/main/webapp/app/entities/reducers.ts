import point from 'app/entities/point/point.reducer';
import userPoint from 'app/entities/user-point/user-point.reducer';
import group from 'app/entities/group/group.reducer';
import company from 'app/entities/company/company.reducer';
import category from 'app/entities/category/category.reducer';
import form from 'app/entities/form/form.reducer';
import field from 'app/entities/field/field.reducer';

/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  point,
  userPoint,
  group,
  company,
  category,
  form,
  field,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
