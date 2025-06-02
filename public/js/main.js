document.addEventListener('DOMContentLoaded', () => {
    // Функция для переключения видимости пароля
    const setupPasswordToggle = (modal) => {
        const passwordFields = modal.querySelectorAll('.password-input');
        passwordFields.forEach(field => {
            const input = field.querySelector('input');
            const toggleIcon = field.querySelector('.password-toggle-icon');
            if (input && toggleIcon) {
                toggleIcon.addEventListener('click', () => {
                    const type = input.getAttribute('type') === 'password' ? 'text' : 'password';
                    input.setAttribute('type', type);
                    toggleIcon.classList.toggle('visible');
                });
            }
        });
    };

    // Управление модальным окном входа
    const profileIcon = document.querySelector('.profile-icon');
    const loginModal = document.querySelector('#loginModal');
    if (loginModal) {
        const closeModalBtn = loginModal.querySelector('.modal-close-btn');
        const modalOverlay = loginModal.querySelector('.modal-overlay');

        profileIcon.addEventListener('click', () => {
            loginModal.classList.add('active');
        });

        closeModalBtn.addEventListener('click', () => {
            loginModal.classList.remove('active');
        });

        modalOverlay.addEventListener('click', () => {
            loginModal.classList.remove('active');
        });

        setupPasswordToggle(loginModal);

        const emailInput = loginModal.querySelector('.login-modal-input input');
        const passwordInput = loginModal.querySelector('.password-input input');
        const loginSubmitBtn = loginModal.querySelector('.login-modal-submit-btn');
        const errorContainerData = loginModal.querySelector('.login-modal .error-container .wrong-data-error').parentElement;

        loginSubmitBtn.addEventListener('click', (e) => {
            e.preventDefault();
            const enteredEmail = emailInput.value.trim();
            const enteredPassword = passwordInput.value.trim();

            if (enteredEmail === 'test@example.com' && enteredPassword === 'password123') {
                errorContainerData.classList.remove('visible');
                window.location.href = 'profile.html';
            } else {
                errorContainerData.classList.add('visible');
            }
        });

        const openRegisterModalBtn = loginModal.querySelector('#openRegisterModal');
        openRegisterModalBtn.addEventListener('click', (e) => {
            e.preventDefault();
            loginModal.classList.remove('active');
            const registerModal = document.querySelector('#registerModal');
            registerModal.classList.add('active');
        });
    }

    // Управление модальным окном регистрации
    const registerModal = document.querySelector('#registerModal');
    if (registerModal) {
        const closeRegisterModalBtn = registerModal.querySelector('.modal-close-btn');
        const registerModalOverlay = registerModal.querySelector('.modal-overlay');

        closeRegisterModalBtn.addEventListener('click', () => {
            registerModal.classList.remove('active');
        });

        registerModalOverlay.addEventListener('click', () => {
            registerModal.classList.remove('active');
        });

        setupPasswordToggle(registerModal);

        const openLoginModalBtn = registerModal.querySelector('#loginModal');
        openLoginModalBtn.addEventListener('click', (e) => {
            e.preventDefault();
            registerModal.classList.remove('active');
            loginModal.classList.add('active');
        });

        const sendCodeBtn = registerModal.querySelector('.send-code-btn');
        if (sendCodeBtn) {
            sendCodeBtn.addEventListener('click', () => {
                const buttonText = sendCodeBtn.querySelector('.button-text');
                const verifiedIcon = sendCodeBtn.querySelector('.verified-icon');
                if (buttonText && verifiedIcon) {
                    buttonText.classList.add('hidden');
                    verifiedIcon.classList.add('visible');
                    sendCodeBtn.classList.add('shrunk');
                }
            });
        }

        const codeInput = registerModal.querySelector('.register-code-input input');
        const errorContainerCode = registerModal.querySelector('.register-modal .error-container .wrong-code-error').parentElement;

        codeInput.addEventListener('input', () => {
            const enteredCode = codeInput.value.trim();
            if (enteredCode === 'TESTTT') {
                errorContainerCode.classList.remove('visible');
            } else {
                errorContainerCode.classList.add('visible');
            }
        });

        const nicknameInput = registerModal.querySelector('.register-nickname-input input[placeholder="Можно изменить позже"]');
        const errorContainerNickname = registerModal.querySelector('.register-modal .error-container .wrong-nickname-error').parentElement;

        nicknameInput.addEventListener('input', () => {
            const enteredNickname = nicknameInput.value.trim();
            if (enteredNickname === 'TESTTT') {
                errorContainerNickname.classList.add('visible');
            } else {
                errorContainerNickname.classList.remove('visible');
            }
        });

        const passwordInput = registerModal.querySelector('.password-input input[placeholder="Введите пароль..."]');
        const confirmPasswordInput = registerModal.querySelector('.password-input input[placeholder="Повторите пароль"]');
        const errorContainerPassword = registerModal.querySelector('.register-modal .error-container .wrong-password-input-error').parentElement;

        const checkPasswordsMatch = () => {
            const password = passwordInput.value.trim();
            const confirmPassword = confirmPasswordInput.value.trim();
            if (password === '' || confirmPassword === '') {
                errorContainerPassword.classList.remove('visible');
            } else if (password !== confirmPassword) {
                errorContainerPassword.classList.add('visible');
            } else {
                errorContainerPassword.classList.remove('visible');
            }
        };

        passwordInput.addEventListener('input', checkPasswordsMatch);
        confirmPasswordInput.addEventListener('input', checkPasswordsMatch);

        const registerSubmitBtn = registerModal.querySelector('.register-modal-submit-btn');
        registerSubmitBtn.addEventListener('click', () => {
            const password = passwordInput.value.trim();
            const confirmPassword = confirmPasswordInput.value.trim();

            if (password !== confirmPassword) {
                errorContainerPassword.classList.add('visible');
            } else {
                errorContainerPassword.classList.remove('visible');
                const toast = registerModal.querySelector('.register-modal .toast');
                if (toast) {
                    toast.classList.add('active');
                    setTimeout(() => {
                        toast.classList.remove('active');
                    }, 5000);
                }
            }
        });
    }

    // Управление модальным окном верификации
    const verifyAccountBtn = document.querySelector('.verify-account-btn');
    const verifyAccountModal = document.querySelector('#verifyAccountModal');
    if (verifyAccountBtn && verifyAccountModal) {
        const closeModalBtn = verifyAccountModal.querySelector('.modal-close-btn');
        const modalOverlay = verifyAccountModal.querySelector('.modal-overlay');
        const loginInput = verifyAccountModal.querySelector('.verify-modal-input input[placeholder="Введите ваш логин СФУ..."]');
        const passwordInput = verifyAccountModal.querySelector('.verify-modal-input input[placeholder="Введите ваш пароль СФУ..."]');
        const verifySubmitBtn = verifyAccountModal.querySelector('.verify-modal-submit-btn');
        const errorContainerSfuData = verifyAccountModal.querySelector('.modal .error-container .wrong-sfu-data-error').parentElement;
        const successVerificationModal = document.querySelector('#successVerificationModal');

        setupPasswordToggle(verifyAccountModal);

        console.log('Verify setup:', { verifyAccountBtn, verifyAccountModal, successVerificationModal });

        verifyAccountBtn.addEventListener('click', () => {
            verifyAccountModal.classList.add('active');
            console.log('Verify modal opened');
        });

        closeModalBtn.addEventListener('click', () => {
            verifyAccountModal.classList.remove('active');
            console.log('Verify modal closed');
        });

        modalOverlay.addEventListener('click', () => {
            verifyAccountModal.classList.remove('active');
            console.log('Verify modal closed by overlay');
        });

        verifySubmitBtn.addEventListener('click', (e) => {
            e.preventDefault();
            const enteredLogin = loginInput.value.trim();
            const enteredPassword = passwordInput.value.trim();
            console.log('Submitted:', { enteredLogin, enteredPassword });

            if (enteredLogin === 'test@example.com' && enteredPassword === 'password123') {
                errorContainerSfuData.classList.remove('visible');
                if (successVerificationModal) {
                    console.log('Opening success modal');
                    successVerificationModal.classList.add('active');
                    verifyAccountModal.classList.remove('active');
                } else {
                    console.log('Success modal not found');
                }
            } else {
                errorContainerSfuData.classList.add('visible');
                console.log('Error shown');
            }
        });

        if (successVerificationModal) {
            const closeSuccessVerificationBtn = successVerificationModal.querySelector('.modal-close-btn');
            const successModalOverlay = successVerificationModal.querySelector('.modal-overlay');
            const successVerificationBtn = successVerificationModal.querySelector('.success-verification-btn');

            closeSuccessVerificationBtn.addEventListener('click', () => {
                successVerificationModal.classList.remove('active');
                console.log('Success modal closed');
            });

            successModalOverlay.addEventListener('click', () => {
                successVerificationModal.classList.remove('active');
                console.log('Success modal closed by overlay');
            });

            successVerificationBtn.addEventListener('click', () => {
                window.location.reload();
                console.log('Page reloaded');
            });
        } else {
            console.log('Success verification modal not found');
        }
    } else {
        console.log('Verify button or modal not found');
    }

    // Управление модальными окнами редактирования профиля
    const usernameBtn = document.querySelector('.username-btn');
    const editProfileModal = document.querySelector('#editProfileModal');
    if (usernameBtn && editProfileModal) {
        const closeEditModalBtn = editProfileModal.querySelector('.modal-close-btn');
        const editModalOverlay = editProfileModal.querySelector('.modal-overlay');
        const nameInput = editProfileModal.querySelector('.name-input');
        const errorContainerNickname = editProfileModal.querySelector('.error-container');
        const applyChangesBtn = editProfileModal.querySelector('.apply-changes-btn');
        const confirmChangesModal = document.querySelector('#confirmChangesModal');

        usernameBtn.addEventListener('click', () => {
            editProfileModal.classList.add('active');
        });

        closeEditModalBtn.addEventListener('click', () => {
            editProfileModal.classList.remove('active');
            errorContainerNickname.classList.remove('visible');
        });

        editModalOverlay.addEventListener('click', () => {
            editProfileModal.classList.remove('active');
            errorContainerNickname.classList.remove('visible');
        });

        nameInput.addEventListener('input', () => {
            const enteredName = nameInput.value.trim();
            if (enteredName === 'anonymus1337') {
                errorContainerNickname.classList.add('visible');
            } else {
                errorContainerNickname.classList.remove('visible');
            }
        });

        applyChangesBtn.addEventListener('click', () => {
            const enteredName = nameInput.value.trim();
            if (enteredName !== 'anonymus1337') {
                editProfileModal.classList.remove('active');
                if (confirmChangesModal) {
                    confirmChangesModal.classList.add('active');
                }
            }
        });

        if (confirmChangesModal) {
            const closeConfirmModalBtn = confirmChangesModal.querySelector('.modal-close-btn');
            const confirmModalOverlay = confirmChangesModal.querySelector('.modal-overlay');
            const confirmBtn = confirmChangesModal.querySelector('.confirm-btn');

            closeConfirmModalBtn.addEventListener('click', () => {
                confirmChangesModal.classList.remove('active');
            });

            confirmModalOverlay.addEventListener('click', () => {
                confirmChangesModal.classList.remove('active');
            });

            confirmBtn.addEventListener('click', () => {
                confirmChangesModal.classList.remove('active');
            });
        }
    }

    // Скрипты для страницы "Команды"
    if (document.body.className === 'teams-page') {
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

    // Скрипты для страницы "Турниры"
    if (document.body.className === 'tournaments-page') {
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
        const createTournamentBtn = document.querySelector('.create-tournament-btn');
        const createTournamentModal = document.querySelector('#createTournamentModal');
        if (createTournamentBtn && createTournamentModal) {
            const closeModalBtn = createTournamentModal.querySelector('.modal-close-btn');
            const modalOverlay = createTournamentModal.querySelector('.modal-overlay');
            createTournamentBtn.addEventListener('click', () => {
                createTournamentModal.classList.add('active');
            });
            closeModalBtn.addEventListener('click', () => {
                createTournamentModal.classList.remove('active');
            });
            modalOverlay.addEventListener('click', () => {
                createTournamentModal.classList.remove('active');
            });
            const gameDropdown = createTournamentModal.querySelector('#tournamentGameDropdown');
            const dropdownSelected = createTournamentModal.querySelector('.dropdown-selected');
            const dropdownArrow = createTournamentModal.querySelector('.dropdown-arrow');
            dropdownArrow.addEventListener('click', () => {
                gameDropdown.classList.toggle('active');
            });
        }
    }

    // Переключение вкладок "Прошедшие" и "Будущие" на главной странице
    const pastTab = document.querySelector('.tab-past-tournaments');
    const futureTab = document.querySelector('.tab-future-tournaments');
    if (pastTab && futureTab) {
        pastTab.addEventListener('click', () => {
            pastTab.classList.add('active');
            futureTab.classList.remove('active');
        });
        futureTab.addEventListener('click', () => {
            futureTab.classList.add('active');
            pastTab.classList.remove('active');
        });
    }
});

// Общие функции для всех страниц
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
    successModal.classList.remove('active');
}

function copyInviteLink() {
    const inviteLink = document.querySelector(".invite-link").textContent;
    navigator.clipboard.writeText(inviteLink).then(() => {
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

function selectTournamentGame(value, text) {
    const dropdownSelected = document.querySelector('#tournamentGameDropdown').parentElement.querySelector('.dropdown-selected');
    dropdownSelected.textContent = text;
    dropdownSelected.setAttribute('data-value', value);
    document.getElementById('tournamentGameDropdown').classList.remove('active');
}



