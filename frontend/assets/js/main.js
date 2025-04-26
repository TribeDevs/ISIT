document.addEventListener('DOMContentLoaded', () => {
    // Управление модальным окном входа
    const profileIcon = document.querySelector('.profile-icon');
    const loginModal = document.querySelector('#loginModal');
    if (loginModal) {
        const closeModalBtn = loginModal.querySelector('.modal-close');
        const modalOverlay = loginModal.querySelector('.modal-overlay');

        // Открытие модального окна при клике на иконку профиля
        profileIcon.addEventListener('click', () => {
            loginModal.classList.add('active');
        });

        // Закрытие модального окна при клике на кнопку закрытия
        closeModalBtn.addEventListener('click', () => {
            loginModal.classList.remove('active');
        });

        // Закрытие модального окна при клике на оверлей
        modalOverlay.addEventListener('click', () => {
            loginModal.classList.remove('active');
        });

        // Переключение видимости пароля в модальном окне входа
        const passwordInput = loginModal.querySelector('.password-input input');
        const passwordToggleIcon = loginModal.querySelector('.password-toggle-icon');
        if (passwordInput && passwordToggleIcon) {
            passwordToggleIcon.addEventListener('click', () => {
                const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                passwordInput.setAttribute('type', type);
                passwordToggleIcon.classList.toggle('visible');
            });
        }

        // Открытие модального окна регистрации при клике на "Зарегистрируйтесь"
        const openRegisterModalBtn = loginModal.querySelector('#openRegisterModal');
        openRegisterModalBtn.addEventListener('click', (e) => {
            e.preventDefault();
            loginModal.classList.remove('active'); // Закрываем модальное окно входа
            const registerModal = document.querySelector('#registerModal');
            registerModal.classList.add('active');
        });
    }

    // Управление модальным окном регистрации
    const registerModal = document.querySelector('#registerModal');
    if (registerModal) {
        const closeRegisterModalBtn = registerModal.querySelector('.modal-close');
        const registerModalOverlay = registerModal.querySelector('.modal-overlay');

        // Закрытие модального окна при клике на кнопку закрытия
        closeRegisterModalBtn.addEventListener('click', () => {
            registerModal.classList.remove('active');
        });

        // Закрытие модального окна при клике на оверлей
        registerModalOverlay.addEventListener('click', () => {
            registerModal.classList.remove('active');
        });

        // Переключение видимости пароля в модальном окне регистрации (для обоих полей пароля)
        const passwordInputs = registerModal.querySelectorAll('.password-input input');
        const passwordToggleIcons = registerModal.querySelectorAll('.password-toggle-icon');
        passwordInputs.forEach((input, index) => {
            const toggleIcon = passwordToggleIcons[index];
            if (input && toggleIcon) {
                toggleIcon.addEventListener('click', () => {
                    const type = input.getAttribute('type') === 'password' ? 'text' : 'password';
                    input.setAttribute('type', type);
                    toggleIcon.classList.toggle('visible');
                });
            }
        });
    }

    // Скрипты, специфичные для страниц
    const bodyClass = document.body.className;

    // Скрипты для страницы "Команды"
    if (bodyClass === 'teams-page') {
        const gameIcons = document.querySelectorAll('.game-icon');

        gameIcons.forEach(icon => {
            if (icon.classList.contains('active')) {
                icon.src = icon.getAttribute('data-active');
            }
        });

        gameIcons.forEach(icon => {
            icon.addEventListener('click', () => {
                if (icon.classList.contains('active')) {
                    icon.classList.remove('active');
                    icon.src = icon.src.replace('-active', '');
                } else {
                    gameIcons.forEach(i => {
                        if (i !== icon) {
                            i.classList.remove('active');
                            i.src = i.src.replace('-active', '');
                        }
                    });
                    icon.classList.add('active');
                    icon.src = icon.getAttribute('data-active');
                }
            });
        });
    }
});

// Общие функции для всех страниц доступны в глобальной области видимости
function openModal() {
    const modal = document.getElementById('createTeamModal');
    modal.classList.add('active');
}

function closeModal() {
    const modal = document.getElementById('createTeamModal');
    modal.classList.remove('active');
}

function openSuccessModal() {
    closeModal();
    const successModal = document.getElementById("successModal");
    successModal.classList.add("active");
}

function closeSuccessModal() {
    const successModal = document.getElementById("successModal");
    successModal.classList.remove("active");
}

function copyInviteLink() {
    const inviteLink = document.querySelector(".invite-link").textContent;
    navigator.clipboard.writeText(inviteLink).then(() => {
        alert("Ссылка скопирована в буфер обмена!");
    }).catch(err => {
        console.error("Ошибка при копировании ссылки: ", err);
    });
}

function toggleDropdown(dropdownId) {
    const dropdownMenu = document.getElementById(dropdownId);
    dropdownMenu.classList.toggle('active');
}

function selectGame(value, text) {
    const dropdownSelected = document.querySelector('#gameDropdown').parentElement.querySelector('.dropdown-selected');
    dropdownSelected.textContent = text;
    dropdownSelected.setAttribute('data-value', value);
    document.getElementById('gameDropdown').classList.remove('active');
}

function selectPlayers(value) {
    const dropdownSelected = document.querySelector('#playersDropdown').parentElement.querySelector('.dropdown-selected');
    dropdownSelected.textContent = value;
    dropdownSelected.setAttribute('data-value', value);
    document.getElementById('playersDropdown').classList.remove('active');
}