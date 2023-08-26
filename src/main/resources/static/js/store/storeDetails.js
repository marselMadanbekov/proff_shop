(function ($) {
    "use strict";
    const dist = document.getElementById('dateForm');

    dist.addEventListener('submit',function (e){
        e.preventDefault();
        const dateFrom = $('#dateFrom').val();
        const dateTo = $('#dateTo').val();
        const storeId = $('#storeId').val();

        window.location.href = `/admin/storeDetails?dateFrom=${dateFrom}&dateTo=${dateTo}&storeId=${storeId}`;
    })
})(jQuery);
