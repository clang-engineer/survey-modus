import { useFormik } from 'formik';
import * as yup from 'yup';
import { translate } from 'react-jhipster';

const formUpdateFormik = (props: { saveEntity: (entity: any) => void }) => {
  return useFormik({
    initialValues: {
      id: 0,
      title: '',
      description: '',
      activated: false,
      user: { id: 0 },
      category: { id: 0 },
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
      category: yup.object({
        id: yup.number().required(translate('entity.validation.required')),
      }),
    }),
    onSubmit(values) {
      props.saveEntity(values);
    },
  });
};

export default formUpdateFormik;
