'use client';
import Link from 'next/link';
import { useAuth } from '@/context/AuthContext';
import { useState } from 'react';
import RegisterModal from '@/components/modals/RegisterModal';
import Image from 'next/image';
import { useRouter } from 'next/navigation';
import AuthModalsContainer from '@/components/modals/AuthModalsContainer';

export default function Avatar() {
  const { user, isAuthenticated, loading } = useAuth();
  const [authModalOpen, setAuthModalOpen] = useState(false);
  const [authModalType, setAuthModalType] = useState('login'); 
  const router = useRouter();

  if (loading) {
    return <div className="w-8 h-8 rounded-full bg-gray-200 animate-pulse" />;
  }

  const handleProfileClick = (e) => {
    if (!isAuthenticated) {
      e.preventDefault();
      setAuthModalType('login');
      setAuthModalOpen(true);
      return;
    }
    router.push(`/profile/${user?.id}`);
  };

  return (
    <>
      {isAuthenticated ? (
      <div 
        className="profile-section cursor-pointer" 
        onClick={handleProfileClick}
      >
            {user?.avatarUrl ? (
              <div className="profile-icon">
                <Image
                  src="/uploads/avatar_b4e8f7dd-7faf-42a6-a706-ad851524d38f.png"
                  alt="Аватар"
                  width={40}
                  height={40}
                  className="object-cover w-full h-full"
                />
              </div>
            ) : (
              <div className="profile-icon-text">
                {user?.username?.[0]?.toUpperCase() || 'U'}
              </div>)}
              </div>
        ) : (
          <div 
        className="profile-section-not-authed cursor-pointer" 
        onClick={handleProfileClick}
          >
          <div className="profile-icon">
            <Image 
              src="/images/Profile-icon.svg" 
              alt="Профиль" 
              width={45}
              height={45}
              className="opacity-70 hover:opacity-100 transition-opacity"
            />
          </div>
          </div>
        )}

      <AuthModalsContainer
        isOpen={authModalOpen}
        onClose={() => setAuthModalOpen(false)}
        defaultActive="login"
      />
    </>
  );
}