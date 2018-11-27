function initGame() {
    $button_ele = null;
    $pack_in_wc = null;
    $pack_in_button = null;
    $pack_in_chart = null;

    var word = [];
    var count = [];
    var rand_arr = [];
    var score = 0;
    var cnt = 0;
    var index_cnt = 0;

    $(document).ready(function() {
        $.ajax({
            url: "/getKeyword",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(null)
        }).done(function (data){
            for (var i = 0; i < data.length; i++) {
                word.push(data[i].word);
                count.push(parseInt(data[i].totalWeight * 10));
            }
            RandomNum();
            main(0);
        });


    });
    function main(before_num) {

        //alert(before_num);

        $pack_in_wc = $('<div id="word" class="element"></div><div id="count" class="element"></div>');
        $pack_in_button = $('<div id="word" class="element"></div><div id="button1" class="button">UP</div><div id="button2" class="button">DOWN</div>');
        $pack_in_chart = $('<div class="wordTree"></div>');
        $end_box = $('<div class="end_box">Game over</div>');
        //alert('main');

        $l_term = $('.term_left ');
        $r_term = $('.term_right ');

        $r_term.animate({opacity : '0'},1);

        $l_term.append($pack_in_wc);
        $l_term.append($pack_in_chart);
        $r_term.append($pack_in_button);
        $pack_in_chart.clone().appendTo($r_term);

        if(before_num == 0)
            var l_num = rand_arr[index_cnt++];
        else
            var l_num = before_num;
        var r_num = rand_arr[index_cnt++];

        getNewsList(word[l_num], 0);
        getNewsList(word[r_num], 1);

        $l_term.children('#word').html(word[l_num]);
        $l_term.children('#count').html(count[l_num]);
        $r_term.children('#word').html(word[r_num]);


        $r_term.animate({opacity : '1'}, 1000);


        $("#button1").click(function() {
            $r_term.children().remove();
            $pack_in_wc.clone().appendTo($r_term);
            $r_term.children('#word').html(word[r_num]);
            $r_term.children('#count').html(count[r_num]);
            if(count[l_num] < count[r_num]){
                //맞음
                $l_term.animate({opacity : '0'},1000);
                $r_term.animate({left:'-50%'},1000,
                    function(){
                        $('.score_bar').html('점수 : ' +(++cnt));
                        $r_term.children().remove();  // 버튼 지움
                        $r_term.append($pack_in_wc);
                        $r_term.append($pack_in_chart); // 단어, 카운트, 차트 붙임
                        $r_term.children('#word').html(word[r_num]);
                        $r_term.children('#count').html(count[r_num]);  // 단어 카운트 값 입력
                        getNewsList(word[r_num], 1); // 차트 입력
                        $r_term.children().remove();
                        $r_term.removeAttr('style');
                        $l_term.removeAttr('style');
                        main(r_num);
                    });
            }
            else
            {
                alert('틀림');
                $r_term.append($end_box);
            }
        });
        $("#button2").click(function() {
            $r_term.children().remove();
            $pack_in_wc.clone().appendTo($r_term);
            $r_term.children('#word').html(word[r_num]);
            $r_term.children('#count').html(count[r_num]);
            if(count[l_num] > count[r_num]){
                //맞음
                $('.score_bar').html('점수 : ' +(++cnt));
                $l_term.animate({opacity : '0'},1000);
                $r_term.animate({left:'-50%'},1000,
                    function(){
                        $r_term.children().remove();  // 버튼 지움
                        $r_term.append($pack_in_wc);
                        $r_term.append($pack_in_chart); // 단어, 카운트, 차트 붙임
                        $r_term.children('#word').html(word[r_num]);
                        $r_term.children('#count').html(count[r_num]);  // 단어 카운트 값 입력
                        getNewsList(word[r_num], 1); // 차트 입력
                        $r_term.children().remove();
                        $r_term.removeAttr('style');
                        $l_term.removeAttr('style');
                        main(r_num);
                    });
            }
            else
            {
                alert('틀림');
                $r_term.append($end_box);
            }
        });

    }
    function RandomNum() // 한번도 나오지 않은 랜덤한 숫자 생성
    {
        while(rand_arr.length < word.length-1){
            var r = Math.floor(Math.random() * (word.length -1));
            if(rand_arr.indexOf(r) === -1) rand_arr.push(r);
        }
        console.log(JSON.stringify(rand_arr));
    }
    function getNewsList(word, num)
    {
        var title = [];

        $.ajax({
            url: "/getNewsList",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(word)
        }).done(function (data){
            for (var i = 0; i < data.length; i++) {
                var temp=[];
                temp.push(data[i].title);
                title.push(temp);
            }
            google.charts.load('current', {packages:['wordtree']});
            google.charts.setOnLoadCallback(function(){drawChart(title, word, num);});
        });


    }

    function drawChart(title, word, num) {
        var data = google.visualization.arrayToDataTable(title);

        var options = {
            colors : ['black','black','black'],
            maxFontSize: 14,
            wordtree: {
                format: 'implicit',
                word: word,
            },
            backgroundColor: 'none'
        };
        var chart = new google.visualization.WordTree($('.wordTree')[num]);
        chart.draw(data, options);
    }
}