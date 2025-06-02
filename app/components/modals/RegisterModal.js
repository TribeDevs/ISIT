'use client';

import { useState, useEffect } from 'react';
import { createAuthAPI } from '@/api/auth';
import { useAuth } from '@/context/AuthContext';
import SendCodeButton from '@/components/modals/SendCodeButton';
import Image from 'next/image';
import TextInput from '@/components/inputs/TextInput';
import PasswordInput from '@/components/inputs/PasswordInput';
import LoginModal from '@/components/modals/LoginModal';

export default function RegisterModal({ isOpen, onClose, onSwitchToLogin }) {
  const { login } = useAuth();
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    confirmPassword: '',
    code: '',
    nickname: ''
  });
  const [error, setError] = useState('');
  const authAPI = createAuthAPI();
  const [activeModal, setActiveModal] = useState(null);
  
  useEffect(() => {
      setActiveModal(isOpen ? "login" : null);
    }, [isOpen, "login"]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const userData = await authAPI.register({
        "email": formData.email,
        "code": formData.code,
        "username": formData.nickname,
        "password": formData.password
      });
      login({
        "login": formData.email,
        "password": formData.password 
      });
      onClose();
      window.location.reload();
    } catch (err) {
      console.log(err);
      setError(err || 'Registration failed');
    }
  };

  const handleLoginClick = () => {
      onClose();
      setActiveModal("login");
  };


  return (
    <div className={`register-modal ${isOpen ? 'active' : ''}`}>
      <div className="modal-overlay" onClick={onClose} />
      
      <div className="modal-content">
        <div className="modal-header">
          <div className="register-logo-section">
            <Image 
              src="/images/Logo.svg" 
              alt="Logo" 
              width={80}
              height={80}
              className="logo"
            />
            <div className="site-name">
              <span className="logo-large">ISIT</span>
              <span className="logo-small">.GAMING</span>
            </div>
          </div>
          <button className="close-button" onClick={onClose}>
            <Image 
              src="/images/Close-button.svg" 
              alt="Close" 
              width={32}
              height={32}
            />
          </button>
        </div>

        <h2 className="modal-title">Регистрация</h2>
        
        <form onSubmit={handleSubmit}>
          <label className="register-modal-label">E-mail</label>
          <div className="register-modal-input">
            <TextInput 
              className="email"
              placeholder="Введите вашу почту..."
              value={formData.email}
              name = "email"
              onChange={handleChange}
            />
            <SendCodeButton email={formData.email}/>
          </div>

          <label className="register-modal-label">Код из письма</label>
          <div className="register-code-input">
            <TextInput 
              className="code"
              placeholder="Код из письма"
              name = "code"
              onChange={handleChange}
              value={formData.code}
            />
          </div>

          {error && (
            <div className="error-container">
              <Image 
                src="/images/Error-icon.svg" 
                alt="Error" 
                width={24}
                height={24}
                className="error-icon"
              />
              <span className="wrong-code-error">{error}</span>
            </div>
          )}

          <label className="register-modal-label-nickname">Никнейм</label>
          <div className="register-nickname-input">
            <TextInput 
              name = "nickname"
              onChange={handleChange}
              className="nickname"
              placeholder="Можно изменить позже"
              value={formData.nickname}
            />
          </div>

          <label className="register-modal-label">Пароль</label>
          <div className="register-modal-input password-input">
            <PasswordInput
              name = "password"
              onChange={handleChange}
              placeholder="Введите пароль..."
              value={formData.password}
            />
          </div>

          <label className="register-modal-label">Повторите пароль</label>
          <div className="register-modal-input password-input">
            <PasswordInput
              name = "confirmPassword"
              onChange={handleChange}
              placeholder="Повторите пароль..."
              value={formData.confirmPassword}
            />
          </div>

          <button type="submit" className="register-modal-submit-btn">
            Зарегистрироваться
          </button>
        </form>

        <div className="modal-divider"></div>

        <div className="register-modal-footer">
          <span className="register-modal-text">Уже есть аккаунт?</span>
          <button 
           onClick={handleLoginClick}
           className="register-modal-login-link" 
            id="loginModal"
          type="button">Войти</button>
        </div>
      </div>
    </div>
  );
}