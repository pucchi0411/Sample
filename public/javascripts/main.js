$(function () {

    $("body").keydown(function (event) {
        if (event.ctrlKey === true && event.which === 13) {
            $("form").submit();
        }
    });

});