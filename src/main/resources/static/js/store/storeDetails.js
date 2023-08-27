(function ($) {
    "use strict";
    const dist = document.getElementById('dateForm');
    const paginationLinks = document.querySelectorAll('.pagination-link');
    const sortSelect = document.getElementById('sortSelect');
    const storeId = document.getElementById('storeId').value;

    sortSelect.addEventListener('change',function (e){
        const dateFrom = $('#dateFrom').val();
        const dateTo = $('#dateTo').val();
        const sort = sortSelect.value;

        let baseUrl = '/admin/storeDetails?';
        let queryParams = '';

        queryParams += 'storeId=' + encodeURIComponent(storeId);
        queryParams += '&dateFrom=' +encodeURIComponent(dateFrom);
        queryParams += '&dateTo=' + encodeURIComponent(dateTo);
        queryParams += '&sort=' + encodeURIComponent(sort);

        window.location.href = baseUrl + queryParams;
    })

    paginationLinks.forEach(function (item){
        item.addEventListener('click',function (e){
            let page = item.getAttribute('page');
            const dateFrom = $('#dateFrom').val();
            const dateTo = $('#dateTo').val();
            const sort = sortSelect.value;

            let baseUrl = '/admin/storeDetails?';
            let queryParams = '';

            queryParams += 'storeId=' + encodeURIComponent(storeId);
            queryParams += '&dateFrom=' +encodeURIComponent(dateFrom);
            queryParams += '&dateTo=' + encodeURIComponent(dateTo);
            queryParams += '&sort=' + encodeURIComponent(sort);
            queryParams += '&page=' + encodeURIComponent(page);

            window.location.href = baseUrl + queryParams;
        })
    })
    dist.addEventListener('submit',function (e){
        e.preventDefault();
        const dateFrom = $('#dateFrom').val();
        const dateTo = $('#dateTo').val();
        const storeId = $('#storeId').val();

        window.location.href = `/admin/storeDetails?dateFrom=${dateFrom}&dateTo=${dateTo}&storeId=${storeId}`;
    })
})(jQuery);
