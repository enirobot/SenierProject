$(document).on('click', '.button', function () {
    console.log("버튼 눌림")

    main.findWordCloud()
});

$(document).keyup('#searchWordCloud', function () {
    if (event.keyCode === 13) {
        console.log("엔터 눌림")

        main.findWordCloud();
    }
});
