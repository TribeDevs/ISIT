const API_BASE_URL = "https://isit-esport.ru"

export const createApiClient = (token = null) => {
  return {

    async noAuthGet(endpoint, params = {}) {
      const url = new URL(`${API_BASE_URL}${endpoint}`);
      console.log('Request URL:', url.toString());
      Object.entries(params).forEach(([key, value]) => {
        url.searchParams.append(key, value);
      });
      console.log(url)
      const response = await fetch(url.toString(), {
        headers: {
          'Content-Type': 'application/json',
        },
      });
       console.log('Response data:', response);
      return handleResponse(response);
    },

    async noAuthPost(endpoint, body) {
      console.log(endpoint, JSON.stringify(body));
      const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
      });
      console.log('Response data:', response);
      return handleResponse(response);
    },


    async get(endpoint, params = {}) {
      const url = new URL(`${API_BASE_URL}${endpoint}`);
      console.log('Request URL:', url.toString());
      Object.entries(params).forEach(([key, value]) => {
        url.searchParams.append(key, value);
      });
      console.log(url)
      const response = await fetch(url.toString(), {
        headers: {
          ...(token && { 'Authorization': `Bearer ${token}` }),
          'Content-Type': 'application/json',
        },
      });
       console.log('Response data:', response);
      return handleResponse(response);
    },

    async post(endpoint, body) {
      const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          ...(token && { 'Authorization': `Bearer ${token}` }),
        },
        body: JSON.stringify(body),
      });
      return handleResponse(response);
    },
  };
};

function handleResponse(response) {
  return response.json().then(data => {
    if (!response.ok) {
      const error = new Error(data.message || 'Request failed');
      error.status = response.status;
      throw error;
    }
    return data;
  });
}