$("#select-map").on('change', () => {
    let selectedMap = $("#select-map").val();
    $("#select-house option").each(() => {
        if(selectedMap !== $(this).attr("data-group"))
            $(this).css("display", "none");
        else
            $(this).css("display", "block");
    });
});

