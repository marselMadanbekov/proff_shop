function updatePageData() {
    $.ajax({
        url: "/general-info", // Путь к вашему API для получения данных страницы
        type: "GET",
        dataType: "json",
        success: function (response) {
            let cartItemCount = response.cartCount;
            let wishlistItemCount = response.wishlistCount;
            // Обновляем иконку корзины
            let cartIcon = $(".header_account-list.mini_cart_wrapper");
            // Обновляем иконку избранного
            let wishlistIcon = $(".header_account-list.header_wishlist");


            if (cartItemCount > 0) {
                cartIcon.find(".item_count").text(cartItemCount).show();
            }

            if (wishlistItemCount > 0) {
                wishlistIcon.find(".item_count").text(wishlistItemCount).show();
            }
            // Обновляем номер поддержки
            let callSupport = $(".call-support");
            callSupport.find("a").text(response.callSupport);

            // Обновляем время работы
            let openingTime = $(".widgets_container.contact_us p");
            openingTime.text(response.workTime);
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
}

$(".item_count").hide();

// Вызываем функцию обновления при загрузке страницы
$(document).ready(function () {
    updatePageData();
});
