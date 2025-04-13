document.addEventListener('DOMContentLoaded', () => {
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
});

// Функция для открытия модального окна
function openModal() {
    const modal = document.getElementById('createTeamModal');
    modal.classList.add('active');
}

// Функция для закрытия модального окна
function closeModal() {
    const modal = document.getElementById('createTeamModal');
    modal.classList.remove('active');
}

// Функция для открытия модального окна успешного создания команды
function openSuccessModal() {
    closeModal();
    const successModal = document.getElementById("successModal");
    successModal.classList.add("active");
}

// Функция для закрытия модального окна успешного создания команды
function closeSuccessModal() {
    const successModal = document.getElementById("successModal");
    successModal.classList.remove("active");
}

// Функция для копирования пригласительной ссылки в буфер обмена
function copyInviteLink() {
    const inviteLink = document.querySelector(".invite-link").textContent;
    navigator.clipboard.writeText(inviteLink).then(() => {
        alert("Ссылка скопирована в буфер обмена!");
    }).catch(err => {
        console.error("Ошибка при копировании ссылки: ", err);
    });
}

// Функция для переключения видимости выпадающего списка
function toggleDropdown(dropdownId) {
    const dropdownMenu = document.getElementById(dropdownId);
    dropdownMenu.classList.toggle('active');
}

// Функция для выбора игры
function selectGame(value, text) {
    const dropdownSelected = document.querySelector('#gameDropdown').parentElement.querySelector('.dropdown-selected');
    dropdownSelected.textContent = text;
    dropdownSelected.setAttribute('data-value', value);
    document.getElementById('gameDropdown').classList.remove('active');
}

// Функция для выбора количества игроков
function selectPlayers(value) {
    const dropdownSelected = document.querySelector('#playersDropdown').parentElement.querySelector('.dropdown-selected');
    dropdownSelected.textContent = value;
    dropdownSelected.setAttribute('data-value', value);
    document.getElementById('playersDropdown').classList.remove('active');
}