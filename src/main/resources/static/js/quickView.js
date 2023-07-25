function updateModalData(productInfo) {
    console.log(productInfo);
    // Получаем ссылки на элементы по их идентификаторам
    const titleElement = document.getElementById("quickViewTitle");
    const newPriceElement = document.getElementById("quickViewNewPrice");
    const oldPriceElement = document.getElementById("quickViewOldPrice");
    const descriptionElement = document.getElementById("quickViewDescription");
    const productIdElement = document.getElementById("productId");

    // Устанавливаем новые значения для элементов
    titleElement.textContent = productInfo.name;
    newPriceElement.textContent = productInfo.price;
    oldPriceElement.textContent = productInfo.price;
    productIdElement.value = productInfo.id;
    descriptionElement.textContent = productInfo.description;
}
