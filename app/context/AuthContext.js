'use client';

import { createContext, useContext, useState, useEffect } from 'react';
import { createAuthAPI } from '@/api/auth';

export const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);
  const [token, setToken] = useState(null);
  console.log('isAuthenticated:', isAuthenticated, 'user:', user); 
  const authAPI = createAuthAPI(token);
  useEffect(() => {
    const storedToken = localStorage.getItem('authToken');
    if (storedToken) {
      setToken(storedToken);
    }
  }, []);

useEffect(() => {
  const checkAuthStatus = async () => {
    setLoading(true);
    try {
      console.log('Проверка авторизации:', token);  
      if (!token) {
        console.log('Нет токена');
        setIsAuthenticated(false);
        return;
      }
      
      const response = await authAPI.checkAuth();
      console.log('Ответ чека:', response);  
      
      if (response?.id) {  
        console.log('1');
        setUser(response);
        setIsAuthenticated(true);
      } else {
        console.log(2)
        setIsAuthenticated(false);
        setUser(null)
      }
    } catch (error) {
      console.error('Auth check failed:', error);  
      setIsAuthenticated(false);
      setUser(null);
    } finally {
      setLoading(false);
    }
  };
  checkAuthStatus();
}, [token]);

const login = async (credentials) => {
  try {
    const response = await authAPI.login(credentials);
    if (!response) {
      throw new Error('Invalid server response');
    }
    
    localStorage.setItem('authToken', response.accessToken);
    setToken(response.accessToken);
    setUser(response);
    setIsAuthenticated(true);
    return { success: true };
  } catch (error) {
    setIsAuthenticated(false);
    return { 
      success: false, 
      error: error.message || 'Ошибка логина' 
    };
  }
};

  const logout = async () => {
    try {
      await authAPI.logout();
      localStorage.removeItem('authToken');
      setToken(null);
      setUser(null);
      setIsAuthenticated(false);
    } catch (error) {
      console.error('Ошибка логаута:', error);
    }
  };

  return (
    <AuthContext.Provider 
      value={{ 
        user, 
        isAuthenticated, 
        loading, 
        login, 
        logout,
        token
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('Не задан контекст');
  }
  return context;
}