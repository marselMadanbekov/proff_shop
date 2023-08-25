(function ($) {
    "use strict";

    const paginationLinks = document.querySelectorAll('.pagination-link');
    const deleteFromGroups = document.querySelectorAll('.deleteFromGroup');
    const addToGroup = document.querySelectorAll('.addToGroup');
    const groupId = document.getElementById('productGroupId').value;
    const filterButton = document.getElementById('filter');
    const cancelButton = document.getElementById('cancel');

    let categoryId = document.getElementById('categorySelect').value;
    let size = document.getElementById('sizeSelect').value;
    let searchQuery = document.getElementById('searchQuery').value;
    let minPrice = document.getElementById('minPrice').value;
    let maxPrice = document.getElementById('maxPrice').value;
    let sort = document.getElementById('sortSelect').value;
    let page = document.getElementById('page').value;

    cancelButton.addEventListener('click', () => {
        window.location.href = '/admin/productGroupDetails?groupId=' + groupId;
    })


    deleteFromGroups.forEach(function(link) {
        link.addEventListener('click', function(event) {
            event.preventDefault();
            let productId = link.getAttribute('productId');
            if (confirm('Вы уверены, что хотите удалить этот продукт из карточки?')) {
                $.ajax({
                    url: '/admin/products/removeFromGroup',
                    type: 'POST',
                    data: { groupId: groupId,
                            productId: productId
                    },
                    success: function (response) {
                        // Обработка успешного удаления
                        alert(response.message);
                        window.location.reload();
                        // Дополнительные действия при успешном удалении категории
                    },
                    error: function (xhr, status, error) {
                        if (xhr.responseJSON && xhr.responseJSON.error) {
                            alert(xhr.responseJSON.error);
                        } else {
                            alert('Произошла ошибка при удалении размера товара');
                        }
                    }
                });
            }
        });
    });

    filterButton.addEventListener('click', () => {
        categoryId = document.getElementById('categorySelect').value;
        size = document.getElementById('sizeSelect').value;
        searchQuery = document.getElementById('searchQuery').value;
        minPrice = document.getElementById('minPrice').value;
        maxPrice = document.getElementById('maxPrice').value;
        sort = document.getElementById('sortSelect').value;
        page = document.getElementById('page').value;
        loadNewProducts(page);
    })
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
    function updateProductsTable(data) {
        const tableBody = $('#productTable tbody'); // Выбираем tbody таблицы
        tableBody.empty(); // Очищаем содержимое tbody

        // Генерируем HTML для новых строк таблицы на основе полученных данных
        data.forEach(function (product) {
            const photoUrl = product.photos.length > 0 ? `/products/${product.photos[0]}` : ''; // Получаем URL первого фото, если оно существует
            const id = product.id;
            const row = `
    <tr>
      <td>
        ${photoUrl ? `<img src="${photoUrl}">` : ''}
      </td>
      <td>${product.name}</td>
      <td>
         <div>
           <button class="btn btn-sm btn-primary addToGroup" productId="${id}">Добавить</button>
         </div>
      </td>
    </tr>
  `;
            tableBody.append(row); // Добавляем сгенерированную строку в tbody
        });
    }

    paginationLinks.forEach(function(link) {
        link.addEventListener('click', function(event) {
            event.preventDefault();
            page = link.getAttribute('page');
            loadNewProducts(page);
        });
    });

    $('#productTable').on('click', '.addToGroup', function (e) {
        e.preventDefault();
        let button = $(this); // Получаем текущую кнопку
        let productId = button.attr('productId');
        let formData = new FormData();
        formData.append('groupId', groupId);
        formData.append('productId', productId);

        if (confirm('Вы уверены, что хотите добавить этот продукт в карточку?')) {
            $.ajax({
                url: '/admin/products/addToGroup',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    // Обработка успешного добавления
                    alert(response.message);
                    window.location.reload();
                },
                error: function (xhr, status, error) {
                    if (xhr.responseJSON && xhr.responseJSON.error) {
                        alert(xhr.responseJSON.error);
                    } else {
                        alert('Произошла ошибка при добавлении товара в карточку');
                    }
                }
            });
        }
    });

    function loadNewProducts(pageNumber) {
        let baseURL = '/admin/filter?';
        let queryParams = '';

        queryParams += 'categoryId=' + encodeURIComponent(categoryId) + '&';
        queryParams += 'page=' + encodeURIComponent(page);
        queryParams += '&minPrice=' + encodeURIComponent(minPrice);
        queryParams += '&maxPrice=' + encodeURIComponent(maxPrice);
        queryParams += '&sort=' + encodeURIComponent(sort);
        if(size !== null && size !== '')
            queryParams += '&size=' + encodeURIComponent(size);
        if (searchQuery !== null && searchQuery !== '')
            queryParams += '&query=' + encodeURIComponent(searchQuery);

        console.log(queryParams);
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
})(jQuery);
