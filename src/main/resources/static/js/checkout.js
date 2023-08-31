document.addEventListener('DOMContentLoaded', function() {
    const states = document.getElementById('state');
    const towns = document.getElementById('town');
    const scost = document.getElementById('scost');
    const checkbox = document.getElementById('shipment');

    const orderDetails = document.getElementById('orderDetails');

    const applyCouponForm = document.getElementById('couponForm');
    const removeCoupon = document.getElementById('remove-coupon');
    removeCoupon.addEventListener('click',function (e){
        e.preventDefault();
        $.ajax({
            url: "/cart/remove-coupon",
            type: "POST",
            success: function (response) {
                $("#spinner").hide();
                showModal('Купон успешно применен', response.message);
            },
            error: function (xhr, status, error) {
                $("#spinner").hide();
                try {
                    const errorData = JSON.parse(xhr.responseText);
                    if (errorData.hasOwnProperty("error")) {
                        showModal('Error', errorData.error);
                    } else {
                        showModal('Error', 'An error occurred while processing the request.');
                    }
                } catch (e) {
                    showModal('Error', 'An error occurred while processing the request.');
                }
            }
        });
    })

    applyCouponForm.addEventListener('submit', function (e) {
        e.preventDefault();
        let formData = new FormData(this);
        $.ajax({
            url: "/cart/apply-coupon",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                $("#spinner").hide();
                showModal('Купон успешно применен', response.message);
            },
            error: function (xhr, status, error) {
                $("#spinner").hide();
                try {
                    const errorData = JSON.parse(xhr.responseText);
                    if (errorData.hasOwnProperty("error")) {
                        showModal('Error', errorData.error);
                    } else {
                        showModal('Error', 'An error occurred while processing the request.');
                    }
                } catch (e) {
                    showModal('Error', 'An error occurred while processing the request.');
                }
            }
        });
    })

    orderDetails.addEventListener('submit', function (e){
        e.preventDefault();
        const formData = new FormData(this);
        formData.append('toShip', checkbox.checked);
        console.log(formData);
        $("#spinner1").show();

        $.ajax({
            url: '/checkout/order', // Замените на адрес вашего сервера
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            success: function(response) {
                $("#spinner1").hide();
                showModal("Заказ", response.message)
            },
            error: function(xhr, status, error) {
                $("#spinner1").hide();
                try {
                    const errorData = JSON.parse(xhr.responseText);
                    if (errorData.hasOwnProperty("error")) {
                        showModal('Order',errorData.error);
                    } else {
                        showModal('order','Что-то пошло не так');
                    }
                } catch (e) {
                    showModal('Order','Что-то пошло не так');
                }
            }
        });
    })

    states.addEventListener('change', function (e){
        e.preventDefault();
        let state = states.value;
        let town = towns.value;

        $("#spinner1").show();
        console.log(state + 'state.' + town + 'town.');
        $.ajax({
            url: "/checkout/towns?state=" + encodeURIComponent(state),
            type: "GET",
            success: function (data) {
                towns.innerHTML = '';

                // Добавляем новые опции на основе полученных данных
                data.forEach(function(town) {
                    let option = document.createElement('option');
                    option.value = town;
                    option.textContent = town;
                    towns.appendChild(option);
                });
                $("#spinner1").hide();
            },
            error: function (xhr,status,error) {
                $("#spinner1").hide();
            }
        });
    })

    scost.addEventListener('click',function (e){
        let state = states.value;
        let town = towns.value;

        $("#spinner1").show();
        console.log(state + 'state.' + town + 'town.');
        $.ajax({
            url: "/checkout/calculateShipping?state=" + encodeURIComponent(state) +'&town=' + encodeURIComponent(town),
            type: "GET",
            success: function (data) {
                const shippingPriceElement = document.getElementById('shippingPrice');
                shippingPriceElement.innerHTML = `<strong>${data} сом</strong>`;

                const cartSubtotal = parseFloat(document.getElementById('orderTotal').textContent);
                const orderTotalElement = document.getElementById('orderTotal');
                const newOrderTotal = cartSubtotal + data;
                orderTotalElement.innerHTML = `<strong>${newOrderTotal} сом</strong>`;
                $("#spinner1").hide();
            },
            error: function (xhr,status,error) {
                $("#spinner1").hide();
            }
        });
    })
    function showModal(title, message) {
        const modal = document.getElementById('modal');
        const modalMessage = document.getElementById('modalMessage');
        modalMessage.textContent = message;

        // Отображаем модальное окно
        modal.style.display = 'block';

        // Закрытие модального окна при клике на крестик
        document.getElementById('closeModal').addEventListener('click', function() {
            modal.style.display = 'none';
        });

        // Закрытие модального окна при клике вне его области
        window.addEventListener('click', function(event) {
            if (event.target === modal) {
                modal.style.display = 'none';
            }
        });
    }
});
