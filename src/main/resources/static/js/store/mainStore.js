(function ($) {
    "use strict";
    const numberForm = document.getElementById("phoneForm");
    const townForm = document.getElementById("townForm");
    const stateForm = document.getElementById("stateForm");
    const instaForm = document.getElementById('instaForm');
    const emailForm = document.getElementById("emailForm");
    const workTimeForm = document.getElementById('workingTimeForm');
    const phoneNumbers = document.querySelectorAll('.deletePhone');
    const allowButtons = document.querySelectorAll('.allowTr');
    const paginationLinks = document.querySelectorAll('.pagination-link');
    const sortSelect = document.getElementById('sortSelect');

    let page = 0;
    let sort = sortSelect.value;
    paginationLinks.forEach(function (item) {
        item.addEventListener('click', function (e) {
            e.preventDefault();
            page = item.getAttribute('page');
            widowUpdate();
        })
    })

    sortSelect.addEventListener('change',function (e){
        e.preventDefault();
        sort = this.value;
        widowUpdate();
    })

    function widowUpdate(){
        let baseUrl = '/admin/superAdmin/main-store'
        let queryParams = '?';

        queryParams += '&page=' + encodeURIComponent(page);
        queryParams += '&sort=' + encodeURIComponent(sort);

        window.location.href = baseUrl + queryParams;

    }
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
    phoneNumbers.forEach(function (phone){
        phone.addEventListener('click', function (e){
            let phone = this.getAttribute('phone');
            let formData = new FormData();
            formData.append('phone', phone);
            $.ajax({
                url: '/admin/superAdmin/delete-phone',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function(response) {
                    // Обработка успешного ответа
                    alert(response.message);
                    $('#progressModal').modal('hide');
                    window.location.reload();
                },
                error: function(xhr, status, error) {
                    try {
                        const errorData = JSON.parse(xhr.responseText);
                        if (errorData.hasOwnProperty("error")) {
                            alert(errorData.error);
                        } else {
                            alert('Что-то пошло не так');
                        }
                    } catch (e) {
                        alert('Что-то пошло не так');
                    }
                    $('#progressModal').modal('hide');
                }
            });
        })
    })

    numberForm.addEventListener('submit',function (e){
        e.preventDefault();
        let formData = new FormData(this);
        $.ajax({
            url: '/admin/superAdmin/add-phone',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                // Обработка успешного ответа
                alert(response.message);
                $('#progressModal').modal('hide');
                window.location.reload();
            },
            error: function(xhr, status, error) {
                try {
                    const errorData = JSON.parse(xhr.responseText);
                    if (errorData.hasOwnProperty("error")) {
                        alert(errorData.error);
                    } else {
                        alert('Что-то пошло не так');
                    }
                } catch (e) {
                    alert('Что-то пошло не так');
                }
                $('#progressModal').modal('hide');
            }
        });
    })

    townForm.addEventListener('submit', function (e){
        e.preventDefault();
        let formData = new FormData(this);
        $.ajax({
            url: '/admin/superAdmin/set-town',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                // Обработка успешного ответа
                alert(response.message);
                $('#progressModal').modal('hide');
                window.location.reload();
            },
            error: function(xhr, status, error) {
                try {
                    const errorData = JSON.parse(xhr.responseText);
                    if (errorData.hasOwnProperty("error")) {
                        alert(errorData.error);
                    } else {
                        alert('Что-то пошло не так');
                    }
                } catch (e) {
                    alert('Что-то пошло не так');
                }
                $('#progressModal').modal('hide');
            }
        });
    })
    stateForm.addEventListener('submit', function (e){
        e.preventDefault();
        let formData = new FormData(this);
        $.ajax({
            url: '/admin/superAdmin/set-state',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                // Обработка успешного ответа
                alert(response.message);
                $('#progressModal').modal('hide');
                window.location.reload();
            },
            error: function(xhr, status, error) {
                try {
                    const errorData = JSON.parse(xhr.responseText);
                    if (errorData.hasOwnProperty("error")) {
                        alert(errorData.error);
                    } else {
                        alert('Что-то пошло не так');
                    }
                } catch (e) {
                    alert('Что-то пошло не так');
                }
                $('#progressModal').modal('hide');
            }
        });
    })
    instaForm.addEventListener('submit', function (e){
        e.preventDefault();
        let formData = new FormData(this);
        $.ajax({
            url: '/admin/superAdmin/set-instagram',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                // Обработка успешного ответа
                alert(response.message);
                $('#progressModal').modal('hide');
                window.location.reload();
            },
            error: function(xhr, status, error) {
                try {
                    const errorData = JSON.parse(xhr.responseText);
                    if (errorData.hasOwnProperty("error")) {
                        alert(errorData.error);
                    } else {
                        alert('Что-то пошло не так');
                    }
                } catch (e) {
                    alert('Что-то пошло не так');
                }
                $('#progressModal').modal('hide');
            }
        });
    })
    emailForm.addEventListener('submit', function (e){
        e.preventDefault();
        let formData = new FormData(this);
        $.ajax({
            url: '/admin/superAdmin/set-email',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                // Обработка успешного ответа
                alert(response.message);
                $('#progressModal').modal('hide');
                window.location.reload();
            },
            error: function(xhr, status, error) {
                try {
                    const errorData = JSON.parse(xhr.responseText);
                    if (errorData.hasOwnProperty("error")) {
                        alert(errorData.error);
                    } else {
                        alert('Что-то пошло не так');
                    }
                } catch (e) {
                    alert('Что-то пошло не так');
                }
                $('#progressModal').modal('hide');
            }
        });
    })
    workTimeForm.addEventListener('submit', function (e){
        e.preventDefault();
        let formData = new FormData(this);
        $.ajax({
            url: '/admin/superAdmin/set-workTime',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                // Обработка успешного ответа
                alert(response.message);
                $('#progressModal').modal('hide');
                window.location.reload();
            },
            error: function(xhr, status, error) {
                try {
                    const errorData = JSON.parse(xhr.responseText);
                    if (errorData.hasOwnProperty("error")) {
                        alert(errorData.error);
                    } else {
                        alert('Что-то пошло не так');
                    }
                } catch (e) {
                    alert('Что-то пошло не так');
                }
                $('#progressModal').modal('hide');
            }
        });
    })
})(jQuery);
