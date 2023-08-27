document.addEventListener('DOMContentLoaded', function() {
    const paginationLinks = document.querySelectorAll('.pagination-link');
    const categoryItems = document.querySelectorAll('.category-item');
    const sizeItems = document.querySelectorAll('.size-item');
    const tagItems = document.querySelectorAll('.tag-item');
    const brandItems = document.querySelectorAll('.brand-item');
    const filterButton = document.getElementById('filter');
    const selectSort = document.getElementById('short');

    let currentTag = document.getElementById('currentTag').value;
    let currentBrand = document.getElementById('currentBrand').value;
    let currentSize =  document.getElementById('currentSize').value;
    let currentCategory = document.getElementById('currentCategory').value;
    let sort = selectSort.value;
    let minPrice = document.getElementById('minPrice').value;
    let maxPrice = document.getElementById('maxPrice').value;
    let page = 0;
    paginationLinks.forEach(function(link) {
        link.addEventListener('click', function(event) {
            event.preventDefault();
            page = link.getAttribute('page');
            sendRequestToShopController();
        });
    });

    categoryItems.forEach(function(item) {
        item.addEventListener('click', function(event) {
            event.preventDefault();
            let isCurrent = item.classList.contains('current');
            currentCategory = item.getAttribute("categoryId");
            // Выполните дополнительные действия, если нужно
            // Например, добавьте класс 'active' к выбранному элементу
            categoryItems.forEach(function(item) {
                item.classList.remove('current');
            });
            if (!isCurrent) {
                item.classList.add('current');
            }else
                currentCategory = 0;
        });
    });

    brandItems.forEach(function (item){
        item.addEventListener('click', function (e){
            e.preventDefault();
            let isCurrent = item.classList.contains('current');
            currentBrand = item.getAttribute('brand');
            brandItems.forEach(function (item){
                item.classList.remove('current');
            })
            if (!isCurrent) {
                item.classList.add('current');
            }else
                currentBrand = null;
        })
    })

    tagItems.forEach(function (item){
        item.addEventListener('click',function (e){
            e.preventDefault();
            let isCurrent = item.classList.contains('current');

            currentTag = item.getAttribute('tag');
            tagItems.forEach(function (item){
                item.classList.remove('current');
            })
            if (!isCurrent) {
                item.classList.add('current');
            }else
                currentTag = null;

        })
    })

    sizeItems.forEach(function(item) {
        item.addEventListener('click', function(event) {
            event.preventDefault();
            currentSize = item.getAttribute("size");
            let isCurrent = item.classList.contains('current');
            sizeItems.forEach(function(item) {
                item.classList.remove('current');
            });
            if (!isCurrent) {
                item.classList.add('current');
            }else
                currentSize = null;

        });
    });

    filterButton.addEventListener('click', function (){
        minPrice = document.getElementById('minPrice').value;
        maxPrice = document.getElementById('maxPrice').value;
        sendRequestToShopController();
    })

    selectSort.addEventListener('change',function (){
        sort = selectSort.value;
        console.log(sort);
        sendRequestToShopController();
    })

    function sendRequestToShopController() {
        let baseURL = '/shop';
        let queryParams = '';

        if(currentCategory !== null && currentCategory !== '')
            queryParams += 'categoryId=' + encodeURIComponent(currentCategory) + '&';
        if(currentSize !== null && currentSize !== '')
            queryParams += 'size=' + encodeURIComponent(currentSize) + '&';
        if(currentTag !== null && currentTag !== '')
            queryParams += 'tag=' + encodeURIComponent(currentTag) + '&';
        if(currentBrand !== null && currentBrand !== '')
            queryParams += 'brand=' + encodeURIComponent(currentBrand) + '&';
        if(page !== null && page !== '')
            queryParams += 'page=' + encodeURIComponent(page) + '&';
        queryParams += 'minPrice=' + encodeURIComponent(minPrice) + '&';
        queryParams += 'maxPrice=' + encodeURIComponent(maxPrice) + '&';
        queryParams += 'sort=' + encodeURIComponent(sort) + '&';
        if(document.getElementById("search-text").value !== null && document.getElementById("search-text").value !== '')
            queryParams += '&query=' + encodeURIComponent(document.getElementById("search-text").value );
        window.location.href = baseURL + '?' + queryParams;
    }
});
