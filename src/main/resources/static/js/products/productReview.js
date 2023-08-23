(function ($) {
    "use strict";
    const ratingStars = document.querySelectorAll(".rating_stars li");
    ratingStars.forEach((star, index) => {
        star.addEventListener("click", (event) => {
            event.preventDefault();
            updateRatingStars(index);
        });
    });

    function updateRatingStars(clickedIndex) {
        const starElements = document.querySelectorAll(".rating_stars li a i");
        starElements.forEach((star, index) => {
            if (index <= clickedIndex) {
                star.classList.remove("icon-star");
                star.classList.add("icon-star2");
            } else {
                star.classList.remove("icon-star2");
                star.classList.add("icon-star");
            }
        });
    }

    const reviewForm = document.getElementById("review_form");
    reviewForm.addEventListener("submit", (event) => {
        event.preventDefault();

        // Получаем данные формы
        const rating = document.querySelectorAll(".rating_stars li a i.icon-star2").length;
        const review = document.getElementById("review_comment").value;
        const product = document.getElementById('productId').value;


        // Создаем объект с данными для отправки на сервер
        const formData = {
            rating: rating,
            review: review,
            productId: product,
        };

        console.log(formData);
        $.ajax({
            url: "/shop/productReview",
            type: "POST",
            dataType: "json",
            data: JSON.stringify(formData),
            contentType: "application/json",
            success: function (response) {
                showModal("Review", response.message);
            },
            error: function (xhr, textStatus, errorThrown) {
                try {
                    const errorData = JSON.parse(xhr.responseText);
                    if (errorData.hasOwnProperty("error")) {
                        showModal('Review',errorData.error);
                    } else {
                        showModal('Review','Что-то пошло не так');
                    }
                } catch (e) {
                    showModal('Review','Что-то пошло не так');
                }
            },
        });
    });
    function showModal(title, message) {
        const modal = document.getElementById('modal');
        const modalMessage = document.getElementById('modalMessage');
        modalMessage.textContent = message;

        // Отображаем модальное окно
        modal.style.display = 'block';

        // Закрытие модального окна при клике на крестик
        document.getElementById('closeModal').addEventListener('click', function() {
            modal.style.display = 'none';
            window.location.reload();
        });

        // Закрытие модального окна при клике вне его области
        window.addEventListener('click', function(event) {
            if (event.target === modal) {
                modal.style.display = 'none';
                window.location.reload();
            }
        });
    }
})(jQuery);
