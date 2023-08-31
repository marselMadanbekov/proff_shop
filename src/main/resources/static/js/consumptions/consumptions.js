(function ($) {
    "use strict";
    const consumptionForm = document.getElementById('consumptionForm');
    const paginationLinks = document.querySelectorAll('.pagination-link');
    const sortSelect = document.getElementById('sortSelect');
    const storeId = document.getElementById('storeId').value;

    paginationLinks.forEach(function (item){
        item.addEventListener('click',function (e){
            e.preventDefault();
            let sort = sortSelect.value;
            let page = item.getAttribute('page');

            window.location.href = '/admin/consumptions?page=' + page + '&sort=' + sort + '&storeId=' + storeId;
        })
    })

    sortSelect.addEventListener('change',function (e){
        e.preventDefault();
        let sort = this.value;
        window.location.href = '/admin/consumptions?sort=' + sort + '&storeId=' + storeId;
    })

    consumptionForm.addEventListener('submit', function (e){
        e.preventDefault();
        let formData = new FormData(this);
        if (confirm('Вы уверены, что создать расход?')) {
            // console.log('deleting category')
            // Выполнение AJAX-запроса на удаление категории с использованием jQuery
            $.ajax({
                url: '/admin/consumptions/create',
                type: 'POST',
                data:formData,
                processData:false,
                contentType:false,
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
