'use client';

import Image from 'next/image';

export default function VerificationModal() {
  return (
    <div className="modal success-verification-modal" id="successVerificationModal">
      <div className="modal-overlay"></div>
      <div className="modal-content">
        <div className="modal-header">
          <h2 className="success-verification-header">Верификация</h2>
          <Image 
            src="/images/Close-button.svg" 
            alt="Закрыть" 
            width={24}
            height={24}
            className="modal-close-btn"
          />
        </div>
        <button className="success-verification-btn">Успешная верификация</button>
      </div>
    </div>
  );
}