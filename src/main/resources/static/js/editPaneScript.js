let selectedMap = "";
let selectedLevel = "";
let selectedHouse;

$("#select-map").on('change', () => {
    selectedMap = $("#select-map option:selected").val();
});

$("#select-level").on('change', () => {
    selectedLevel = $("#select-level option:selected").val();
});

$("#select-house option").each(() => {
    if(selectedMap === $(this).attr("data-group") && selectedLevel === $(this).attr("data-level"))
        $(this).css("display", "block");
    else
        $(this).css("display", "none");

    $(this).on('change', () => {
        selectedHouse = $("#select-house option:selected").val();
        $("table").each(() => {
            if(selectedHouse === $(this).attr("table-for"))
                $(this).css("display", "block");
            else
                $(this).css("display", "none");
        });
    });
});


