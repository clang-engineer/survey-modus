import React, { useCallback, useState } from 'react';
import { Alert, Box, FormControl, IconButton, Typography } from '@mui/material';
import { IField } from 'app/shared/model/field.model';
import { FormikProps } from 'formik';

import CloudUploadIcon from '@mui/icons-material/CloudUpload';

import { useDropzone } from 'react-dropzone';
import { IconDownload, IconTrash } from '@tabler/icons';
import {
  downloadFileFromServer,
  uploadFilesToServer,
} from 'app/modules/survey/modal/component/survey-modal-file-field/file-uploader-utils';
import { IFile } from 'app/shared/model/file.model';

interface ISurveyModalTextFieldProps {
  field: IField;
  formik: FormikProps<Record<string, any>>;
}

const SurveyModalTextField = (props: ISurveyModalTextFieldProps) => {
  const { field, formik } = props;
  const [files, setFiles] = useState<IFile[]>([]);
  const [error, setError] = useState<string | null>(null);

  React.useEffect(() => {
    if (formik.values[field.id]) {
      setFiles(formik.values[field.id]);
    }
  }, [formik.values[field.id]]);

  const onDrop = useCallback(
    async (acceptedFiles: File[]) => {
      setError(null); // 이전 에러 초기화

      if (files.length + acceptedFiles.length > 5) {
        setError(`You can only upload up to ${5} files.`);
        return;
      }

      try {
        const response = await uploadFilesToServer(acceptedFiles);
        if (response.status === 201) {
          const newFiles = [...files, ...response.data];
          setFiles(newFiles);
          props.formik.setFieldValue(`${props.field.id}`, newFiles);
        } else {
          setError('File upload failed. Please try again.');
        }
      } catch (e) {
        setError('File upload failed. Please try again.');
      }
    },
    [files]
  );

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: {
      'image/*': ['.png', '.jpg', '.jpeg', '.gif'],
      'video/*': ['.mp4', '.avi', '.mkv'],
      'audio/*': ['.mp3', '.wav', '.flac'],
      'application/pdf': ['.pdf'],
      'application/msword': ['.doc', '.docx'],
      'application/vnd.openxmlformats-officedocument.wordprocessingml.document': ['.docx'],
      'application/vnd.ms-excel': ['.xls', '.xlsx'],
      'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': ['.xlsx'],
      'application/vnd.ms-powerpoint': ['.ppt', '.pptx'],
      'application/vnd.openxmlformats-officedocument.presentationml.presentation': ['.pptx'],
      'text/plain': ['.txt'],
      'application/zip': ['.zip'],
    },
    multiple: true,
    // maxSize: 10485760,
    // maxFiles: 5,
  });

  const onRemoveButtonClick = (file: IFile) => {
    setFiles(prevFiles => prevFiles.filter(prevFile => prevFile !== file));
    formik.setFieldValue(
      `${field.id}`,
      files.filter(prevFile => prevFile !== file)
    );
  };

  const onDownloadButtonClick = (file: IFile) => {
    downloadFileFromServer(file);
  };

  return (
    <FormControl fullWidth>
      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}
      <Box
        {...getRootProps()}
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          p: 2,
          m: 1,
          border: '2px dashed #9e9e9e',
          borderRadius: 2,
          textAlign: 'center',
          backgroundColor: isDragActive ? '#f5f5f5' : '#ffffff',
          cursor: 'pointer',
          transition: 'background-color 0.2s',
        }}
      >
        <input {...getInputProps()} />
        <CloudUploadIcon color="primary" />
        <Typography variant="body1" sx={{ mt: 1 }}>
          {isDragActive ? 'Drop the files here...' : 'Drag & drop some files here, or click to select files'}
        </Typography>
      </Box>
      <Box sx={{ mt: 2 }}>
        {files.length > 0 && <Typography variant="h6">Uploaded Files:</Typography>}
        {files.map((file: IFile, index) => (
          <Box display="flex" alignItems="center" key={index}>
            <Typography variant="body2">
              {file.name} ({file.size} bytes)
            </Typography>
            &nbsp;
            <IconButton
              size="small"
              onClick={() => {
                onDownloadButtonClick(file);
              }}
            >
              <IconDownload size={'12px'} />
            </IconButton>
            <IconButton
              size="small"
              onClick={() => {
                onRemoveButtonClick(file);
              }}
            >
              <IconTrash size={'12px'} />
            </IconButton>
          </Box>
        ))}
        <Box sx={{ mt: 2 }}>
          <Typography variant="body2" color="textSecondary">
            <strong>Accepted file types:</strong> .png, .jpg, .jpeg, .gif, .pdf, .doc, .docx, .xls, .xlsx, .ppt, .pptx, .txt, .zip
          </Typography>
          <Typography variant="body2" color="textSecondary">
            <strong>Max file size:</strong> 10MB
          </Typography>
        </Box>
      </Box>
    </FormControl>
  );
};

export default SurveyModalTextField;
