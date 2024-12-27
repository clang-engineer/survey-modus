import { defaultValue, IField } from 'app/shared/model/field.model';
import { isLookupType } from 'app/shared/model/enumerations/type.model';

import { useFormik } from 'formik';
import * as yup from 'yup';

export interface FieldWizardUpdateFormikProps {
  items: IField[];
  setItems: (items: IField[]) => void;
  handleClose: () => void;
}

const fieldWizardUpdateFormik = (props: FieldWizardUpdateFormikProps) => {
  const { items } = props;
  return useFormik<IField>({
    initialValues: defaultValue,
    validationSchema: yup.object({
      title: yup.string().required('Title is required'),
      description: yup.string().required('Description is required'),
      activated: yup.boolean().required('Activated is required'),
      form: yup.object({}),
      attribute: yup.object({
        type: yup.string().required('Type is required'),
        defaultValue: yup.string().required('Default Value is required'),
      }),
      display: yup.object({
        orderNo: yup.number().required('Order No is required'),
      }),
      lookups: yup
        .array()
        .of(yup.string())
        .test('is-required-based-on-type', 'Lookup Values are required', function (value) {
          const { type } = this.parent.attribute || {};
          if (isLookupType(type)) {
            return value && value.length >= 1; // 최소 1개 필요
          }
          return true; // 조건이 맞지 않으면 검증 통과
        })
        .test('is-unique-based-on-type', 'Lookup Values must be unique', function (value) {
          const { type } = this.parent.attribute || {};
          if (isLookupType(type)) {
            return new Set(value).size === value.length; // 중복 값이 없어야 함
          }
          return true; // 조건이 맞지 않으면 검증 통과
        }),
    }),
    onSubmit: values => {
      props.setItems(
        items.map(a => {
          if (a.id === values.id) {
            return values;
          }
          return a;
        })
      );

      props.handleClose();
    },
  });
};

export default fieldWizardUpdateFormik;
