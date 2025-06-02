import Image from "next/image"

export default function ProfileSection(user) {
    return (
            <div className="profile-block">
                <div className="profile-content">
                    <Image 
                        src={`/uploads/${user?.userData.avatarUrl}`}
                        alt="Фото профиля" 
                        width={120} 
                        height={120}
                        className="profile-image"
                    />
                 <div className="profile-details">
                        <div className="student-info-block">
                            <p className="student-group">
                                <span className="label">Группа:</span><br/>
                                {user?.userData.sfuGroup ? (
                                <span className="group-name">{`${user?.userData.sfuGroup}`}</span>
                                ) : (
                                <span className="group-name">Не указана</span>
                                )}
                            </p>
                            <p className="student-info">
                                <span className="label">Студент:</span><br/>
                                {user?.userData.sfuName ? (
                                <span className="group-name">{`${user?.userData.sfuName}`}</span>
                                ) : (
                                <span className="group-name">Не указан</span>
                                )}
                            </p>
                        </div>
                    </div>
                </div>
                <div className="profile-actions-block">
                    {user?.userData?.verified ? (
                        <button className="change-account-btn">Сменить аккаунт</button>
                    ) : (
                        <button className="verify-account-btn">Верифицировать аккаунт</button>

                    )}
                    <button className="username-btn">
                        <span className="username-text">{`${user?.userData.username}`}</span>
                        <img src="../images/Edit-icon.svg" alt="Редактировать" className="edit-icon"/>
                    </button>
                    <button className="email-btn">
                        <span className="email-text">{`${user?.userData.email}`}</span>
                    </button>
                </div>
            </div>
    )
}