$(document).ready(function () {
    $('#photoForm').submit(function (e) {
        e.preventDefault(); // Отменяем стандартное поведение отправки формы

        var formData = new FormData(this); // Создаем объект FormData из формы

        $('#progressModal').modal('show');
        $.ajax({
            url: '/admin/addPhoto',
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
    });
});
