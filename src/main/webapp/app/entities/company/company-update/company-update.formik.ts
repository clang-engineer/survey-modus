import { useFormik } from 'formik';
import * as yup from 'yup';
import { translate } from 'react-jhipster';

const companyUpdateFormik = (props: { saveEntity: (entity: any) => void }) => {
  return useFormik({
    initialValues: {
      id: undefined,
      title: '',
      description: '',
      activated: false,
      user: { id: 0 },
      forms: [],
      staffs: [],
    },
    validationSchema: yup.object({
      id: yup.string(),
      title: yup
        .string()
        .min(5, translate('entity.validation.minlength', { min: 5 }))
        .max(100, translate('entity.validation.maxlength', { max: 100 }))
        .required(translate('entity.validation.required')),
      description: yup.string(),
      activated: yup.boolean().required(translate('entity.validation.required')),
      user: yup.object({
        id: yup.number().required(translate('entity.validation.required')),
      }),
      forms: yup.array().of(
        yup.object({
          id: yup.number(),
        })
      ),
      staffs: yup.array().of(
        yup.object({
          email: yup.string().email(translate('entity.validation.email')),
          name: yup
            .string()
            .min(5, translate('entity.validation.minlength', { min: 5 }))
            .max(100, translate('entity.validation.maxlength', { max: 100 }))
            .required(translate('entity.validation.required')),
          phone: yup.string().matches(/^[0-9]{10}$/, translate('entity.validation.pattern', { pattern: '^[0-9]{10}$' })),
          activated: yup.boolean(),
        })
      ),
    }),
    onSubmit: values => {
      props.saveEntity(values);
    },
  });
};

export default companyUpdateFormik;
