import React, { useCallback, useState } from 'react';
import { Box, FormControl, IconButton, styled, Typography } from '@mui/material';
import { IField } from 'app/shared/model/field.model';
import { FormikProps } from 'formik';

import CloudUploadIcon from '@mui/icons-material/CloudUpload';

import { useDropzone } from 'react-dropzone';
import { IconX } from '@tabler/icons';

interface ISurveyModalTextFieldProps {
  field: IField;
  formik: FormikProps<Record<string, any>>;
}

const SurveyModalTextField = (props: ISurveyModalTextFieldProps) => {
  const [files, setFiles] = useState<any[]>([]);

  const onDrop = useCallback((acceptedFiles: File[]) => {
    console.log('Uploaded files:', acceptedFiles);
    setFiles(prevFiles => [...prevFiles, ...acceptedFiles]);
  }, []);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: {
      'image/*': ['.png', '.jpg', '.jpeg', '.gif'],
      // "video/*": [".mp4", ".avi", ".mkv"],
      // "audio/*": [".mp3", ".wav", ".flac"],
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
    maxSize: 10485760,
  });

  return (
    <FormControl fullWidth>
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
        {files.map((file: File, index) => (
          <Box display="flex" alignItems="center" key={index}>
            <IconButton
              color="error"
              size="small"
              onClick={() => {
                setFiles(prevFiles => prevFiles.filter(prevFile => prevFile !== file));
              }}
            >
              <IconX size={'1rem'} />
            </IconButton>
            <Typography variant="body2">
              {file.name} - {file.size} bytes
            </Typography>
          </Box>
        ))}
      </Box>
    </FormControl>
  );
};

export default SurveyModalTextField;
