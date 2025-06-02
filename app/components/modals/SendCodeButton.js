import { useState, useEffect } from 'react';
import Image from 'next/image';
import { createSendAPI } from '@/api/send';

export default function SendCodeButton({ email }) {
  const [isLoading, setIsLoading] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);
  const [countdown, setCountdown] = useState(0);
  const [isDisabled, setIsDisabled] = useState(false);
  
  const sendApi = createSendAPI();

  const handleSendCode = async () => {
    if (isLoading || isDisabled) return;
    
    setIsLoading(true);
    
    try {
        console.log(email)
      const response = await sendApi.sendCode({"email":email});
      if (response) {
        setIsSuccess(true);
        startCountdown(); 
      } else {
        throw new Error('Request failed');
      }
    } catch (error) {
      console.error('Error:', error);
      setIsSuccess(false);
    } finally {
      setIsLoading(false);
    }
  };


  const startCountdown = () => {
    setIsDisabled(true);
    setCountdown(60);
  };


  useEffect(() => {
    let timer;
    
    if (countdown > 0) {
      timer = setTimeout(() => {
        setCountdown(countdown - 1);
      }, 1000);
    } else if (countdown === 0) {
      setIsDisabled(false);
      setIsSuccess(false); 
    }

    return () => clearTimeout(timer);
  }, [countdown]);


  return (
    <div className="button-div">
      <button
        type="button"
        className={`send-code-btn${isSuccess ? '-sent' : ''} ${isLoading ? 'loading' : ''}`}
        onClick={handleSendCode}
        disabled={isLoading || isDisabled}
      >
        <span className="button-content">
          {isSuccess ? (
            <>
              <span className="countdown-text">{countdown}s</span>
            </>
          ) : (
            <>
              <span className="button-text">
                {isLoading ? 'Отправка...' : isDisabled ? `Повторно через ${countdown}s` : 'Отправить код'}
              </span>
              {!isLoading && !isDisabled && (
                <Image 
                  src="/images/Verified-icon-white.svg" 
                  alt="Verified" 
                  width={20}
                  height={20}
                  className="verified-icon"
                />
              )}
            </>
          )}
        </span>
      </button>
    </div>
  );
}