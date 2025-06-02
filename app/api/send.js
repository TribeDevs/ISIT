import { createApiClient } from '@/api/client';

export const createSendAPI = () => {
  const apiClient = createApiClient();
  
  return {
    sendCode: (userData) => 
      apiClient.noAuthPost('/api/v1/send-code', userData),  
  };
};