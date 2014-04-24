$(function () {
    setTimeout(refresh(), 1000);
});

function refresh() {
    var time = new Date().getTime();

    var reloader = (function () {
        if (new Date().getTime() - time >= 5000) {
            diff();
            time = new Date().getTime();
        }
        setTimeout(reloader, 1000);
    });

    return reloader;
}

function diff(){
    $.ajax({
        url: window.location.pathname + "/" + $("table tbody .id:last").text(),
        type: 'GET',
        timeout: 10000,
        async: true,
        cache:false,
        dataType: 'html'
    }).done(function( data, textStatus, jqXHR ) {
        console.log(data);
        lattestId = "";
        $("table tbody:last-child").append(data);
    });
}