function initLineChart(divObj) {
    google.charts.load('current', {'packages':['line']});

    var inputdata1 = [];
    var inputdata2 = [];
    var inputdata3 = [];

    var data = null;

    $.ajax({
        url: "/lineChart",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        async: false,
        data: JSON.stringify(data),
        success: function (data) {//안정된 상태일 때 단어 총 3개로 파싱
            var i = 0;

            for (i; i < data.length; i++) {
                inputdata1.push( [ data[i].word, data[i].counts, data[i].date ] );
                if(data[i].word !== data[i+1].word)
                {
                    i++;
                    break;
                }
                // document.write(data[i].counts);
                //document.write(inputdata1[0][0]);
                //document.write(inputdata1[0][1]);
                console.log(i);
            }

            for (i; i < data.length; i++) {
                inputdata2.push( [ data[i].word, data[i].counts, data[i].date ] );
                if(data[i].word !== data[i+1].word)
                {
                    i++;
                    break;
                }
                console.log(i);
            }

            for (i; i < data.length; i++) {
                inputdata3.push( [ data[i].word, data[i].counts, data[i].date ] );
                console.log(i);
            }

            google.charts.setOnLoadCallback(drawChart());

            },
        error: function () {
            alert("fail");
        }
    })


    function drawChart() {

        var data = new google.visualization.DataTable();
        data.addColumn('date', 'Date');
        data.addColumn('number', inputdata1[0][0]);
        data.addColumn('number', inputdata2[0][0]);
        //data.addColumn('number', inputdata3[0][0]); //안정된 상태일 때

        var arr=[];
        //시간 파싱
        for (var i=0; i < 6; i++){
            var fullDate = inputdata1[i][2];
            fullDate = fullDate.split(' ');

            var date = fullDate[0].split('/');
            var time = fullDate[1];

            var newDate = date[0] + '/' + date[1] + '/' + date[2] + ' ' + time;
            arr[i] = new Date(newDate);
        }
        data.addRows([
            //임시
            [arr[0],  inputdata1[0][1], inputdata2[0][1]],
            [arr[1],  inputdata1[1][1], inputdata2[1][1]],
            [arr[2],  inputdata1[2][1], inputdata2[2][1]],
            [arr[3],  inputdata1[3][1], inputdata2[3][1]],
            [arr[4],  inputdata1[4][1], inputdata2[4][1]],
            [arr[5],  inputdata1[5][1], inputdata2[5][1]]
            //[3,  inputdata1[2][1], inputdata2[2][1], inputdata3[2][1]]
            //3개일때 --> 안정된 상태
        ]);

        var options = {
            chart: {
                title: 'Top Total Weight Words\' Frequency',
                subtitle: 'By Date Interval'
            },
            width: 900,
            height: 500
        };

        var chart = new google.charts.Line(divObj);

        chart.draw(data, google.charts.Line.convertOptions(options));
    }

}