(function ($) {
    "use strict";

    const filterButton = document.getElementById('filter');
    const cancelButton = document.getElementById('cancel');

    let categoryId = document.getElementById('categorySelect').value;
    let size = document.getElementById('sizeSelect').value;
    let searchQuery = document.getElementById('searchQuery').value;
    let minPrice = document.getElementById('minPrice').value;
    let maxPrice = document.getElementById('maxPrice').value;
    let sort = document.getElementById('sortSelect').value;
    let page = document.getElementById('page').value;

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

    paginationLinks.forEach(function(link) {
        link.addEventListener('click', function(event) {
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
        if(size !== null && size !== '')
            queryParams += '&size=' + encodeURIComponent(size);
        if (searchQuery !== null && searchQuery !== '')
            queryParams += '&query=' + encodeURIComponent(searchQuery);
        window.location.href = baseURL + '?' + queryParams;
    }
})(jQuery);
