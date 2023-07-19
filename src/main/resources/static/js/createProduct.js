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
                    alert("Продукт успешно создан");
                    $('#progressModal').modal('hide');
                },
                error: function(xhr, status, error) {
                    // Обработка ошибки
                    alert("Ошибка при создании продукта");
                    $('#progressModal').modal('hide');
                }
            });
        });
    });
})(jQuery);
