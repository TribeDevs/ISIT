'use client';

import Image from 'next/image';
import FileInput from '@/components/inputs/FileInput';
import TextInput from '@/components/inputs/TextInput';

export default function EditProfileModal() {
  return (
    <div className="modal" id="editProfileModal">
             <div className="modal-overlay">
                
             </div>
      <div className="modal-content">
        <div className="modal-header">
          <h2 className="modal-title">Редактирование профиля</h2>
          <Image 
            src="/images/Close-button.svg" 
            alt="Закрыть" 
            width={24}
            height={24}
            className="modal-close-btn"
          />
        </div>
        <div className="modal-body">
          <div className="profile-container">
            <div className="profile-image-wrapper">
              <FileInput className="profile-input-file" />
              <Image 
                src="/images/Profile-image.svg" 
                alt="Иконка профиля" 
                width={100}
                height={100}
                className="modal-profile-icon"
              />
            </div>

            <div className="name-and-error-container">
              <div className="name-field">
                <TextInput placeholder="anonymus1337" className="name-input"/>
                <Image 
                  src="/images/Edit-icon.svg" 
                  alt="Редактировать" 
                  width={20}
                  height={20}
                  className="edit-icon"
                />
              </div>

              <div className="error-container">
                <Image 
                  src="/images/Error-icon.svg" 
                  alt="Error" 
                  width={20}
                  height={20}
                  className="error-icon"
                />
                <span className="wrong-nickname-profile-error">
                  Ошибка! Имя пользователя недоступно или занято.
                </span>
              </div>
            </div>
          </div>
          <button className="apply-changes-btn">
            <span>Применить изменения</span>
          </button>
        </div>
      </div>
    </div>
  );
}