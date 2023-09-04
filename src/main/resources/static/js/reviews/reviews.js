(function ($) {
    "use strict";
    const deleteLinks = document.querySelectorAll('.deleteReview');
    const sortSelect = document.getElementById('sort');
    const paginationLinks = document.querySelectorAll('.pagination-link');

    let sortValue = sortSelect.value;

    sortSelect.addEventListener('change', function (e){
        e.preventDefault();
        sortValue = sortSelect.value;
        window.location.href = '/admin/reviews?sort=' + sortValue;
    })

    paginationLinks.forEach(function (item){
        item.addEventListener("click",function (e){
            e.preventDefault();
            let page = item.getAttribute("page");

            window.location.href = '/admin/reviews?sort=' + sortValue + '&page=' +page;
        })
    })


    deleteLinks.forEach(function (item){
        item.addEventListener('click', function (e){
            e.preventDefault();
            let reviewId = item.getAttribute('reviewId');
            let formData = new FormData();
            formData.append('reviewId', reviewId);
            if (confirm('Вы уверены, что хотите удалить отзыв?')) {
                $.ajax({
                    url: '/admin/delete-review',
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
