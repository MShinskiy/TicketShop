const origin = window.location.origin
$(document).ready(() => {
    $("#fill-button").on("click", () => {
        $.get( origin + "/db/fill");
    });

    $("#delete-button").on("click", () => {
        $.ajax({
            url:origin + "/db/delete",
            type: 'DELETE'
        });
    });
});


