(function ($) {
    "use strict";

    const deleteVariation = document.querySelectorAll('.deleteSpecification');
    const newSpecForm = document.getElementById('addSpec')

    deleteVariation.forEach(function (link) {
        link.addEventListener('click', function (event) {
            event.preventDefault();
            let productId = document.getElementById('productId').value;
            let specKey = link.getAttribute('specKey');

            let formData = new FormData();
            formData.append('specKey', specKey);
            formData.append('productId', productId);
            if (confirm('Вы уверены, что хотите удалить эту спецификацию товара?' + specKey)) {
                console.log(productId)
                $.ajax({
                    url: '/admin/product/deleteSpecification',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
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

    newSpecForm.addEventListener('submit', function (e) {
        let formData = new FormData(this);
        $.ajax({
            url: '/admin/product/addSpecification',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                $('#progressModal').modal('hide');
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
    })
})(jQuery);
