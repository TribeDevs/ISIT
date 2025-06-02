import { useState } from 'react';
import Image from 'next/image';

export default function PasswordInput({ placeholder, name, value, onChange }) {
  const [isVisible, setIsVisible] = useState(false);
  
  const toggleVisibility = () => {
    setIsVisible(!isVisible);
  };

  return (
    <div className="password-container">
      <input
        type={isVisible ? "text" : "password"}
        placeholder={placeholder}
        className="password-input"
        name = {name}
        value = {value}
        onChange={onChange}
      />
      
      <button
        type="button"
        onClick={toggleVisibility}
        className="password-toggle-button"
        aria-label={isVisible ? "Скрыть пароль" : "Показать пароль"}
      >
        <Image
          className={isVisible ? "password-toggle-icon.visible" : "password-toggle-icon"}
          src={isVisible ? "/images/hide-password.png" : "/images/show-password.png"}
          width={20}
          height={20}
          alt=""
        />
      </button>
    </div>
  );
}