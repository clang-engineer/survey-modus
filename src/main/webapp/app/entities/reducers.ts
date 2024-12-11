import point from 'app/entities/point/point.reducer';
import userPoint from 'app/entities/user-point/user-point.reducer';
import group from 'app/entities/group/group.reducer';
import groupUser from 'app/entities/group-user/group-user.reducer';
import company from 'app/entities/company/company.reducer';
import groupCompany from 'app/entities/group-company/group-company.reducer';
import category from 'app/entities/category/category.reducer';
import form from 'app/entities/form/form.reducer';
import companyForm from 'app/entities/company-form/company-form.reducer';
import field from 'app/entities/field/field.reducer';

/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  point,
  userPoint,
  group,
  groupUser,
  company,
  groupCompany,
  category,
  form,
  companyForm,
  field,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
