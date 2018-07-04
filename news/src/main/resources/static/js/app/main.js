function getWordCloud() {
    var param = {
        test : "안녕하세요"
    }

    $.ajax({
        type : "POST",
        url : "/mainWordCloud",
        dataType : 'json',
        contentType : 'application/json',
        data : param,
        success : successCall,
        error : errorCall
    });
}

function successCall () {
    alert("전송성공");
}

function errorCall() {
    alert("전송실패");
}