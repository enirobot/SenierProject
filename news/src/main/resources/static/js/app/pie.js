function initPie(pie_div){
    console.log("pie : " + pie_div);
    var inputdata1 = [];
    google.charts.load("current", {packages: ["corechart"]});

    $(document).ready(function(){
        $.ajax({
            url: "/pie_post",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(null),
            success: function(data) {
                inputdata1.push(['category','value']);
                for (var i = 0; i < data.length; i++) {
                    inputdata1.push( [data[i].category, data[i].value]);
                    console.log([data[i].category+data[i].value]+"is");
                }
                var timeout1 = setInterval(function () {
                    if (google.visualization !== undefined) {
                        google.charts.setOnLoadCallback(drawChart1);
                        clearInterval(timeout1);
                    }
                }, 300);
                console.log(JSON.stringify(inputdata1));

            }
        })
    });

    function drawChart1() {
        console.log(JSON.stringify(inputdata1));
        var data = google.visualization.arrayToDataTable(inputdata1);

        var options = {
            title: 'Pie chart'
        };

        var chart = new google.visualization.PieChart(pie_div);
        chart.draw(data, options);
    }

};
