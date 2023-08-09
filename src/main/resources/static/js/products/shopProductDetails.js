(function ($) {
    'use strict';

    $(document).ready(function () {
        $("#addToWishlist").click(function (event) {
            event.preventDefault();

            let productId = document.getElementById("productId").value;
            // Показываем прогресс-спиннер при отправке запроса
            $("#spinner").show();


            $.ajax({
                url: "/wishlist/addToWishlist?productId=" + productId,
                type: "GET",
                success: function (data) {
                    // Скрываем прогресс-спиннер после получения ответа
                    $("#spinner").hide();

                    // Показываем модальное окно с сообщением из JSON-ответа
                    showModal('Success', data.message);
                },
                error: function (xhr, status, error) {
                    // Скрываем прогресс-спиннер при ошибке
                    $("#spinner").hide();

                    // Показываем модальное окно с сообщением об ошибке
                    try {
                        const errorData = JSON.parse(xhr.responseText);
                        if (errorData.hasOwnProperty("error")) {
                            showModal('Error', errorData.error);
                        } else {
                            showModal('Error', 'An error occurred while processing the request.');
                        }
                    } catch (e) {
                        showModal('Error', 'An error occurred while processing the request.');
                    }
                }
            });
        });
    });

// Функция для показа модального окна
    function showModal(title, message) {
        const modal = document.getElementById('modal');
        const modalMessage = document.getElementById('modalMessage');
        modalMessage.textContent = message;

        // Отображаем модальное окно
        modal.style.display = 'block';

        // Закрытие модального окна при клике на крестик
        document.getElementById('closeModal').addEventListener('click', function() {
            modal.style.display = 'none';
        });

        // Закрытие модального окна при клике вне его области
        window.addEventListener('click', function(event) {
            if (event.target === modal) {
                modal.style.display = 'none';
            }
        });
    }
})(jQuery);
