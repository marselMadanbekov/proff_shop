(function ($) {
    "use strict";
    $(document).on('click', '.receive-button', function() {
        let productId = $(this).data('product-id');

        $('#receiveModal').modal('show');

        // Установить идентификатор продукта в скрытое поле формы
        $('#productId').val(productId);
    });

    // Обработчик события для кнопки "Поступление" в диалоговом окне
    $('#receiveButton').click(function() {
        let productId = $('#productId').val();
        let store = $('#selectStore').val();
        let quantity = $('#inputQuantity').val();

        // Выполнить AJAX-запрос на отправку данных в базу данных
        $.ajax({
            url: '/admin/receive-product',
            type: 'POST',
            data: {
                productId: productId,
                store: store,
                quantity: quantity
            },
            success: function(response) {
                // Дополнительные действия при успешной отправке данных
                alert('Поступление товара успешно зарегистрировано');
                $('#receiveModal').modal('hide');
                window.location.reload();
            },
            error: function(xhr, status, error) {
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
    });
})(jQuery);
