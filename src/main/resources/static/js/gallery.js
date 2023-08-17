(function ($) {
    'use strict';
    document.addEventListener("DOMContentLoaded", function () {
        const thumbnails = document.querySelectorAll(".thumbnail");
        const mainPhoto = document.querySelector(".main-photo img");
        const deleteButton = document.querySelector("#delete-button");

        thumbnails.forEach(function (thumbnail) {
            thumbnail.addEventListener("click", function () {
                const src = this.getAttribute("data-src");
                const photo = this.getAttribute("photo");
                mainPhoto.src = src;
                mainPhoto.setAttribute("photo", photo);
                thumbnails.forEach(function (thumb) {
                    thumb.classList.remove("active");
                });
                this.setAttribute("photo", photo);
                this.classList.add("active");
            });
        });

        deleteButton.addEventListener("click", function () {
            const activeThumbnail = document.querySelector(".thumbnail.active");
            if (activeThumbnail) {
                const photo = mainPhoto.getAttribute('photo');
                const productId = mainPhoto.getAttribute("productId");

                $('#progressModal').modal('show');
                const formData = new FormData();
                formData.append('photo', photo);
                formData.append('productId', productId);

                $.ajax({
                    url: '/admin/deletePhoto',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (response) {
                        $('#progressModal').modal('hide');
                        const src = activeThumbnail.getAttribute("data-src");
                        mainPhoto.src = "";
                        activeThumbnail.parentNode.removeChild(activeThumbnail);
                        alert(response.message);
                        window.location.reload();
                    },
                    error: function (xhr, textStatus, errorThrown) {
                        $('#progressModal').modal('hide');
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
                    }
                });
            }
        });
    });
})(jQuery);
