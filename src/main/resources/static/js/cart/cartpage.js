document.addEventListener('DOMContentLoaded', function () {
    const updateCart = document.getElementById('update_cart');
    const deleteFromCart = document.querySelectorAll('.cart_remove')

    deleteFromCart.forEach(function(item) {
        item.addEventListener('click', function(event) {
            event.preventDefault();
            let selectedProductId = item.getAttribute("productId");
            let selectedProductSize = item.getAttribute("size");
            $("#spinner").show();
            $.ajax({
                url: "/cart/remove?productId=" + selectedProductId+'&size=' + selectedProductSize,
                type: "GET",
                success: function (data) {
                    // Скрываем прогресс-спиннер после получения ответа
                    $("#spinner").hide();

                    // Показываем модальное окно с сообщением об успешном сбросе пароля
                    showModal('Удаление продукта', data.message);
                },
                error: function (xhr,status,error) {
                    $("#spinner").hide();

                    try {
                        const errorData = JSON.parse(xhr.responseText);
                        if (errorData.hasOwnProperty("error")) {
                            showModal('Error', errorData.error);
                        } else {
                            showModal('Error', 'An error occurred while processing the request.');
                        }
                    } catch (e) {
                        showModal('Error', 'An error occurred while processing the request.');
                    }
                }
            });

        });
    });
    updateCart.addEventListener('click', function (event) {
        event.preventDefault();
        let cartItems = [];

        $('.product_q').each(function () {
            let productId = $(this).attr('productId');
            let newQuantity = $(this).val();
            cartItems.push({productId: productId, newQuantity: newQuantity});
        });
        $("#spinner").show();

        $.ajax({
            url: '/cart/update-cart', // Путь к вашему обработчику на сервере
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(cartItems),
            success: function (response) {
                $("#spinner").hide();
                showModal('Добавление продукта', response.message);
            },
            error: function (xhr, status, error) {
                $("#spinner").hide();
                try {
                    const errorData = JSON.parse(xhr.responseText);
                    if (errorData.hasOwnProperty("error")) {
                        showModal('Error', errorData.error);
                    } else {
                        showModal('Error', 'An error occurred while processing the request.');
                    }
                } catch (e) {
                    showModal('Error', 'An error occurred while processing the request.');
                }
            }
        });
    })
    function showModal(title, message) {
        const modal = document.getElementById('modal');
        const modalMessage = document.getElementById('modalMessage');
        modalMessage.textContent = message;

        // Отображаем модальное окно
        modal.style.display = 'block';

        // Закрытие модального окна при клике на крестик
        document.getElementById('closeModal').addEventListener('click', function () {
            modal.style.display = 'none';
            window.location.reload();
        });

        // Закрытие модального окна при клике вне его области
        window.addEventListener('click', function (event) {
            if (event.target === modal) {
                modal.style.display = 'none';
                window.location.reload();
            }
        });
    }
});
