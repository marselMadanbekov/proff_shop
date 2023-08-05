(function ($) {
    "use strict";

    // Обработчик события для удаления категории
    window.deleteShipment = function (shipmentId) {
        if (confirm('Вы уверены, что хотите удалить категорию?')) {
            // Выполнение AJAX-запроса на удаление категории с использованием jQuery
            $.ajax({
                url: '/admin/couponT/delete?couponTid=' + shipmentId,
                type: 'GET',
                success: function (response) {
                    // Обработка успешного удаления
                    alert("Доставка успешно удалена");
                    window.location.reload()
                },
                error: function (xhr, status, error) {
                    // Обработка ошибки удаления
                    alert('Ошибка при удалении доставки :' + error);

                }
            });
        }
    };

    // Обработчик события для отправки формы
    $(document).ready(function () {
        $('form').submit(function (event) {
            event.preventDefault();

            let form = this;
            let progressModal = document.getElementById('progressModal');
            let submitButton = form.querySelector('button[type="submit"]');
            let progressSpinner = progressModal.querySelector('.progress-spinner');

            submitButton.disabled = true;
            progressModal.style.display = 'block';

            let formData = new FormData(form);

            $.ajax({
                url: '/admin/create-couponT',
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
