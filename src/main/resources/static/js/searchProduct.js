document.addEventListener('DOMContentLoaded', function() {
    const searchButton = document.getElementById('search-button');
    const searchButton1 = document.getElementById('search-button1');
    const categorySelect = document.getElementById('search-category');
    const categorySelect1 = document.getElementById('search-category1');
    const searchText = document.getElementById('search-text');
    const searchText1 = document.getElementById('search-text1');

    // searchButton.addEventListener('click',function (e){
    //     e.preventDefault();
    //     let selectedCategory = categorySelect.value;
    //     let searchQuery = searchText.value;
    //     let baseURL = '/shop/search';
    //     let queryParams = '';
    //     console.log('hello ' + searchQuery);
    //
    //     queryParams += 'categoryId=' + encodeURIComponent(selectedCategory) + '&';
    //     queryParams += 'query=' + encodeURIComponent(searchQuery);
    //
    //     window.location.href = baseURL + '?' + queryParams;
    // });
    const searchForm = document.getElementById('search-form');
    const searchForm1 = document.getElementById('search-form1');

    searchForm.addEventListener('submit', function(event) {
        event.preventDefault(); // Предотвращаем стандартное поведение отправки формы
        let selectedCategory = categorySelect.value;
        let searchQuery = searchText.value;
        let baseURL = '/shop';
        let queryParams = '';

        queryParams += 'categoryId=' + encodeURIComponent(selectedCategory) + '&';
        queryParams += 'query=' + encodeURIComponent(searchQuery);

        window.location.href = baseURL + '?' + queryParams;
    });
    searchForm1.addEventListener('submit', function(event) {
        event.preventDefault(); // Предотвращаем стандартное поведение отправки формы
        let selectedCategory = categorySelect1.value;
        let searchQuery = searchText1.value;
        let baseURL = '/shop';
        let queryParams = '';

        queryParams += 'categoryId=' + encodeURIComponent(selectedCategory) + '&';
        queryParams += 'query=' + encodeURIComponent(searchQuery);

        window.location.href = baseURL + '?' + queryParams;
    });
});
