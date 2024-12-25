import { useFormik } from 'formik';
import * as yup from 'yup';
import { translate } from 'react-jhipster';

const groupUpdateFormik = (props: { saveEntity: (entity: any) => void }) => {
  return useFormik({
    initialValues: {
      id: 0,
      title: '',
      description: '',
      activated: false,
      user: { id: 0 },
      users: [],
      companies: [],
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
      users: yup.array().of(
        yup.object({
          id: yup.number(),
        })
      ),
      companies: yup.array().of(
        yup.object({
          id: yup.number(),
        })
      ),
    }),
    onSubmit(values) {
      props.saveEntity(values);
    },
  });
};

export default groupUpdateFormik;
