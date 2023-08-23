(function ($) {
    "use strict";

    const deleteVariation = document.querySelectorAll('.deleteVariation');
    const newVariation = document.getElementById('addVariationForm');

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


    newVariation.addEventListener('submit', function (e){
        if(confirm("Вы уверены что хотите добавить размер?")){
            e.preventDefault();
            let formData = new FormData(this);
            $.ajax({
                url: '/admin/products/addVariation',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    alert(response.message);
                    window.location.reload();
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
    })
})(jQuery);
