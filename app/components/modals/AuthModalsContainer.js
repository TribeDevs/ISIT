'use client';
import { useState, useEffect } from 'react';
import RegisterModal from './RegisterModal';
import LoginModal from './LoginModal';

export default function AuthModalsContainer({ isOpen, onClose, defaultActive = 'login' }) {
  const [activeModal, setActiveModal] = useState(null);

  useEffect(() => {
    setActiveModal(isOpen ? defaultActive : null);
  }, [isOpen, defaultActive]);


  const onSwitchToLogin = () => setActiveModal('login');
  const onSwitchToRegister = () => setActiveModal('register');

  if (!activeModal) return null;

  return (
    <>
      <RegisterModal 
        isOpen={activeModal === 'register'}
        onClose={onClose}
        onSwitchToLogin={setActiveModal('login')} 
      />
      
      <LoginModal 
        isOpen={activeModal === 'login'}
        onClose={onClose}
        onSwitchToRegister={setActiveModal('register')} 
      />
    </>
  );
}