document.addEventListener('DOMContentLoaded', function () {
    const updateOrder = document.getElementById('updateOrderItems');
    const deleteFromOrder = document.querySelectorAll('.removeFromOrder')
    const statusUp = document.getElementById('statusUp');
    const statusDown = document.getElementById('statusDown');
    const orderId = document.getElementById('orderId').value;

    statusUp.addEventListener('click', function (e){
        let formData = new FormData();
        formData.append('orderId' , orderId);
        if (confirm("Вы уверены что хотите продвинуть статус заказа?")) {
            $.ajax({
                url: "/admin/order/statusUp",
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
    })
    statusDown.addEventListener('click', function (e){
        let formData = new FormData();
        formData.append('orderId' , orderId);
        if (confirm("Вы уверены что хотите вернуть предыдущий статус заказа?")) {
            $.ajax({
                url: "/admin/order/statusDown",
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
    })
});
