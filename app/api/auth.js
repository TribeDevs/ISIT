import { createApiClient } from '@/api/client';

export const createAuthAPI = (token = null) => {
  const apiClient = createApiClient(token);
  
  return {
    register: (userData) => 
      apiClient.noAuthPost('/api/v1/auth/signup', userData),  
    checkAuth: () => 
      apiClient.get('/api/v1/users/check-auth'), 
    login: (userData) =>
      apiClient.noAuthPost('/api/v1/auth/signin', userData),
    logout: () => 
      apiClient.post('/api/v1/users/logout', {}),
    sfuLogin: () =>
      apiClient
  };
};