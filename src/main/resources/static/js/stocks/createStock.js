const selectedProducts = {};
const selectedCategory = {};

function updateProductsTable(data) {
    const tableBody = $('#productTable tbody'); // Выбираем tbody таблицы
    tableBody.empty(); // Очищаем содержимое tbody

    // Генерируем HTML для новых строк таблицы на основе полученных данных
    data.forEach(function (product) {
        const isChecked = selectedProducts[product.id] ? 'checked' : ''; // Проверяем, выбран ли продукт
        const photoUrl = product.photos.length > 0 ? `/static/uploads/${product.photos[0]}` : ''; // Получаем URL первого фото, если оно существует
        const row = `
    <tr>
      <td>
        ${photoUrl ? `<img src="${photoUrl}">` : ''}
      </td>
      <td>${product.name}</td>
      <td>${product.price}</td>
      <td>
        <input type="checkbox" data-product-id="${product.id}" onclick="handleProductSelection(this)" ${isChecked}>
      </td>
    </tr>
  `;
        tableBody.append(row); // Добавляем сгенерированную строку в tbody
    });
}

function updateCategoriesTable(data) {
    const tableBody = $('#categoryTable tbody'); // Выбираем tbody таблицы
    tableBody.empty(); // Очищаем содержимое tbody

    // Генерируем HTML для новых строк таблицы на основе полученных данных
    data.forEach(function (category) {
        const isChecked = selectedCategory[category.id] ? 'checked' : ''; // Проверяем, выбран ли продукт
        const row = `
    <tr>
      <td>${category.name}</td>
      <td>
        <input type="checkbox" data-category-id="${category.id}" onclick="handleCategorySelection(this)" ${isChecked}>
      </td>
    </tr>
  `;
        tableBody.append(row); // Добавляем сгенерированную строку в tbody
    });
}

function updateProductsPagination(data) {
    const pagination = $('#productPagination'); // Выбираем элемент пагинации

    // Создаем HTML-код для новой пагинации
    let paginationHtml = '';

    // Генерируем ссылки для каждой страницы
    for (let i = 0; i < data.totalPages; i++) {
        const pageLinkClass = data.number === i ? 'active' : ''; // Определяем класс активной страницы
        paginationHtml += `<a href="#" class="page-link ${pageLinkClass}" data-page="${i}">${i + 1}</a>`;
    }

    // Добавляем ссылки в пагинацию
    pagination.html(paginationHtml);
}
function updateCategoryPagination(data) {
    const pagination = $('#categoryPagination'); // Выбираем элемент пагинации

    // Создаем HTML-код для новой пагинации
    let paginationHtml = '';

    // Генерируем ссылки для каждой страницы
    for (let i = 0; i < data.totalPages; i++) {
        const pageLinkClass = data.number === i ? 'active' : ''; // Определяем класс активной страницы
        paginationHtml += `<a href="#" class="page-link ${pageLinkClass}" data-page="${i}">${i + 1}</a>`;
    }

    // Добавляем ссылки в пагинацию
    pagination.html(paginationHtml);
}

function loadNewProducts(pageNumber) {
    $.ajax({
        url: `/admin/productsByPage?page=${pageNumber}`,
        method: "GET",
        dataType: "json",
        success: function (data) {
            updateProductsTable(data.content);
            updateProductsPagination(data);
        },
        error: function (xhr, status, error) {
            console.error("Ошибка загрузки данных:", error);
        }
    });
}

function loadNewCategories(pageNumber) {
    $.ajax({
        url: `/admin/categoriesByPage?page=${pageNumber}`,
        method: "GET",
        dataType: "json",
        success: function (data) {
            console.log(data);
            updateCategoriesTable(data.content);
            updateCategoryPagination(data);
        },
        error: function (xhr, status, error) {
            console.error("Ошибка загрузки данных:", error);
        }
    });
}

function handleProductSelection(checkbox) {
    const productId = $(checkbox).attr('data-product-id');
    if (checkbox.checked) {
        selectedProducts[productId] = true; // Добавляем продукт в выбранные
    } else {
        delete selectedProducts[productId]; // Удаляем продукт из выбранных
    }
}

function handleCategorySelection(checkbox) {
    const categoryId = $(checkbox).attr('data-category-id');
    if (checkbox.checked) {
        selectedCategory[categoryId] = true; // Добавляем продукт в выбранные
    } else {
        delete selectedCategory[categoryId]; // Удаляем продукт из выбранных
    }
    console.log(selectedCategory);
}

$(document).on("click", "#productPagination a", function (e) {
    e.preventDefault();

    const pageNumber = $(this).attr("data-page");
    loadNewProducts(pageNumber);
});
$(document).on("click", "#categoryPagination a", function (e) {
    e.preventDefault();

    const pageNumber = $(this).attr("data-page");
    loadNewCategories(pageNumber);
});

loadNewProducts(0);
loadNewCategories(0);


function createPromotion() {
    // Получение данных акции
    const startDate = $('#startDate').val(); // Значение начальной даты из элемента с id "start-date"
    const endDate = $('#endDate').val(); // Значение конечной даты из элемента с id "end-date"
    const discount = $('#discount').val(); // Значение суммы скидки из элемента с id "discount-amount"
    const type = $('#selectType').val()
    // Создание объекта данных для отправки на сервер
    const data = {
        startDate: startDate,
        endDate: endDate,
        discount: discount,
        type: type,
        participants: type === 1 ? selectedCategory : selectedProducts,
        participants: type === '1' ? selectedCategory : selectedProducts,
    };

    console.log(data);
    // Отправка запроса на сервер
    $.ajax({
        url: '/admin/create-stock', // URL вашего серверного маршрута для создания акции
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (response) {
            // Успешное создание акции
            console.log('Акция успешно создана:', response);
        },
        error: function (xhr, status, error) {
            // Ошибка при создании акции
            console.error('Ошибка при создании акции:', error);
        }
    });
}

function searchProducts() {
    // Получение значения поискового запроса
    const searchQuery = $('#navbar-search-input').val();

    // Создание объекта данных для отправки на сервер

    // Отправка запроса на сервер
    $.ajax({
        url: '/admin/products/search', // URL вашего серверного маршрута для выполнения поиска продуктов
        method: 'POST',
        contentType: 'application/json',
        data: searchQuery,
        success: function (response) {
            // Обновление таблицы с продуктами
            updateProductsTable(response);
        },
        error: function (xhr, status, error) {
            console.error('Ошибка при выполнении поиска:', error);
        }
    });
}
