(function ($) {
    'use strict';
    document.addEventListener("DOMContentLoaded", function () {
        const redirectButton = document.getElementById("redirect-button");
        const confirmRedirectButton = document.getElementById("confirm-redirect");

        redirectButton.addEventListener("click", function () {
            $('#myModal').modal('show');
        });

        confirmRedirectButton.addEventListener("click", function () {
            window.location.href = "новая_страница.html";
        });
    });
})(jQuery);