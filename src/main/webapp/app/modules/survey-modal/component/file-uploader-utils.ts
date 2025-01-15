import axios from 'axios';

const uploadFilesToServer = async (files: File[]) => {
  const formData = new FormData();
  files.forEach(file => formData.append('multipartFiles', file)); // 'files'는 서버가 기대하는 필드 이름

  try {
    return await axios.post('/api/files/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
  } catch (error) {
    console.error('File upload error:', error);
    throw error;
  }
};

export { uploadFilesToServer };
