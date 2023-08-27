(function ($) {
    "use strict";

    document.addEventListener('DOMContentLoaded', function () {
        var form = document.getElementById('storeForm');
        var progressModal = document.getElementById('progressModal');
        var submitButton = form.querySelector('button[type="submit"]');

        form.addEventListener('submit', function (event) {
            event.preventDefault();

            submitButton.disabled = true;

            let formData = new FormData(form);

            $.ajax({
                url: '/admin/create-store',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
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
                }
            });
        });
    });

})(jQuery);
