document.addEventListener('DOMContentLoaded', function() {
    const addToCart = document.querySelectorAll('.add_to_cart');


    addToCart.forEach(function(item) {
        item.addEventListener('click', function(event) {
            event.preventDefault();
            let selectedProductId = item.getAttribute("productId");
            $("#spinner").show();
            $.ajax({
                url: "/cart/add?productId=" + selectedProductId,
                type: "GET",
                success: function (data) {
                    $("#spinner").hide();
                    showModal("adding", data.message)
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
