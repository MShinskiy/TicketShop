let selectedMap = $("#select-map option:selected").val();
let selectedLevel = $("#select-level option:selected").val();
let selectedHouse;
let selectedIds;
let validForm = true;
let token;
let header;
let csrfHeader;

$(document).ready(() => {
    token = $("meta[name='_csrf']").attr("content");
    header = $("meta[name='_csrf_header']").attr("content");
    csrfHeader = {};
    csrfHeader[header] = token;

    $("#select-map").on('change', () => {
        selectedMap = $("#select-map option:selected").val();
        changeHouseVisibility();
        changeOptionsVisibility();
        changeEditPaneVisibility();
        updateIds()
    });

    $("#select-level").on('change', () => {
        selectedLevel = $("#select-level option:selected").val();
        changeHouseVisibility();
        changeOptionsVisibility();
        changeEditPaneVisibility();
        updateIds()
    });

    $("#select-house").on('change', () => {
        selectedHouse = $("#select-house option:selected").val();
        changeEditPaneVisibility();
    });

    $("input[input-param='progress-desc']").each(function () {
        $(this).on('keyup', () => {
            let input = $(this).val();
            let isvalidinput = isValidInput(input);
            if (isvalidinput)
                $(this).css("background-color", "");
            else
                $(this).css("background-color", "#FDD6D6");

            validForm = isvalidinput;
        });
    });

    $("#submit-form").on('click', function () {
        $(this).prop("disabled", !validForm);
        if (!validForm)
            return
        if (confirm("Вы уверены, что хотите внести изменения?")) {
            $.ajax({
                    url: "/townofgames/administration/edit",
                    method: 'post',
                    data: {
                        edits: JSON.stringify(parseAllTables())
                    },
                    headers: csrfHeader
                }
            );
        }
    });

    $("#submit-demo-form").on('click', function () {
        $(this).prop("disabled", !validForm);
        if (!validForm)
            return

        $.ajax({
            url: "/townofgames/administration/demo",
            type: 'post',
            data: {
                edits: JSON.stringify(parseTables())
            },
            headers: csrfHeader,
            success: function (htmlContent) {
                let newTab = window.open();
                newTab.document.write(htmlContent);
            }
        });
    });

    updateIds();
    changeHouseVisibility();
});

function isValidInput(input) {
    const regex = /^(?:[^{]*{\d}[^{]*){0,3}$/;
    const matches = input.match(regex);
    return matches != null && matches.length > 0;
}

function changeHouseVisibility() {
    $("#select-house option").each(function () {
        if (selectedMap === $(this).attr("data-group") && selectedLevel === $(this).attr("data-level"))
            $(this).css("display", "block");
        else
            $(this).css("display", "none");
    });
}

function changeEditPaneVisibility() {
    selectedHouse = $("#select-house option:selected").val();
    $("table").each(function () {
        if (selectedHouse === $(this).attr("table-for"))
            $(this).css("display", "block");
        else
            $(this).css("display", "none");
    });
}

function changeOptionsVisibility() {
    $("#select-house option").each(function () {
        if (selectedMap === $(this).attr("data-group") && selectedLevel === $(this).attr("data-level"))
            $(this).css("display", "block");
        else
            $(this).css("display", "none");
    });
}

function updateIds() {
    selectedIds = [];
    $("#select-house option").each(function () {
        if (selectedMap === $(this).attr("data-group") && selectedLevel === $(this).attr("data-level"))
            selectedIds.push($(this).val());
    });
}

function parseTables() {
    let edits = [];
    $("table").each(function () {
        let id = $(this).attr("table-for");
        if (selectedIds.includes(id)) {
            edits.push({
                uuid: id,
                name: $(this).find("input[name='name']").val(),
                mapId: $(this).attr("house-map-id"),
                progress: $(this).find("input[name='progress-desc']").val(),
                description: $(this).find("input[name='description']").val(),
                task1: $(this).find("input[name='task1']").val(),
                task2: $(this).find("input[name='task2']").val(),
                text1: $(this).find("input[name='btn-text1']").val(),
                url1: $(this).find("input[name='btn-url1']").val(),
                text2: $(this).find("input[name='btn-text2']").val(),
                url2: $(this).find("input[name='btn-url2']").val(),
                text3: $(this).find("input[name='btn-text3']").val(),
                url3: $(this).find("input[name='btn-url3']").val(),
                caption: $(this).find("input[name='caption']").val(),
                level: $(this).attr("house-level"),
                group: $(this).attr("house-group")
            });
        }
    });
    return edits;
}

function parseAllTables() {
    let edits = [];
    $("table").each(function () {
        edits.push({
            uuid: $(this).attr("table-for"),
            name: $(this).find("input[name='name']").val(),
            mapId: $(this).attr("house-map-id"),
            progress: $(this).find("input[name='progress-desc']").val(),
            description: $(this).find("input[name='description']").val(),
            task1: $(this).find("input[name='task1']").val(),
            task2: $(this).find("input[name='task2']").val(),
            text1: $(this).find("input[name='btn-text1']").val(),
            url1: $(this).find("input[name='btn-url1']").val(),
            text2: $(this).find("input[name='btn-text2']").val(),
            url2: $(this).find("input[name='btn-url2']").val(),
            text3: $(this).find("input[name='btn-text3']").val(),
            url3: $(this).find("input[name='btn-url3']").val(),
            caption: $(this).find("input[name='caption']").val(),
            level: $(this).attr("house-level"),
            group: $(this).attr("house-group")
        });

    });
    return edits;
}


