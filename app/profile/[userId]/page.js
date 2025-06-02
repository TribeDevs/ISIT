'use client';

import { useAuth } from '@/context/AuthContext';
import ProfileSection from "@/components/profile/ProfileSection";
import GameBoardsSection from "@/components/profile/GameboardsSection";
import StatsSection from "@/components/profile/StatsSection";
import TournamentsSection from "@/components/profile/TournamentsSection";
import EditProfileModal from "@/components/modals/EditProfileModal";
import VerificationModal from "@/components/modals/VerificationModal";
import { useEffect, useState } from 'react';

export default function Profile() {
  const { user, isAuthenticated, loading } = useAuth();
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    if (!loading && !isAuthenticated) {
      setIsModalOpen(true);
    }
  }, [loading, isAuthenticated]);

  if (loading || !user) {
    return <div>Загрузка...</div>;
  }

  return (
    <div>
      <div className="global-profile-container">
        <div className="page-title">
          <h1>Личный кабинет</h1>
        </div>
        
        <div className="together-columns">
          <ProfileSection user={user} />
          <GameBoardsSection/>
          <TournamentsSection/>
          <StatsSection/>
        </div>
      </div>
      <VerificationModal />
      <EditProfileModal/>
    </div>
  );
}