$(document).ready(function () {
    const productForm = document.getElementById('productEdit');

    productForm.addEventListener('submit', function (e){
        e.preventDefault();
        let formData = new FormData(this);
        $.ajax({
            url: '/admin/editProduct',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                // Дополнительные действия при успешной отправке данных
                alert(response.message);
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
    })
})
