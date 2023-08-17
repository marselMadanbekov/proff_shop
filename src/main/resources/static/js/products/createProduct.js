(function ($) {
    'use strict';

    $(document).ready(function() {
        // Обработчик события submit формы
        $('#productForm').submit(function(event) {
            event.preventDefault(); // Предотвращаем стандартное поведение отправки формы

            // Получаем данные формы
            var formData = new FormData(this);

            // Открываем модальное окно с индикатором прогресса
            $('#progressModal').modal('show');

            // Выполняем AJAX-запрос
            $.ajax({
                url: '/admin/create-product',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function(response) {
                    // Обработка успешного ответа
                    alert(response.message);
                    $('#progressModal').modal('hide');
                },
                error: function(xhr, status, error) {
                    try {
                        const errorData = JSON.parse(xhr.responseText);
                        if (errorData.hasOwnProperty("error")) {
                            alert(errorData.error);
                        } else {
                            alert('Что-то пошло не так');
                        }
                    } catch (e) {
                        alert('Что-то пошло не так');
                    }
                    $('#progressModal').modal('hide');
                }
            });
        });
    });
})(jQuery);
