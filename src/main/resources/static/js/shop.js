document.addEventListener('DOMContentLoaded', function() {
    const paginationLinks = document.querySelectorAll('.pagination-link');
    const categoryItems = document.querySelectorAll('.category-item');
    const sizeItems = document.querySelectorAll('.size-item');
    const filterButton = document.getElementById('filter');
    const selectSort = document.getElementById('short');

    const category = document.getElementById('currentCategory');
    const size = document.getElementById('currentSize');
    let currentSize = size.value;
    let currentCategory = category.value;
    let sort = selectSort.value;
    let minPrice = document.getElementById('minPrice').value;
    let maxPrice = document.getElementById('maxPrice').value;
    let page = 0;
    paginationLinks.forEach(function(link) {
        link.addEventListener('click', function(event) {
            event.preventDefault();
            page = link.getAttribute('page');
            console.log('Компонент с id ' + page + ' нажат!');
            sendRequestToShopController();
        });
    });

    categoryItems.forEach(function(item) {
        item.addEventListener('click', function(event) {
            event.preventDefault();
            currentCategory = item.getAttribute("categoryId");
            // Выполните дополнительные действия, если нужно
            // Например, добавьте класс 'active' к выбранному элементу
            categoryItems.forEach(function(item) {
                item.classList.remove('active');
            });
            item.classList.add('active');
            sendRequestToShopController(); // Отправить запрос на сервер
        });
    });
    sizeItems.forEach(function(item) {
        item.addEventListener('click', function(event) {
            event.preventDefault();
            currentSize = item.getAttribute("size");
            console.log(currentSize);

            sizeItems.forEach(function(item) {
                item.classList.remove('active');
            });
            item.classList.add('active');
            sendRequestToShopController(); // Отправить запрос на сервер
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

        queryParams += 'categoryId=' + encodeURIComponent(currentCategory) + '&';
        queryParams += 'size=' + encodeURIComponent(currentSize) + '&';
        queryParams += 'page=' + encodeURIComponent(page);
        queryParams += '&minPrice=' + encodeURIComponent(minPrice);
        queryParams += '&maxPrice=' + encodeURIComponent(maxPrice);
        queryParams += '&sort=' + encodeURIComponent(sort);
        if(document.getElementById("search-text") !== null)
            queryParams += '&query=' + encodeURIComponent(document.getElementById("search-text").value );
        window.location.href = baseURL + '?' + queryParams;
    }
});
