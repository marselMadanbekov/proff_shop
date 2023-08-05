(function ($) {
    "use strict"
    document.addEventListener('DOMContentLoaded', function () {
        // Получите ссылки на карты
        const mapForCategory = document.getElementById('categoriesCard');
        const mapForProducts = document.getElementById('productsCard');
        const mapForFilters = document.getElementById('productFilter')

        // Получите ссылку на выпадающий список
        var selectType = document.getElementById('selectType');

        // Добавьте слушатель события изменения значения выпадающего списка
        selectType.addEventListener('change', function () {
            // Получите выбранное значение
            var selectedValue = selectType.value;

            // Проверьте выбранное значение и установите видимость карт
            if (selectedValue === '1') {
                mapForCategory.style.display = 'block'; // Показать карту для категорий
                mapForProducts.style.display = 'none'; // Скрыть карту для продуктов
                mapForFilters.style.display = 'none';
            } else if (selectedValue === '2') {
                mapForCategory.style.display = 'none'; // Скрыть карту для категорий
                mapForProducts.style.display = 'block'; // Показать карту для продуктов
                mapForFilters.style.display = 'block';
            } else {
                mapForCategory.style.display = 'none'; // Скрыть все карты, если ни одна из опций не выбрана
                mapForProducts.style.display = 'none';
                mapForFilters.style.display = 'none';
            }
        });
    });
})(jQuery);
