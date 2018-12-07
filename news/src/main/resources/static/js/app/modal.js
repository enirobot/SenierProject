
// When the user clicks on the button, open the modal
function modalOpen() {
    var modal = document.getElementById('myModal');
    var span = document.getElementsByClassName("close")[0];

    $('#myModal').fadeIn(120);

    // When the user clicks on <span> (x), close the modal
    span.onclick = function() {

        $("#modal_list").empty();
        modal.style.display = "none";
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        if (event.target == modal) {

            $("#modal_list").empty();
            modal.style.display = "none";
        }
    }
}

function modalClose() {
    var modal = document.getElementById('myModal');

    $("#modal_list").empty();
    modal.style.display = "none";
}
