(function ($) {
    "use strict";
    $(document).on('click', '.receive-button', function() {
        var productId = $(this).data('product-id');

        $('#receiveModal').modal('show');

        // Установить идентификатор продукта в скрытое поле формы
        $('#productId').val(productId);
    });

    // Обработчик события для кнопки "Поступление" в диалоговом окне
    $('#receiveButton').click(function() {
        var productId = $('#productId').val();
        var store = $('#selectStore').val();
        var quantity = $('#inputQuantity').val();

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
            },
            error: function(xhr, status, error) {
                // Обработка ошибки при отправке данных
                alert('Ошибка при регистрации поступления товара');
            }
        });
    });
})(jQuery);
