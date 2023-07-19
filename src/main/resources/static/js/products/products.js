(function ($) {
    "use strict";
    window.showSelectedProductInfo = function (productId,productName, productPrice, productSKU,productCategory,productDescription) {
        // Обновить данные в таблице "Selected product"
        $('#selectedProductId').text(productId);
        $('#selectedProductName').text(productName);
        $('#selectedProductPrice').text(productPrice);
        $('#selectedProductSKU').text(productSKU);
        $('#selectedProductCategory').text(productCategory);
        $('#selectedProductDescription').text(productDescription);
        // Остальные столбцы товара
    }

    const moreButton = document.querySelector('#moreProductInfo');

// Обработчик события при нажатии на кнопку
    moreButton.addEventListener('click', () => {
        // Получение значения productId из скрытого элемента
        const productId = document.querySelector('#selectedProductId').textContent;
        window.location.href = '/admin/productDetails?productId=' + productId;
        // Формирование URL с параметром productId
    });
})(jQuery);
