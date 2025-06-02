import Image from "next/image";


export default function GameBoardsSection() {
  const gameAccounts = [
    {
      id: "steam",
      name: "Steam",
      icon: "/images/Steam-account-icon.svg",
      authUrl: "https://steamcommunity.com/login/home/?goto=",
    },
    {
      id: "epic",
      name: "Epic Games",
      icon: "/images/Epic_Games-account-icon.svg",
      authUrl: "https://www.epicgames.com/id/login?lang=ru",
    },
    {
      id: "faceit",
      name: "Faceit",
      icon: "/images/Faceit-account-icon.svg",
      authUrl: "https://accounts.faceit.com/login",
    },
    {
      id: "riot",
      name: "Riot Games",
      icon: "/images/Riot_Games-account-icon.svg",
      authUrl: "https://authenticate.riotgames.com/",
    },
  ];

  return (
    <div className="accounts-block">
      <div className="accounts-header">
        <h2>Аккаунты</h2>
      </div>
      <div className="accounts-list">
        {gameAccounts.map((account) => (
          <div key={account.id} className="account-item">
            <Image
              src={account.icon}
              alt={account.name}
              width={32}
              height={32}
              className="account-icon"
            />
            <button className="bind-account-btn">
              <a 
                href={account.authUrl}
                target="_blank" 
                rel="noopener noreferrer"
                aria-label={`Привязать`}
              >
                Привязать
              </a>
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}