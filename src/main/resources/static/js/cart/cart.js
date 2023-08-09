document.addEventListener('DOMContentLoaded', function() {
    const addToCart = document.querySelectorAll('.add_toCart');


    addToCart.forEach(function(item) {
        item.addEventListener('click', function(event) {
            event.preventDefault();
            let selectedProductId = item.getAttribute("productId");
            $("#spinner").show();
            $.ajax({
                url: "/cart/add?productId=" + selectedProductId,
                type: "GET",
                success: function (data) {
                    $("#spinner").hide();

                },
                error: function (xhr,status,error) {
                    $("#spinner").hide();
                }
            });
        });
    });


});
