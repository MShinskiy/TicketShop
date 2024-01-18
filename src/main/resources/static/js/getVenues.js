$('#dropdown').on('change', () => {
    let city =  $('#dropdown').val();
    $.get("/displayAllConcerts", {value:city}, function(json){
        let venuesArr = JSON.parse(json);
        for(const venue of venuesArr) {
            $('#dropdown2').append("<option value='" + venue +"'>" + venue + "</option>");
        }
    });
});