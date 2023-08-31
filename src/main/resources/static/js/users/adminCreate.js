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
                url: '/admin/superAdmin/create-admin',
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
                    $('#progressModal').modal('hide');
                    try {
                        const errorData = JSON.parse(xhr.responseText);
                        if (errorData.hasOwnProperty("error")) {
                            alert(errorData.error);
                        } else {
                            alert('An error occurred while processing the request.');
                        }
                    } catch (e) {
                        alert('An error occurred while processing the request.');
                    }
                }
            });
        });
    });
})(jQuery);
