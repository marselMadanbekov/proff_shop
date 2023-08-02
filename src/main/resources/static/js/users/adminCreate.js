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
                    alert("Администратор успешно создан");
                    $('#progressModal').modal('hide');
                },
                error: function(xhr, status, error) {
                    // Обработка ошибки
                    alert("Ошибка при создании Администратора");
                    $('#progressModal').modal('hide');
                }
            });
        });
    });
})(jQuery);
