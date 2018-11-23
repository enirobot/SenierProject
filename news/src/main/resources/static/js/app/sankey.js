var inputdata1 = [];
var inputdata2 = [];
var inputdata3 = [];



function initSankey(){
    $.ajax({
        url: "/sankey_major_post",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(null),
        success: function(data) {
            for (var i = 0; i < data.length; i++) {
                inputdata1.push( [ data[i].source, data[i].destination, data[i].value ] );
            }

        }
    })

    $.ajax({
        url: "/sankey_minor_post",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(null),
        success: function(data) {
            for (var i = 0; i < data.length; i++) {
                inputdata2.push( [ data[i].source, data[i].destination, data[i].value ] );
            }
        }
    })
    $.ajax({
        url: "/sankey_sports_post",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(null),
        success: function(data) {
            for (var i = 0; i < data.length; i++) {
                inputdata3.push( [ data[i].source, data[i].destination, data[i].value ] );
            }

            google.charts.load('current', {'packages':['sankey']});
            google.charts.setOnLoadCallback(drawChart1);
            google.charts.setOnLoadCallback(drawChart2);
            google.charts.setOnLoadCallback(drawChart3);
        }
    })
};


function drawChart1() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'From');
    data.addColumn('string', 'To');
    data.addColumn('number', 'Weight');
    data.addRows(inputdata1);

    var colors = ['#a6cee3', '#b2df8a', '#fb9a99', '#fdbf6f',
        '#cab2d6', '#ffff99', '#1f78b4', '#33a02c'];

    // Sets chart options.
    var options = {
        width: 400,
        height: 400,
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

    // Instantiates and draws our chart, passing in some options.
    var chart = new google.visualization.Sankey(document.getElementById('sankey_major'));
    chart.draw(data, options);
}

function drawChart2() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'From');
    data.addColumn('string', 'To');
    data.addColumn('number', 'Weight');
    data.addRows(inputdata2);

    var colors = ['#a6cee3', '#b2df8a', '#fb9a99', '#fdbf6f',
        '#cab2d6', '#ffff99', '#1f78b4', '#33a02c'];

    // Sets chart options.
    var options = {
        width: 400,
        height: 400,
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

    // Instantiates and draws our chart, passing in some options.
    var chart = new google.visualization.Sankey(document.getElementById('sankey_minor'));
    chart.draw(data, options);
}
function drawChart3() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'From');
    data.addColumn('string', 'To');
    data.addColumn('number', 'Weight');
    data.addRows(inputdata3);

    var colors = ['#a6cee3', '#b2df8a', '#fb9a99', '#fdbf6f',
        '#cab2d6', '#ffff99', '#1f78b4', '#33a02c'];

    // Sets chart options.
    var options = {
        width: 400,
        height: 400,
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

    // Instantiates and draws our chart, passing in some options.
    var chart = new google.visualization.Sankey(document.getElementById('sankey_sports'));
    chart.draw(data, options);
}
