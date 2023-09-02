$(document).ready(function () {
    const salesChartCanvas = document.getElementById('salesChart');
    const prevMonthButton = document.getElementById('prevMonthButton');
    const nextMonthButton = document.getElementById('nextMonthButton');
    const productSize = document.getElementById('productSize');
    let month = 0;
    let salesChart = null;
    let productId = productSize.value;

    productSize.addEventListener('change',function (e){
        e.preventDefault();
        productId = productSize.value;
        updateChart();
    })

    prevMonthButton.addEventListener('click', function (e) {
        e.preventDefault();
        month += 1;
        updateChart();
    });

    nextMonthButton.addEventListener('click', function (e) {
        e.preventDefault();
        if (month > 0) {
            month -= 1;
            updateChart();
        } else {
            alert("Следующие месяцы недоступны");
        }
    });


    function updateChart() {
        if (salesChart) {
            salesChart.destroy();
        }

        $('#progressModal').modal('show');
        $.ajax({
            url: `/admin/sales/product/${productId}/${month}`,
            method: 'GET',
            dataType: 'json',
            success: function (data) {
                const months = data.map(item => item.month);
                const sales = data.map(item => item.sales);

                const chartData = {
                    labels: months,
                    datasets: [{
                        label: 'Продажи (шт)',
                        data: sales,
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1,
                    }],
                };

                salesChart = new Chart(salesChartCanvas, {
                    type: 'bar',
                    data: chartData,
                    options: {
                        responsive: true,
                        scales: {
                            y: {
                                beginAtZero: true,
                            },
                        },
                    },
                });
                $('#progressModal').modal('hide');
            },

            error: function (error) {
                alert("Произошла ошибка при загрузке данных. Попробуйте перезагрузить страницу");
            },
        });
    }

    updateChart();
});
