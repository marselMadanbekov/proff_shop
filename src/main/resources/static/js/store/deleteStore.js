(function ($) {
    "use strict";
    const deleteLinks = document.querySelectorAll('.deleteStore');

    deleteLinks.forEach(function (item){
        item.addEventListener('click', function (e){
            e.preventDefault();
            let storeId = item.getAttribute('storeId');
            let formData = new FormData();
            formData.append('storeId', storeId);
            if (confirm('Вы уверены, что хотите удалить магазин?')) {
                $.ajax({
                    url: '/admin/superAdmin/delete-store',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (response) {
                        window.location.reload();
                    },
                    error: function (xhr, status, error) {
                        alert("Ошибка при удалении магазина")
                    }
                });
            }
        })
    })
})(jQuery);
