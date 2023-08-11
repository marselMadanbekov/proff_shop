document.addEventListener('DOMContentLoaded', function() {
    const states = document.getElementById('state');
    const towns = document.getElementById('town');
    const scost = document.getElementById('scost');
    const checkbox = document.getElementById('shipment');

    const orderDetails = document.getElementById('orderDetails');

    orderDetails.addEventListener('submit', function (e){
        e.preventDefault();
        const formData = new FormData(this);
        formData.append('toShip', checkbox.checked);
        console.log(formData);
        $("#spinner").show();

        $.ajax({
            url: '/checkout/order', // Замените на адрес вашего сервера
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            success: function(response) {
                console.log(response)
                $("#spinner").hide();
            },
            error: function(xhr, status, error) {
                console.log(xhr.error)
            }
        });
    })

    states.addEventListener('change', function (e){
        e.preventDefault();
        let state = states.value;
        let town = towns.value;

        $("#spinner").show();
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
                $("#spinner").hide();
            },
            error: function (xhr,status,error) {
                $("#spinner").hide();
            }
        });
    })

    scost.addEventListener('click',function (e){
        let state = states.value;
        let town = towns.value;

        $("#spinner").show();
        console.log(state + 'state.' + town + 'town.');
        $.ajax({
            url: "/checkout/calculateShipping?state=" + encodeURIComponent(state) +'&town=' + encodeURIComponent(town),
            type: "GET",
            success: function (data) {
                const shippingPriceElement = document.getElementById('shippingPrice');
                shippingPriceElement.innerHTML = `<strong>${data} сом</strong>`;

                const cartSubtotal = parseFloat(document.getElementById('cartSubtotal').textContent);
                const orderTotalElement = document.getElementById('orderTotal');
                const newOrderTotal = cartSubtotal + data;
                orderTotalElement.innerHTML = `<strong>${newOrderTotal} сом</strong>`;
                $("#spinner").hide();
            },
            error: function (xhr,status,error) {
                $("#spinner").hide();
            }
        });
    })



});
