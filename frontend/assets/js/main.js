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

// Скрипты, специфичные для страниц
document.addEventListener('DOMContentLoaded', () => {
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