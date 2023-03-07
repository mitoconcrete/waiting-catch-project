// let host = "http://" + window.location.host;

function sendTokenDelete() {
    fetch("/api/admin/signout", {
        method: 'post'
    })
    document.cookie =
        'Authorization' + "=" + "" + "; " +
        "path=/; expires=Thu, 01 Jan 1970 00:00:01 GMT;";
    window.location.href = host + "/general/templates/admin/login";
}
