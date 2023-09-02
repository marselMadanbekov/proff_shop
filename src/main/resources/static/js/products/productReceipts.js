$(document).ready(function () {
    const prevMonthButton = document.getElementById('recPrevMonthButton');
    const nextMonthButton = document.getElementById('recNextMonthButton');
    const productSize = document.getElementById('productSize');
    let month = 0;
    let productId = productSize.value;


    productSize.addEventListener('change',function (e){
        e.preventDefault();
        productId = productSize.value;
        loadNewSalesData();
        loadNewReceipts();
    })

    prevMonthButton.addEventListener('click', function (e) {
        e.preventDefault();
        month += 1;
        loadNewReceipts();
        loadNewSalesData();
    });

    nextMonthButton.addEventListener('click', function (e) {
        e.preventDefault();
        if (month > 0) {
            month -= 1;
            loadNewReceipts();
            loadNewSalesData();
        } else {
            alert("Следующие месяцы недоступны");
        }
    });

    // Функция для обновления названия месяца
    function updateMonthLabel(monthName) {
        const monthLabel = document.getElementById('currentMonthLabel');
        monthLabel.textContent =  monthName;
    }

    function updateTableReceipts(data) {
        const tableBody = document.getElementById('receiptsTableBody');
        tableBody.innerHTML = ''; // Очистка таблицы

        let totalQuantity = 0;

        data.receipts.forEach(function (receipt, index) {
            const row = document.createElement('tr');

            // Создание ячеек и заполнение данными
            const idCell = document.createElement('td');
            idCell.textContent = index + 1;
            row.appendChild(idCell);

            const productCell = document.createElement('td');
            productCell.textContent = receipt.productName + ' ( ' + receipt.productSize + ' )';
            row.appendChild(productCell);

            const dateCell = document.createElement('td');
            dateCell.textContent = receipt.date;
            row.appendChild(dateCell);

            const storeCell = document.createElement('td');
            storeCell.textContent = receipt.storeTown;
            row.appendChild(storeCell);

            const quantityCell = document.createElement('td');
            quantityCell.textContent = receipt.quantity;
            row.appendChild(quantityCell);

            const actorCell = document.createElement('td');
            actorCell.textContent = receipt.actorInfo;
            row.appendChild(actorCell);

            totalQuantity += receipt.quantity;
            // Добавление строки в таблицу
            tableBody.appendChild(row);
        });

        const totalRow = document.createElement('tr');
        const totalCell = document.createElement('td');
        totalCell.colSpan = 6; // Занимает все столбцы
        totalCell.textContent = 'Общее количество поступлений за месяц: ' + totalQuantity;
        totalRow.appendChild(totalCell);

        // Добавление строки с общим количеством в таблицу
        tableBody.appendChild(totalRow);

        // Обновление названия месяца
        updateMonthLabel(data.monthName);
    }

    function updateSalesData(data) {
        // Получите элементы, которые вы хотите обновить, по их ID
        const totalSalesElement = document.getElementById('totalSales');
        const targetAmountElement = document.getElementById('targetAmount');

        // Проверьте, есть ли данные в объекте data
        if (data != null && Object.keys(data).length > 0) {
            // Извлеките значения из объекта data
            const totalSales = data.totalSales || 0;
            const targetAmount = data.targetAmount || 0;

            // Обновите содержимое элементов
            totalSalesElement.textContent = totalSales + ' сом';
            targetAmountElement.textContent = targetAmount + ' сом';
        } else {
            // Если данных нет или они пусты, установите соответствующее сообщение
            totalSalesElement.textContent = 'Данные о продажах отсутствуют';
            targetAmountElement.textContent = 'Данные о цели отсутствуют';
        }
    }

    function loadNewReceipts() {
        $('#progressModal').modal('show');
        $.ajax({
            url: '/admin/product/receipts/' + productId + '/' + month, // Замените на URL вашего сервера
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                // Обновление таблицы данными из ответа сервера
                updateTableReceipts(data);
                $('#progressModal').modal('hide');
            },
            error: function (error) {
                alert("Произошла ошибка при загрузке данных. Попробуйте перезагрузить страницу");
            },
        });
    }
    function loadNewSalesData(){
        $('#progressModal').modal('show');
        $.ajax({
            url: '/admin/product/sales/' + productId + '/' + month, // Замените на URL вашего сервера
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                updateSalesData(data);
                $('#progressModal').modal('hide');
            },
            error: function (error) {
                alert("Произошла ошибка при загрузке данных. Попробуйте перезагрузить страницу");
            },
        });
    }
    loadNewSalesData();
    loadNewReceipts();
});
