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
    // Находим модальное окно по ID
    const modal = document.getElementById('createTeamModal');
    // Добавляем класс active для отображения
    modal.classList.add('active');
}

// Функция для закрытия модального окна
function closeModal() {
    // Находим модальное окно по ID
    const modal = document.getElementById('createTeamModal');
    // Убираем класс active для скрытия
    modal.classList.remove('active');
}