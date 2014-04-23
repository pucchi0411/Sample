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

    var reloader = (function () {
        if (new Date().getTime() - time >= 5000)
            window.location.reload(true);
        else
            setTimeout(reloader, 1000);
    });

    return reloader;
}