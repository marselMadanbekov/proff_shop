(function ($) {
    "use strict";

    const paginationLinks = document.querySelectorAll('.pagination-link');
    const filterButton = document.getElementById('filter');
    const cancelButton = document.getElementById('cancel');

    let statusSelect = document.getElementById('statusSelect').value;
    let sortSelect = document.getElementById('sortSelect').value;
    let storeSelect = document.getElementById('storeSelect').value;
    let page = 0;


    cancelButton.addEventListener('click', () => {
        window.location.href = '/admin/orders';
    })

    filterButton.addEventListener('click', () => {
        statusSelect = document.getElementById('statusSelect').value;
        sortSelect = document.getElementById('sortSelect').value;
        storeSelect  = document.getElementById('storeSelect').value;
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
        let baseURL = '/admin/orders';
        let queryParams = '';

        queryParams += 'status=' + encodeURIComponent(statusSelect) + '&';
        queryParams += 'storeId=' + encodeURIComponent(storeSelect) + '&';
        queryParams += 'sort=' + encodeURIComponent(sortSelect) + '&';
        queryParams += 'page=' + encodeURIComponent(page);

        window.location.href = baseURL + '?' + queryParams;
    }
})(jQuery);
