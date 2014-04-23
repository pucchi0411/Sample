$(function () {

    $("body").keydown(function (event) {
        if (event.ctrlKey === true && event.which === 13) {
            $("form").submit();
        }
    });

    setTimeout(refresh(), 1000);
});

function refresh() {
    var time = new Date().getTime();

    var reloader =  (function() {
        if (new Date().getTime() - time >= 5000) {
            console.log("reloaded");
            window.location.reload(true);
        } else {
            console.log("interval time not yet.");
            setTimeout(reloader, 1000);
        }
    });

    return reloader;
}