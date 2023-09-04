$(document).ready(function () {
    const blockButton = document.getElementById('lock');
    const unlockButton = document.getElementById('unlock');
    const paginationLinks = document.querySelectorAll('.pagination-link');

    let page = 0;

    paginationLinks.forEach(function (item) {
        item.addEventListener('click', function (e) {
            e.preventDefault();
            page = item.getAttribute('page');
            window.location.href = "/admin/users?page=" + page + "&sort=" + sort;
        })
    });

    blockButton.addEventListener('click', function (e) {
        e.preventDefault();
        let userId = blockButton.getAttribute('userId');
        let formData = new FormData();
        formData.append('userId', userId);
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
    unlockButton.addEventListener('click', function (e) {
        e.preventDefault();
        let userId = unlockButton.getAttribute('userId');
        let formData = new FormData();
        formData.append('userId', userId);
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
        })
    })
})
