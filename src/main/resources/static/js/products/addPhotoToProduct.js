const saveButton = document.querySelector('#savePhoto');

// Добавляем обработчик события на клик кнопки "Save"
saveButton.addEventListener('click', function(event) {
    // Отменяем стандартное действие формы (отправку)
    event.preventDefault();

    // Получаем ссылку на форму
    const form = document.querySelector('form');

    // Создаем объект FormData для сбора данных из формы, включая файл
    const formData = new FormData(form);

    // Выполняем AJAX-запрос на сервер
    fetch('/admin/addPhoto', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            // Обрабатываем ответ от сервера
            if (response.ok) {
                // Если ответ успешный, делаем что-то, например, обновляем страницу
                window.location.reload();
            } else {
                // Если ответ неуспешный, обрабатываем ошибку
                alert('Ошибка при загрузке фото:' + response.statusText);
            }
        })
        .catch(error => {
            // Обрабатываем ошибку, если AJAX-запрос не выполнен
            console.error('Ошибка при выполнении запроса:', error);
        });
});
