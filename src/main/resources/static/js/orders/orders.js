(function ($) {
    "use strict";

    const createOrderButton = document.getElementById('newOrderCreate');


    createOrderButton.addEventListener('click', () => {
        $.ajax({
            url: "/admin/orders/createOrder",
            type: "GET",
            success: function (response) {
                window.location.href = '/admin/orderDetails?orderId=' + response.message;
            },
            error: function (xhr,status,error) {
                $("#spinner1").hide();
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


})(jQuery);
