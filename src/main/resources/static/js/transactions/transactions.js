(function ($) {
    "use strict";
    const transactionForm = document.getElementById('transactionForm');
    const paginationLinks = document.querySelectorAll('.pagination-link');
    const recPaginationLinks = document.querySelectorAll('.rec-pagination-link');
    const sortSelect = document.getElementById('sortSelect');
    const allowButtons = document.querySelectorAll('.allowTr');

    const recSortSelect = document.getElementById('recSortSelect');
    const storeId = document.getElementById('storeId').value;
    const baseUrl = '/admin/transactions'
    let sort = sortSelect.value;
    let page = 0;
    let recSort = recSortSelect.value;
    let recPage = 0;

    paginationLinks.forEach(function (item) {
        item.addEventListener('click', function (e) {
            e.preventDefault();
            page = item.getAttribute('page');
            widowUpdate();
        })
    })
    recPaginationLinks.forEach(function (item) {
        item.addEventListener('click', function (e) {
            e.preventDefault();
            recPage = item.getAttribute('page');
            widowUpdate();
        })
    })
    allowButtons.forEach(function (item) {
        item.addEventListener('click', function (e) {
            e.preventDefault();
            let transactionId = item.getAttribute('transactionId');
            let formData = new FormData();
            formData.append('transactionId', transactionId);
            if (confirm('Вы уверены, что эти деньги были получены?')) {
                $.ajax({
                    url: '/admin/transactions/allow',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (response) {
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


    recSortSelect.addEventListener('change', function (e) {
        e.preventDefault();
        recSort = recSortSelect.value;
        widowUpdate();
    })

    sortSelect.addEventListener('change', function (e) {
        e.preventDefault();
        sort = this.value;
        widowUpdate();
    })

    function widowUpdate(){
        let queryParams = '?';

        queryParams += 'storeId=' + storeId;
        queryParams += '&sendPage=' + encodeURIComponent(page);
        queryParams += '&sendSort=' + encodeURIComponent(sort);
        queryParams += '&recSort=' + encodeURIComponent(recSort);
        queryParams += '&recPage=' + encodeURIComponent(recPage);

        window.location.href = baseUrl + queryParams;

    }

    transactionForm.addEventListener('submit', function (e) {
        e.preventDefault();
        let formData = new FormData(this);
        if (confirm('Вы уверены, что хотите перевести сумму')) {
            $.ajax({
                url: '/admin/transactions/create',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
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
})(jQuery);
