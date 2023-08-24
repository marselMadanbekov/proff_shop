document.addEventListener('DOMContentLoaded', function() {
    const states = document.getElementById('state');
    const towns = document.getElementById('town');
    const scost = document.getElementById('newShipment');
    const deleteShipment = document.getElementById('removeShipment');
    deleteShipment.addEventListener('click', function (e){
        e.preventDefault();
        let orderId = this.getAttribute('orderId');
        if(confirm("Уверены что хотите удалить доставку заказа")){
            $.ajax({
                url: "/admin/order/removeShipment?orderId=" + orderId,
                type: "GET",
                success: function (response) {
                    alert(response.message);
                    window.location.reload();
                },
                error: function (xhr,status,error) {
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

    scost.addEventListener('click',function (e){
        let state = states.value;
        let town = towns.value;
        let orderId = this.getAttribute('orderId');
        let formData = new FormData();
        formData.append('state',state);
        formData.append('town', town);
        formData.append('orderId',orderId);
        $("#spinner1").show();
        $.ajax({
            url: "/admin/order/newShipment",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                $("#spinner1").hide();
                alert(response.message);
                window.location.reload();
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
});
