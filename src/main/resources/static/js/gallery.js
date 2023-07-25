(function ($) {
    'use strict';
    document.addEventListener("DOMContentLoaded", function () {
        const thumbnails = document.querySelectorAll(".thumbnail");
        const mainPhoto = document.querySelector(".main-photo img");
        const deleteButton = document.querySelector("#delete-button");
        const uploadInput = document.querySelector("#upload-input");
        const uploadButton = document.querySelector("#upload-button");

        thumbnails.forEach(function (thumbnail) {
            thumbnail.addEventListener("click", function () {
                const src = this.getAttribute("data-src");
                const photo = this.getAttribute("photo");
                mainPhoto.src = src;
                mainPhoto.setAttribute("photo",photo);
                thumbnails.forEach(function (thumb) {
                    thumb.classList.remove("active");
                });
                this.setAttribute("photo",photo);
                this.classList.add("active");
            });
        });

        deleteButton.addEventListener("click", function () {
            const activeThumbnail = document.querySelector(".thumbnail.active");
            if (activeThumbnail) {
                const photo = mainPhoto.getAttribute('photo');
                const productId = mainPhoto.getAttribute("productId");
                console.log(photo);
                console.log(productId);
                fetch(`/admin/deletePhoto?photo=${photo}&productId=${productId}`, {
                    method: 'GET',
                })
                    .then(response => {
                        // Обрабатываем ответ от сервера
                        if (response.ok) {
                            // Если ответ успешный, делаем что-то, например, обновляем страницу
                            const src = activeThumbnail.getAttribute("data-src");
                            mainPhoto.src = "";
                            activeThumbnail.parentNode.removeChild(activeThumbnail);
                        } else {
                            // Если ответ неуспешный, обрабатываем ошибку
                            alert('Ошибка при удалении фото:' + response.statusText);
                        }
                    })
                    .catch(error => {
                        // Обрабатываем ошибку, если AJAX-запрос не выполнен
                        console.error('Ошибка при выполнении запроса:', error);
                    });
            }
        });


        uploadInput.addEventListener("change", function () {
            const file = this.files[0];
            const reader = new FileReader();

            reader.onload = function (e) {
                const imgData = e.target.result;

                const newThumbnail = document.createElement("img");
                newThumbnail.src = imgData;
                newThumbnail.alt = "New Photo";
                newThumbnail.setAttribute("data-src", imgData);
                newThumbnail.classList.add("thumbnail");
                thumbnails.forEach(function (thumb) {
                    thumb.classList.remove("active");
                });
                newThumbnail.classList.add("active");
                newThumbnail.addEventListener("click", function () {
                    const src = this.getAttribute("data-src");
                    mainPhoto.src = src;

                    thumbnails.forEach(function (thumb) {
                        thumb.classList.remove("active");
                    });
                    this.classList.add("active");
                });

                const thumbnailsContainer = document.querySelector(".thumbnails");
                thumbnailsContainer.appendChild(newThumbnail);
                mainPhoto.src = imgData;

                // Убираем фокус с новой добавленной фотографии
                newThumbnail.blur();
            };

            reader.readAsDataURL(file);
        });
    });
})(jQuery);
