(function ($) {
    "use strict";

    function showModal(title, message) {
        const modal = document.getElementById('modal');
        const modalMessage = document.getElementById('modalMessage');
        modalMessage.textContent = message;

        // Отображаем модальное окно
        modal.style.display = 'block';

        // Закрытие модального окна при клике на крестик
        document.getElementById('closeModal').addEventListener('click', function () {
            modal.style.display = 'none';
            window.location.reload();
        });

        // Закрытие модального окна при клике вне его области
        window.addEventListener('click', function (event) {
            if (event.target === modal) {
                modal.style.display = 'none';
                window.location.reload();
            }
        });
    }
});
