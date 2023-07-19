(function ($) {
    "use strict";

    document.addEventListener('DOMContentLoaded', function() {
        var form = document.getElementById('storeForm');
        var progressModal = document.getElementById('progressModal');
        var submitButton = form.querySelector('button[type="submit"]');

        form.addEventListener('submit', function(event) {
            event.preventDefault();

            submitButton.disabled = true;

            var formData = new FormData(form);

            var xhr = new XMLHttpRequest();
            xhr.open('POST', '/admin/create-store');
            xhr.onreadystatechange = function() {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status === 200) {
                        console.log('Request successful');
                        alert("Магазин успешно создан")
                        // Дополнительные действия при успешном ответе
                    } else {
                        console.log('Request failed');
                        alert("Ошибка при создании магазина")
                        // Дополнительные действия при ошибке
                    }
                    progressModal.style.display = 'none';
                    submitButton.disabled = false;
                }
            };
            xhr.send(formData);
        });
    });

})(jQuery);
