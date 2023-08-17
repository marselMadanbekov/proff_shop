document.addEventListener('DOMContentLoaded', function() {
    const addToWishlist = document.querySelectorAll('.add_to_wishlist');

    addToWishlist.forEach(function(item) {
        item.addEventListener('click', function(event) {
            event.preventDefault();
            let selectedProductId = item.getAttribute("productId");
            $("#spinner").show();
            $.ajax({
                url: "/wishlist/add?productId=" + selectedProductId,
                type: "GET",
                success: function (data) {
                    $("#spinner").hide();
                    showModal("Adding to wishlist", data.message)
                },
                error: function (xhr,status,error) {
                    $("#spinner").hide();
                    if (xhr.responseJSON && xhr.responseJSON.error) {
                        showModal('Adding to wishlist', xhr.responseJSON.error);
                    } else {
                        showModal('Adding to wishlist', 'Произошла ошибка при удалении размера товара');
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
        document.getElementById('closeModal').addEventListener('click', function() {
            modal.style.display = 'none';
        });

        // Закрытие модального окна при клике вне его области
        window.addEventListener('click', function(event) {
            if (event.target === modal) {
                modal.style.display = 'none';
            }
        });
    }
});
