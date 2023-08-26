document.addEventListener('DOMContentLoaded', function () {
    const statusUp = document.getElementById('statusUp');
    const statusDown = document.getElementById('statusDown');
    const orderId = document.getElementById('orderId').value;
    const deleteOrder = document.getElementById('deleteOrder');

    deleteOrder.addEventListener('click',function (e){
        if(confirm("Вы уверены что хотите отменить заказ? Заказ нельзя будет восстановить")) {
            $.ajax({
                url: "/admin/order/delete",
                type: "POST",
                data: {orderId : orderId},
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
});
