(function ($) {
    "use strict";

    const paginationLinks = document.querySelectorAll('.pagination-link');
    const deleteGroups = document.querySelectorAll('.delete');
    const filterButton = document.getElementById('filter');
    const cancelButton = document.getElementById('cancel');

    let name = document.getElementById('searchQuery').value;
    let sort = document.getElementById('sortSelect').value;
    let page = document.getElementById('page').value;

    cancelButton.addEventListener('click', () => {
        window.location.href = '/admin/productGroups';
    })


    deleteGroups.forEach(function(link) {
        link.addEventListener('click', function(event) {
            event.preventDefault();
            let groupId = link.getAttribute('groupId');
            if (confirm('Вы уверены, что хотите удалить эту карточку?')) {
                $.ajax({
                    url: '/admin/products/deleteGroup',
                    type: 'POST',
                    data: { groupId: groupId },
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

    filterButton.addEventListener('click', () => {
        name = document.getElementById('searchQuery').value;
        sort = document.getElementById('sortSelect').value;
        page = document.getElementById('page').value;
        sendRequestToShopController();
    })

    paginationLinks.forEach(function(link) {
        link.addEventListener('click', function(event) {
            event.preventDefault();
            page = link.getAttribute('page');
            sendRequestToShopController();
        });
    });

    function sendRequestToShopController() {
        let baseURL = '/admin/productGroups';
        let queryParams = '';

        queryParams += 'page=' + encodeURIComponent(page);
        queryParams += '&sort=' + encodeURIComponent(sort);
        if (name !== null && name !== '')
            queryParams += '&name=' + encodeURIComponent(name);
        window.location.href = baseURL + '?' + queryParams;
    }
})(jQuery);
