function initSankey(divList){
    google.charts.load('current', {'packages':['sankey']});
    var sankey_major_data = [];
    var sankey_minor_data = [];
    var sankey_sports_data = [];

    $.ajax({
        url: "/sankey_major_post",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(null),
        success: function(data) {
            for (var i = 0; i < data.length; i++)
                sankey_major_data.push( [ data[i].source, data[i].destination, data[i].value ] );

            var timeout1 = setInterval(function () {
                if (google.visualization !== undefined) {
                    google.charts.setOnLoadCallback(drawChart1(divList[0], sankey_major_data));
                    clearInterval(timeout1);
                }
            }, 300);
        }
    })

    $.ajax({
        url: "/sankey_minor_post",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(null),
        success: function(data) {
            for (var i = 0; i < data.length; i++)
                sankey_minor_data.push( [ data[i].source, data[i].destination, data[i].value ] );

            var timeout2 = setInterval(function () {
                if (google.visualization !== undefined) {
                    google.charts.setOnLoadCallback(drawChart2(divList[1], sankey_minor_data));
                    clearInterval(timeout2);
                }
            }, 300);

        }
    })
    $.ajax({
        url: "/sankey_sports_post",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(null),
        success: function(data) {
            for (var i = 0; i < data.length; i++)
                sankey_sports_data.push( [ data[i].source, data[i].destination, data[i].value ] );

            var timeout3 = setInterval(function () {
                if (google.visualization !== undefined) {
                    google.charts.setOnLoadCallback(drawChart3(divList[2], sankey_sports_data));
                    clearInterval(timeout3);
                }
            }, 300);
        }
    })
};


function drawChart1(major_div, sankey_major_data) {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'From');
    data.addColumn('string', 'To');
    data.addColumn('number', 'Weight');
    data.addRows(sankey_major_data);

    var colors = ['#a6cee3', '#b2df8a', '#fb9a99', '#fdbf6f',
        '#cab2d6', '#ffff99', '#1f78b4', '#33a02c'];

    var parent = document.getElementById("viewer");

    // Sets chart options.
    var options = {
        width: parent.offsetWidth,
        height: parent.offsetHeight / 3,
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


    var chart = new google.visualization.Sankey(major_div);
    chart.draw(data, options);
}

function drawChart2(minor_div, sankey_minor_data) {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'From');
    data.addColumn('string', 'To');
    data.addColumn('number', 'Weight');
    data.addRows(sankey_minor_data);

    var colors = ['#a6cee3', '#b2df8a', '#fb9a99', '#fdbf6f',
        '#cab2d6', '#ffff99', '#1f78b4', '#33a02c'];

    var parent = document.getElementById("viewer");

    // Sets chart options.
    var options = {
        width: parent.offsetWidth,
        height: parent.offsetHeight / 3,
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
    var chart = new google.visualization.Sankey(minor_div);
    chart.draw(data, options);
}

function drawChart3(sports_div, sankey_sports_data) {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'From');
    data.addColumn('string', 'To');
    data.addColumn('number', 'Weight');
    data.addRows(sankey_sports_data);

    var colors = ['#a6cee3', '#b2df8a', '#fb9a99', '#fdbf6f',
        '#cab2d6', '#ffff99', '#1f78b4', '#33a02c'];

    var parent = document.getElementById("viewer");

    // Sets chart options.
    var options = {
        width: parent.offsetWidth,
        height: parent.offsetHeight / 3,
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
    var chart = new google.visualization.Sankey(sports_div);
    chart.draw(data, options);
}
