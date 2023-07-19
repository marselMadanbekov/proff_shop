(function ($) {
    "use strict";

    // Обработчик события для удаления категории
    window.deleteCategory = function (categoryId) {
        if (confirm('Вы уверены, что хотите удалить категорию?')) {
            // Выполнение AJAX-запроса на удаление категории с использованием jQuery
            $.ajax({
                url: '/admin/categories/delete/' + categoryId,
                type: 'POST',
                success: function (response) {
                    // Обработка успешного удаления
                    alert('Категория успешно удалена');

                    // Дополнительные действия при успешном удалении категории
                },
                error: function (xhr, status, error) {
                    // Обработка ошибки удаления
                    alert('Ошибка при удалении категории');

                }
            });
        }
    };

    // Обработчик события для отправки формы
    $(document).ready(function () {
        $('form').submit(function (event) {
            event.preventDefault();

            var form = this;
            var progressModal = document.getElementById('progressModal');
            var submitButton = form.querySelector('button[type="submit"]');
            var progressSpinner = progressModal.querySelector('.progress-spinner');

            submitButton.disabled = true;
            progressModal.style.display = 'block';

            var formData = new FormData(form);

            $.ajax({
                url: '/admin/create-category',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    console.log('Request successful');
                    alert('Форма успешно отправлена');
                    window.location.reload();
                },
                error: function (xhr, status, error) {
                    console.log('Request failed');
                    alert('Ошибка при отправке формы');
                },
                complete: function () {
                    submitButton.disabled = false;
                    progressModal.style.display = 'none';
                }
            });
        });
    });
})(jQuery);
