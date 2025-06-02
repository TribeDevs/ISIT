import Link from 'next/link';
import { useAuth } from '@/context/AuthContext';
import { useState, useRef, useEffect } from 'react';
import RegisterModal from '@/components/modals/RegisterModal';
import Image from 'next/image';
import { useRouter } from 'next/navigation';

export default function Avatar() {
  const { user, isAuthenticated, loading, logout } = useAuth();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const router = useRouter();
  const menuRef = useRef(null);

  if (loading) {
    return <div className="w-8 h-8 rounded-full bg-gray-200 animate-pulse" />;
  }

  const handleProfileClick = (e) => {
    if (!isAuthenticated) {
      e.preventDefault();
      setIsModalOpen(true);
      return;
    }
    router.push(`/profile/${user?.id}`);
  };

  return (
    <div className='techn-profile-container relative h-10' ref={menuRef}>
       {isAuthenticated ? (
       <div 
        className="profile-section w-10 h-10 cursor-pointer relative inline-block text-center" 
        onClick={handleProfileClick}
        onMouseEnter={() => setIsMenuOpen(true)}
        onMouseLeave={() => setIsMenuOpen(false)}
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
      {/* Выпадающее меню (только для авторизованных) */}
        {isAuthenticated && isMenuOpen && (
        <div 
            className="dropdown-menu"
            onMouseEnter={() => setIsMenuOpen(true)}
            onMouseLeave={() => setIsMenuOpen(false)}
        >
            <Link href={`/profile/${user?.id}`}>
            <div className="dropdown-profile">
                Профиль
            </div>
            </Link>
            <button
            onClick={() => logout()}
            className="dropdown-logout"
            >
            Выйти
            </button>
        </div>
        )}

       {isModalOpen && (
         <RegisterModal
           isOpen={isModalOpen}
           onClose={() => setIsModalOpen(false)}
         />
       )}
     </div>
   );
}