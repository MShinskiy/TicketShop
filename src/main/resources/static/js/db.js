const origin = window.location.origin
$(document).ready(() => {
    $("#fill-button").on("click", () => {
        $.ajax({
            url:origin + "/db/fill",
            type: 'GET',
            success: () => {
                $("#fill-button").css("background", "#DBFFDC");
            },
            error: () => {
                $("#fill-button").css("background", "#FFDBDB");
            }
        }).always(() => {
                resetColorAfterSeconds("fill-button", 1);
        });
        $("#fill-button").css("background", "#D1D1D1");
    });
    $("#delete-button").on("click", () => {
        $.ajax({
            url:origin + "/db/delete",
            type: 'DELETE',
            success: () => {
                $("#delete-button").css("background", "#DBFFDC");
            },
            error: () => {
                $("#delete-button").css("background", "#FFDBDB");
            }
        }).always(() => {
            resetColorAfterSeconds("delete-button", 1);
        });

        $("#delete-button").css("background", "#D1D1D1");

    });
});

function resetColorAfterSeconds(id, seconds) {
    setTimeout(() => {
        $("#" + id).css("background", "#FFFFFF")
    }, seconds * 1000);
}


