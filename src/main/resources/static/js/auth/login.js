document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('login');
    loginForm.addEventListener('submit', function (e){
        e.preventDefault();
        const formData = new FormData(this);
        $("#spinner").show();

        $.ajax({
            url: '/auth/signIn', // Замените на адрес вашего сервера
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            success: function(response) {
                $("#spinner").hide();
                window.location.href = '/account';
            },
            error: function(xhr, status, error) {
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
        });

        // Закрытие модального окна при клике вне его области
        window.addEventListener('click', function (event) {
            if (event.target === modal) {
                modal.style.display = 'none';
            }
        });
    }
});
