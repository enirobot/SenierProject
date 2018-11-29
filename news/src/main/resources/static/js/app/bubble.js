function initBubble(bubble_div){
    console.log("initBubble : " + bubble_div);
    var inputdata1 = [];
    google.charts.load("current", {packages: ["corechart"]});

    $(document).ready(function(){
        $.ajax({
            url: "/bubble_post",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(null),
            success: function(data) {
                inputdata1.push(['키워드','총가중치','기사수','감정수치','총댓글수']);
                for (var i = 0; i < data.length; i++) {
                    inputdata1.push( [data[i].word, data[i].totalWeight,data[i].counts,data[i].totalEmotionWeight,data[i].totalcommentCount]);
                    console.log([data[i].word+data[i].totalWeight+" "+data[i].totalcommentCount+" "+data[i].totalEmotionWeight+" "+data[i].counts]);
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

            width: window.innerWidth,
            height: window.innerHeight,
            title: 'Bubble chart(-부정적인키워드+긍정적인키워드)',
            hAxis: {title: 'TotalWeight(가중치)'},
            vAxis: {minValue:0,maxValue:15,title: 'Count(기사수)'},
            colorAxis: {minValue:-10,maxValue:50,colors: ['yellow', 'red']},////////////////

            bubble: {textStyle: {fontSize: 10}}
        };

        var chart = new google.visualization.BubbleChart(bubble_div);
        chart.draw(data, options);
    }

};
