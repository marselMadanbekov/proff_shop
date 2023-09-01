document.addEventListener('DOMContentLoaded', function() {

    const firstItem = document.getElementById('firstPhoto');
    const secondItem = document.getElementById('secondPhoto');
    const thirdItem = document.getElementById('thirdPhoto');
    const salesItem = document.getElementById('salesPhoto');
    const topItem = document.getElementById('topPhoto');

    const deliveryForm = document.getElementById('deliveryForm');
    const paymentForm = document.getElementById('paymentForm');
    const serviceForm = document.getElementById('serviceForm');


    firstItem.addEventListener('submit', function (e){
        e.preventDefault();
        let formData = new FormData(this);

        $.ajax({
            url: '/admin/carousel-first-item', // Замените '/your-server-endpoint' на ваш путь к серверу
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                // Обработка успешного ответа от сервера
                console.log('Успешно:', response);
                // Здесь вы можете выполнить дополнительные действия после успешного запроса
            },
            error: function(error) {
                // Обработка ошибки
                console.error('Ошибка:', error);
            }
        });
    })
    secondItem.addEventListener('submit', function (e){
        e.preventDefault();
        let formData = new FormData(this);

        $.ajax({
            url: '/admin/carousel-second-item', // Замените '/your-server-endpoint' на ваш путь к серверу
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                // Обработка успешного ответа от сервера
                console.log('Успешно:', response);
                // Здесь вы можете выполнить дополнительные действия после успешного запроса
            },
            error: function(error) {
                // Обработка ошибки
                console.error('Ошибка:', error);
            }
        });
    })
    thirdItem.addEventListener('submit', function (e){
        e.preventDefault();
        let formData = new FormData(this);

        $.ajax({
            url: '/admin/carousel-third-item', // Замените '/your-server-endpoint' на ваш путь к серверу
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                // Обработка успешного ответа от сервера
                console.log('Успешно:', response);
                // Здесь вы можете выполнить дополнительные действия после успешного запроса
            },
            error: function(error) {
                // Обработка ошибки
                console.error('Ошибка:', error);
            }
        });
    })
    salesItem.addEventListener('submit', function (e){
        e.preventDefault();
        let formData = new FormData(this);

        $.ajax({
            url: '/admin/sales-item', // Замените '/your-server-endpoint' на ваш путь к серверу
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                // Обработка успешного ответа от сервера
                console.log('Успешно:', response);
                // Здесь вы можете выполнить дополнительные действия после успешного запроса
            },
            error: function(error) {
                // Обработка ошибки
                console.error('Ошибка:', error);
            }
        });
    })
    topItem.addEventListener('submit', function (e){
        e.preventDefault();
        let formData = new FormData(this);

        $.ajax({
            url: '/admin/top-item', // Замените '/your-server-endpoint' на ваш путь к серверу
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                // Обработка успешного ответа от сервера
                console.log('Успешно:', response);
                // Здесь вы можете выполнить дополнительные действия после успешного запроса
            },
            error: function(error) {
                // Обработка ошибки
                console.error('Ошибка:', error);
            }
        });
    })
    deliveryForm.addEventListener('submit', function (e){
        e.preventDefault();
        let formData = new FormData(this);

        $.ajax({
            url: '/admin/set-delivery-text', // Замените '/your-server-endpoint' на ваш путь к серверу
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                // Обработка успешного ответа от сервера
                console.log('Успешно:', response);
                // Здесь вы можете выполнить дополнительные действия после успешного запроса
            },
            error: function(error) {
                // Обработка ошибки
                console.error('Ошибка:', error);
            }
        });
    })
    paymentForm.addEventListener('submit', function (e){
        e.preventDefault();
        let formData = new FormData(this);

        $.ajax({
            url: '/admin/set-payment-text', // Замените '/your-server-endpoint' на ваш путь к серверу
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                // Обработка успешного ответа от сервера
                console.log('Успешно:', response);
                // Здесь вы можете выполнить дополнительные действия после успешного запроса
            },
            error: function(error) {
                // Обработка ошибки
                console.error('Ошибка:', error);
            }
        });
    })
    serviceForm.addEventListener('submit', function (e){
        e.preventDefault();
        let formData = new FormData(this);

        $.ajax({
            url: '/admin/set-service-text', // Замените '/your-server-endpoint' на ваш путь к серверу
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                // Обработка успешного ответа от сервера
                console.log('Успешно:', response);
                // Здесь вы можете выполнить дополнительные действия после успешного запроса
            },
            error: function(error) {
                // Обработка ошибки
                console.error('Ошибка:', error);
            }
        });
    })

});
