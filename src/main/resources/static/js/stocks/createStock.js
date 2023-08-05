const selectedProducts = {};
const selectedCategory = {};


let size = document.getElementById('sizeSelect').value;
let searchQuery = document.getElementById('searchQuery').value;
let minPrice = 0;
let maxPrice = 0;
let sort = document.getElementById('sortSelect').value;

function updateProductsTable(data) {
    const tableBody = $('#productTable tbody'); // Выбираем tbody таблицы
    tableBody.empty(); // Очищаем содержимое tbody

    // Генерируем HTML для новых строк таблицы на основе полученных данных
    data.forEach(function (product) {
        const isChecked = selectedProducts[product.id] ? 'checked' : ''; // Проверяем, выбран ли продукт
        const photoUrl = product.photos.length > 0 ? `/products/${product.photos[0]}` : ''; // Получаем URL первого фото, если оно существует
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
    const pagination = document.getElementById('productPagination');

    // Генерируем ссылки для каждой страницы
    let paginationHtml = '';

    if (data.number > 0) {
        paginationHtml += `<li>
                                <a href="#" class="page-link prev-page-link" data-page="${0}">&laquo;</a>
                           </li>`;
    }


    if(data.number > 1){
        paginationHtml += `<li>
                                <a href="#" class="page-link prev-page-link" data-page="${data.number - 1}">${data.number}</a>
                           </li>`;
    }

    paginationHtml +=  `<li class="current">
                                <a href="#" >${data.number + 1}</a>
                           </li>`;

    if (data.number + 1 < data.totalPages) {
        paginationHtml += `<li>
                                <a href="#" class="page-link next-page-link" data-page="${data.number + 1}">${data.number + 2}</a>
                           </li>`;
        paginationHtml += `<li>
                                <a href="#" class="page-link next-page-link" data-page="${data.totalPages - 1}">&raquo;</a>
                           </li>`;
    }

    // Добавляем ссылки в пагинацию
    pagination.querySelector('ul').innerHTML = paginationHtml;
}
function updateCategoryPagination(data) {
    const pagination = document.getElementById('categoryPagination');

    // Генерируем ссылки для каждой страницы
    let paginationHtml = '';

    if (data.number > 0) {
        paginationHtml += `<li>
                                <a href="#" class="page-link prev-page-link" data-page="${0}">&laquo;</a>
                           </li>`;
    }


    if(data.number > 1){
        paginationHtml += `<li>
                                <a href="#" class="page-link prev-page-link" data-page="${data.number - 1}">${data.number}</a>
                           </li>`;
    }

    paginationHtml +=  `<li class="current">
                                <a href="#" >${data.number + 1}</a>
                           </li>`;

    if (data.number + 1 < data.totalPages) {
        paginationHtml += `<li>
                                <a href="#" class="page-link next-page-link" data-page="${data.number + 1}">${data.number + 2}</a>
                           </li>`;
        paginationHtml += `<li>
                                <a href="#" class="page-link next-page-link" data-page="${data.totalPages - 1}">&raquo;</a>
                           </li>`;
    }

    // Добавляем ссылки в пагинацию
    pagination.querySelector('ul').innerHTML = paginationHtml;
}

function loadNewProducts(pageNumber) {
    let baseURL = '/admin/filter?';
    let queryParams = '';

    queryParams += 'size=' + encodeURIComponent(size) + '&';
    queryParams += 'page=' + encodeURIComponent(pageNumber);
    queryParams += '&minPrice=' + encodeURIComponent(minPrice);
    queryParams += '&maxPrice=' + encodeURIComponent(maxPrice);
    queryParams += '&sort=' + encodeURIComponent(sort);
    if (searchQuery !== null)
        queryParams += '&query=' + encodeURIComponent(searchQuery);

    $.ajax({
        url: baseURL + queryParams,
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
$(document).on("click", "#filter", function (e) {
    e.preventDefault();

    size = document.getElementById('sizeSelect').value;
    searchQuery = document.getElementById('searchQuery').value;
    minPrice = document.getElementById('minPrice').value;
    maxPrice = document.getElementById('maxPrice').value;
    sort = document.getElementById('sortSelect').value;
    loadNewProducts(0);
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

    // Отправка запроса на сервер
    $.ajax({
        url: '/admin/products/search?query=' + searchQuery, // URL вашего серверного маршрута для выполнения поиска продуктов
        method: 'GET',
        contentType: 'application/json',
        dataType: "json",
        success: function (response) {
            // Обновление таблицы с продуктами
            updateProductsTable(response.getContent());
            updateProductsPagination(response);
        },
        error: function (xhr, status, error) {
            console.error('Ошибка при выполнении поиска:', error);
        }
    });
}
