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