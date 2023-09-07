(function ($) {
    "use strict";

    // Обработчик события для удаления категории
    window.deleteCategory = function (categoryId) {
        if (confirm('Вы уверены, что хотите удалить категорию?')) {
            // console.log('deleting category')
            // Выполнение AJAX-запроса на удаление категории с использованием jQuery
            $.ajax({
                url: '/admin/categories/delete?categoryId=' + categoryId,
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
                    alert(response.message);
                    window.location.reload();
                },
                error: function (xhr, status, error) {
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
                },
                complete: function () {
                    submitButton.disabled = false;
                    progressModal.style.display = 'none';
                }
            });
        });
    });
})(jQuery);
