import Image from 'next/image';

const TournamentItem = ({ tournament }) => {
  return (
    <div className="profile-tournament-item">
      <div className="pti-left">
        <Image
          src={tournament.gameIcon}
          alt={`${tournament.gameName} icon`}
          width={50}
          height={50}
          className="profile-tournament-game-icon"
        />
        <div className="pti-left-text">
          <span className="profile-tournament-name">
            {tournament.tournamentName}
          </span>
          <span className="profile-tournament-place">
            Место {tournament.position}
          </span>
        </div>
      </div>
      
      <div className="pti-right">
        <div className="profile-tournament-dates">
          <span className="profile-tournament-start-date">
            {tournament.startDate}
          </span>
          <span className="profile-separator">—</span>
          <span className="profile-tournament-end-date">
            {tournament.endDate}
          </span>
        </div>
      </div>
    </div>
  );
};

export default function TournamentsSection() {
  const tournaments = [
    {
      id: '1',
      gameIcon: '/images/CS2-icon-50x50.svg',
      gameName: 'CS2',
      tournamentName: 'SibFU Spring Cup 2047',
      position: '10/10',
      startDate: '27.04.2025',
      endDate: '01.05.2025'
    },
    {
      id: '2',
      gameIcon: '/images/Dota2-icon-50x50.svg',
      gameName: 'Dota 2',
      tournamentName: 'SibFU Spring Cup 2047',
      position: '10/10',
      startDate: '27.04.2025',
      endDate: '01.05.2025'
    }
  ];

  return (
    <div className="profile-tournaments-block">
      <div className="profile-tournaments-header">
        <h2>Прошедшие турниры</h2>
      </div>
      
      <div className="profile-tournaments-list">
        {tournaments.map((tournament) => (
          <TournamentItem key={tournament.id} tournament={tournament} />
        ))}
      </div>
    </div>
  );
}