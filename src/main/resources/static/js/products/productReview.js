const ratingStars = document.querySelectorAll(".rating_stars li");
ratingStars.forEach((star, index) => {
    star.addEventListener("click", (event) => {
        event.preventDefault();
        updateRatingStars(index);
    });
});

// Функция для изменения классов звездочек при клике
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

// Обработчик отправки формы
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
        success: function (data) {
            console.log(data);
            // Можно добавить обработку успешного ответа от сервера
        },
        error: function (error) {
            console.error(error);
            // Можно добавить обработку ошибок
        },
    });
});
