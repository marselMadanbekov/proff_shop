document.addEventListener('DOMContentLoaded', function () {

    const redirectOrder = document.getElementById('redirect');

    const orderId = document.getElementById('orderId').value;
    const updateOrder = document.getElementById('updateOrderItems');
    const deleteFromOrder = document.querySelectorAll('.removeFromOrder')
    const itemCountDowns = document.querySelectorAll('.itemCountDown');
    const itemCountUps = document.querySelectorAll('.itemCountUp');

    const filterButton = document.getElementById('filter');
    const cancelButton = document.getElementById('cancel');
    const paginationLinks = document.querySelectorAll('.pagination-link');

    let categoryId = document.getElementById('categorySelect').value;
    let size = document.getElementById('sizeSelect').value;
    let searchQuery = document.getElementById('searchQuery').value;
    let minPrice = document.getElementById('minPrice').value;
    let maxPrice = document.getElementById('maxPrice').value;
    let sort = document.getElementById('sortSelect').value;
    let page = document.getElementById('page').value;

    cancelButton.addEventListener('click',function (e){
        window.location.reload();
    })

    deleteFromOrder.forEach(function (item) {
        item.addEventListener('click', function (event) {
            event.preventDefault();
            let orderItemId = item.getAttribute("orderItemId");
            let formData = new FormData();
            formData.append('orderItemId', orderItemId);
            if (confirm("Вы уверены что хотите удалить товар из заказа?")) {
                $.ajax({
                    url: "/admin/order/removeItem",
                    type: "POST",
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (response) {
                        alert(response.message);
                        window.location.reload();
                    },
                    error: function (xhr, status, error) {

                        try {
                            const errorData = JSON.parse(xhr.responseText);
                            if (errorData.hasOwnProperty("error")) {
                                alert(errorData.error);
                            } else {
                                alert('Что то пошло не так');
                            }
                        } catch (e) {
                            alert('Что то пошло не так');
                        }
                    }
                });
            }
        });
    });
    updateOrder.addEventListener('click', function (event) {
        event.preventDefault();
        let orderItems = [];

        $('.newSize').each(function () {
            let orderItemId = $(this).attr('orderItemId');
            let productVariationId = $(this).val();
            orderItems.push({orderItemId: orderItemId, productVariationId: productVariationId});
        });

        $.ajax({
            url: '/admin/order/update-order',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(orderItems),
            success: function (response) {
                $("#spinner").hide();
                alert(response.message);
                window.location.reload();
            },
            error: function (xhr, status, error) {
                $("#spinner").hide();
                try {
                    const errorData = JSON.parse(xhr.responseText);
                    if (errorData.hasOwnProperty("error")) {
                        alert(errorData.error);
                    } else {
                        alert('Что то пошло не так');
                    }
                } catch (e) {
                    alert('Что то пошло не так');
                }
            }
        });
    });


    itemCountDowns.forEach(function (item){
        item.addEventListener('click', function (e){
            e.preventDefault();
            let orderItemId = item.getAttribute('orderItemId');
            let formData = new FormData();
            formData.append('orderItemId', orderItemId);
            $.ajax({
                url: "/admin/order/itemCountDown",
                type: "POST",
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
                            alert('Что то пошло не так');
                        }
                    } catch (e) {
                        alert('Что то пошло не так');
                    }
                }
            });
        })
    })
    itemCountUps.forEach(function (item){
        item.addEventListener('click', function (e){
            e.preventDefault();
            let orderItemId = item.getAttribute('orderItemId');
            let formData = new FormData();
            formData.append('orderItemId', orderItemId);
            $.ajax({
                url: "/admin/order/itemCountUp",
                type: "POST",
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
                            alert('Что то пошло не так');
                        }
                    } catch (e) {
                        alert('Что то пошло не так');
                    }
                }
            });
        })
    })

    redirectOrder.addEventListener('click', function (e){
        e.preventDefault();
        let targetStore = document.getElementById('targetStore').value;
        let formData = new FormData();
        formData.append('orderId', orderId);
        formData.append('targetStoreId', targetStore);
        if(confirm("Уверены что хотите перенаправить заказ?")) {
            $.ajax({
                url: "/admin/order/redirect",
                type: "POST",
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
                            alert('Что то пошло не так');
                        }
                    } catch (e) {
                        alert('Что то пошло не так');
                    }
                }

            });
        }
    })


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
           <button class="btn btn-sm btn-primary addToOrder" productId="${id}">Добавить</button>
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
            loadNewProducts();
        });
    });

    $('#productTable').on('click', '.addToOrder', function (e) {
        e.preventDefault();
        let button = $(this); // Получаем текущую кнопку
        let productId = button.attr('productId');
        let formData = new FormData();
        formData.append('orderId', orderId);
        formData.append('productId', productId);

        if (confirm('Вы уверены, что хотите добавить этот продукт в карточку?')) {
            $.ajax({
                url: '/admin/order/addToOrder',
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

    function loadNewProducts() {
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
})
