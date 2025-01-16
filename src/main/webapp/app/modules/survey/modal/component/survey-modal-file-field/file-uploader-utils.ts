import axios from 'axios';
import { IFile } from 'app/shared/model/file.model';

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

const downloadFileFromServer = async (file: IFile) => {
  try {
    const response = await axios.get(`/api/files/download?fileId=${file.id}`, {
      responseType: 'blob', // Blob 형태로 응답 받기
    });

    // 응답 데이터를 Blob 객체로 생성
    const blob = new Blob([response.data], { type: response.headers['content-type'] });

    // Blob URL 생성
    const url = URL.createObjectURL(blob);

    // 동적으로 링크 생성
    const link = document.createElement('a');
    link.href = url;

    // 서버에서 파일 이름 정보를 전달하지 않으면 기본 이름 지정
    const [name, extension] = file.name.split('.'); // 파일 이름에서 확장자 추출
    link.download = `${name}.${extension}`;

    // 다운로드 트리거
    document.body.appendChild(link);
    link.click();

    // 클린업
    link.remove();
    URL.revokeObjectURL(url);
  } catch (error) {
    console.error('Error downloading the file:', error);
  }
};

export { uploadFilesToServer, downloadFileFromServer };
