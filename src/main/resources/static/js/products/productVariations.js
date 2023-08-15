(function ($) {
    "use strict";

    const deleteVariation = document.querySelectorAll('.deleteVariation');
    const newVariation = document.getElementById('newVariation');

    deleteVariation.forEach(function(link) {
        link.addEventListener('click', function(event) {
            event.preventDefault();
            let variation = link.getAttribute('productVarID');
            if (confirm('Вы уверены, что хотите удалить этот размер товара?' + variation)) {
                console.log('deleting category');
                $.ajax({
                    url: '/admin/products/deleteVariation',
                    type: 'POST',
                    data: { variationId: variation }, // Передаем variation в запросе
                    success: function (response) {
                        // Обработка успешного удаления
                        alert(response.message);
                        window.location.reload();
                        // Дополнительные действия при успешном удалении категории
                    },
                    error: function (xhr, status, error) {
                        if (xhr.responseJSON && xhr.responseJSON.error) {
                            alert(xhr.responseJSON.error);
                        } else {
                            alert('Произошла ошибка при удалении размера товара');
                        }
                    }
                });
            }
        });
    });


    newVariation.addEventListener('click', function (e){
        let variation = document.getElementById('newSize').value;
        let productId = this.getAttribute('productId');
        if(confirm("Creation of " + variation)){
            $.ajax({
                url: '/admin/products/addVariation',
                type: 'POST',
                data: {
                    size: variation,
                    productId: productId
                }, // Передаем variation в запросе
                success: function (response) {
                    // Обработка успешного удаления
                    alert(response.message);
                    window.location.reload();
                    // Дополнительные действия при успешном удалении категории
                },
                error: function (xhr, status, error) {
                    if (xhr.responseJSON && xhr.responseJSON.error) {
                        alert(xhr.responseJSON.error);
                    } else {
                        alert('Произошла ошибка при создании размера товара');
                    }
                }
            });
        }
    })
})(jQuery);
