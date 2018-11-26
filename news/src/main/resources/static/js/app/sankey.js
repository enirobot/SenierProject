function initSankey(sankey_div){
    console.log("initSankey : " + sankey_div);
    var inputdata = [];
    google.charts.load('current', {'packages':['sankey']});

    $(document).ready(function(){
        $.ajax({
            url: "/sankey_major_post",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(null),
            success: function(data) {
                for (var i = 0; i < data.length; i++) {
                    inputdata.push( [ data[i].source, data[i].destination, data[i].value ] );
                }

                google.charts.setOnLoadCallback(drawChart);
            }
        })

    });


    function drawChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'From');
        data.addColumn('string', 'To');
        data.addColumn('number', 'Weight');
        data.addRows(inputdata);

        var colors = ['#a6cee3', '#b2df8a', '#fb9a99', '#fdbf6f',
            '#cab2d6', '#ffff99', '#1f78b4', '#33a02c'];

        // Sets chart options.
        var options = {
            width: window.innerWidth,
            height: window.innerHeight,
            sankey: {
                node: {
                    colors: colors
                },
                link: {
                    colorMode: 'gradient',
                    colors: colors
                }
            }
        };

        // sankey_div.width = window.innerWidth;
        // sankey_div.height = window.innerHeight;
        //
        // console.log(sankey_div.width + " : " + window.innerWidth);
        // console.log(sankey_div.height + " : " + window.innerHeight);

        // Instantiates and draws our chart, passing in some options.
        var chart = new google.visualization.Sankey(sankey_div);
        chart.draw(data, options);
    }

};
