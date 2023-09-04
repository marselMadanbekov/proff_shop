$(document).ready(function () {
    const blockLinks = document.querySelectorAll('.lock');
    const unlockLinks = document.querySelectorAll('.unlock');
    const paginationLinks = document.querySelectorAll('.pagination-link');
    const sortType = document.getElementById('sortSelect');
    const searchButton = document.getElementById('searchButton');

    let searchQuery = document.getElementById('searchQuery').value;
    let sort = sortType.value;
    let page = 0;

    paginationLinks.forEach(function (item){
        item.addEventListener('click',function (e){
            e.preventDefault();
            page = item.getAttribute('page');
            window.location.href = "/admin/users?page="+ page +"&sort=" + sort;
        })
    });
    sortType.addEventListener('change',function (e){
        e.preventDefault();
        sort = sortType.value;
        window.location.href = "/admin/users?sort=" + sort + "&searchQuery";
    })

    searchButton.addEventListener('click',function (e){
        e.preventDefault();
        searchQuery = document.getElementById('searchQuery').value;
        window.location.href = "/admin/users?searchQuery=" + searchQuery;
    })

    blockLinks.forEach(function (item){
        item.addEventListener('click', function (e){
            e.preventDefault();
            let userId = item.getAttribute('userId');
            let formData = new FormData();
            formData.append('userId',userId);
            $.ajax({
                url: "/admin/users/block",
                type: "POST",
                data: formData,
                processData: false,
                contentType: false,
                success: function (data) {
                    window.location.reload();
                },
                error: function (xhr, status, error) {
                    try {
                        const errorData = JSON.parse(xhr.responseText);
                        if (errorData.hasOwnProperty("error")) {
                            alert(errorData.error);
                        } else {
                            alert("Произошла ошибка при загрузке данных");
                        }
                    } catch (e) {
                        alert("Произошла ошибка при загрузке данных");
                    }
                }
            });
        })
    })
    unlockLinks.forEach(function (item){
        item.addEventListener('click', function (e){
            e.preventDefault();
            let userId = item.getAttribute('userId');
            let formData = new FormData();
            formData.append('userId',userId);
            $.ajax({
                url: "/admin/users/unlock",
                type: "POST",
                data: formData,
                processData: false,
                contentType: false,
                success: function (data) {
                    window.location.reload();
                },
                error: function (xhr, status, error) {
                    // Скрываем прогресс-спиннер при ошибке
                    try {
                        const errorData = JSON.parse(xhr.responseText);
                        if (errorData.hasOwnProperty("error")) {
                            alert(errorData.error);
                        } else {
                            alert("Произошла ошибка при загрузке данных");
                        }
                    } catch (e) {
                        alert("Произошла ошибка при загрузке данных");
                    }
                }
            });
        })
    })
})
