(function ($) {
    "use strict";

    const filterButton = document.getElementById('filter');
    const cancelButton = document.getElementById('cancel');
    const deleteButtons = document.querySelectorAll('.deleteProduct');

    let categoryId = document.getElementById('categorySelect').value;
    let size = document.getElementById('sizeSelect').value;
    let searchQuery = document.getElementById('searchQuery').value;
    let minPrice = document.getElementById('minPrice').value;
    let maxPrice = document.getElementById('maxPrice').value;
    let sort = document.getElementById('sortSelect').value;
    let page = document.getElementById('page').value;


    deleteButtons.forEach(function (item) {
        item.addEventListener("click", function (e) {
            e.preventDefault();
            let productId = item.getAttribute('productId');
            let formData = new FormData();
            formData.append('productId', productId);
            if (confirm("Уверены что хотите удалить товар?")) {
                $.ajax({
                    url: '/admin/products/delete',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (response) {
                        // Обработка успешного удаления
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
    })

    cancelButton.addEventListener('click', () => {
        window.location.href = '/admin/products';
    })

    filterButton.addEventListener('click', () => {
        categoryId = document.getElementById('categorySelect').value;
        size = document.getElementById('sizeSelect').value;
        searchQuery = document.getElementById('searchQuery').value;
        minPrice = document.getElementById('minPrice').value;
        maxPrice = document.getElementById('maxPrice').value;
        sort = document.getElementById('sortSelect').value;
        page = document.getElementById('page').value;
        sendRequestToShopController();
    })
    const paginationLinks = document.querySelectorAll('.pagination-link');

    paginationLinks.forEach(function (link) {
        link.addEventListener('click', function (event) {
            event.preventDefault();
            page = link.getAttribute('page');
            console.log('Компонент с id ' + page + ' нажат!');
            sendRequestToShopController();
        });
    });

    function sendRequestToShopController() {
        let baseURL = '/admin/products';
        let queryParams = '';

        queryParams += 'categoryId=' + encodeURIComponent(categoryId) + '&';
        queryParams += 'page=' + encodeURIComponent(page);
        queryParams += '&minPrice=' + encodeURIComponent(minPrice);
        queryParams += '&maxPrice=' + encodeURIComponent(maxPrice);
        queryParams += '&sort=' + encodeURIComponent(sort);
        if (size !== null && size !== '')
            queryParams += '&size=' + encodeURIComponent(size);
        if (searchQuery !== null && searchQuery !== '')
            queryParams += '&query=' + encodeURIComponent(searchQuery);
        window.location.href = baseURL + '?' + queryParams;
    }
})(jQuery);
