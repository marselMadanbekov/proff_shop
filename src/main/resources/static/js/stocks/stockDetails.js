(function ($) {
    "use strict";
    const removeLinks = document.querySelectorAll('.removeFromStock');

    removeLinks.forEach(function (item){
        item.addEventListener('click',function (e){
            e.preventDefault();
            let stockId = this.getAttribute('stockId');
            let productId = this.getAttribute('productId');
            let formData = new FormData();
            formData.append('stockId', stockId);
            formData.append('productId', productId);
            if (confirm('Вы уверены, что хотите удалить этот товар из акции?')) {
                $.ajax({
                    url: '/admin/stocks/remove-participant',
                    type: 'POST',
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
                                alert('An error occurred while processing the request.');
                            }
                        } catch (e) {
                            alert('An error occurred while processing the request.');
                        }
                    }
                });
            }
        })
    })

})(jQuery);
