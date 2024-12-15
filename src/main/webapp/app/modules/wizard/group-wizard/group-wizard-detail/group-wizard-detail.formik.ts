import { useFormik } from 'formik';
import * as yup from 'yup';
import { translate } from 'react-jhipster';
import { createEntity, updateEntity } from 'app/entities/group/group.reducer';
import { IUser } from 'app/shared/model/user.model';
import { IGroup } from 'app/shared/model/group.model';

const groupWizardDetailFormik = (props: { groupEntity: IGroup; isNew: boolean; user: IUser; dispatch: any }) => {
  const { groupEntity, isNew, user, dispatch } = props;

  return useFormik({
    initialValues: {
      id: 0,
      title: '',
      description: '',
      activated: false,
      companys: [],
      users: [],
    },
    validationSchema: yup.object({
      id: yup.number(),
      title: yup
        .string()
        .min(5, translate('entity.validation.minlength', { min: 5 }))
        .max(100, translate('entity.validation.maxlength', { max: 100 }))
        .required(translate('entity.validation.required')),
      description: yup.string(),
      activated: yup.boolean().required(translate('entity.validation.required')),
      companys: yup.array().of(
        yup.object().shape({
          id: yup.number(),
        })
      ),
      users: yup.array().of(
        yup.object().shape({
          id: yup.number(),
        })
      ),
    }),
    onSubmit: values => {
      const entity = {
        ...groupEntity,
        ...values,
        user,
      };

      if (isNew) {
        dispatch(createEntity(entity));
      } else {
        dispatch(updateEntity(entity));
      }
    },
  });
};

export default groupWizardDetailFormik;
