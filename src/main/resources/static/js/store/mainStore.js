(function ($) {
    "use strict";
    const numberForm = document.getElementById("phoneForm");
    const townForm = document.getElementById("townForm");
    const stateForm = document.getElementById("stateForm");
    const instaForm = document.getElementById('instaForm');
    const emailForm = document.getElementById("emailForm");
    const workTimeForm = document.getElementById('workingTimeForm');
    const phoneNumbers = document.querySelectorAll('.deletePhone');

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
