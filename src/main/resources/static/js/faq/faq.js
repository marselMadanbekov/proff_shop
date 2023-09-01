document.addEventListener('DOMContentLoaded', function() {
    const faqForm = document.getElementById('faqForm');
    const faqDeleteButtons = document.querySelectorAll(".deleteFaq");

    faqDeleteButtons.forEach(function (item){
        item.addEventListener("click",function (e){
            e.preventDefault();
            let faqId = item.getAttribute('faqId');
            let formData = new FormData();
            formData.append("faqId", faqId);
            if(confirm("Вы уверены что хотите удалить этот вопрос?")){
                $.ajax({
                    url: '/admin/faq/delete',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (response) {
                        window.location.reload()
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

    faqForm.addEventListener("submit", function (e){
        e.preventDefault();
        let formData = new FormData(this);
        $.ajax({
            url: '/admin/faq/create',
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
    })
});
