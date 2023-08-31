deleteStock = function (stockId) {
    if (confirm('Вы уверены, что хотите удалить акцию?')) {
        // Выполнение AJAX-запроса на удаление категории с использованием jQuery
        $.ajax({
            url: '/admin/stock/delete?stockId=' + stockId,
            type: 'POST',
            success: function (response) {
                window.location.reload();
            },
            error: function (xhr, status, error) {
                // Обработка ошибки удаления
                alert('Ошибка при удалении акции');

            }
        });
    }
};
