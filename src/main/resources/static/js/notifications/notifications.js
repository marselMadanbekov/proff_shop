$(document).ready(function() {
    const viewedLinks = document.querySelectorAll('.viewed');

    viewedLinks.forEach(function (item){
        item.addEventListener('click',function (e){
            e.preventDefault();
            let notificationId = this.getAttribute('notificationId');
            let formData = new FormData();
            formData.append('notificationId', notificationId);
            $.ajax({
                url: '/admin/notification-viewed', // Замените на свой путь к серверу
                method: 'POST',
                data: formData,
                processData:false,
                contentType:false,
                success: function(response) {
                    window.location.reload();
                },
                error: function(error) {
                    console.error('Error checking notifications:', error);
                }
            });
        })
    })

    function updateNotificationCount(hasNotifications) {
        const countSpan = $('.count');
        if (hasNotifications) {
            countSpan.show(); // Показать уведомление
        } else {
            countSpan.hide(); // Скрыть уведомление
        }
    }

    // Функция для отправки запроса на сервер
    function checkNotifications() {
        $.ajax({
            url: '/admin/check-notifications', // Замените на свой путь к серверу
            method: 'GET',
            dataType: 'json',
            success: function(response) {
                updateNotificationCount(response.hasNotifications);
            },
            error: function(error) {
                console.error('Error checking notifications:', error);
            }
        });
    }

    // Вызывайте функцию при загрузке страницы и по необходимости
    checkNotifications();
});
