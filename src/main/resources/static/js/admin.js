let host = "http://" + window.location.host;

function sendTokenDelete() {
    fetch("/admin/signout", {
        method: 'get',
        headers: {
            'content-type': 'application/json',
            'Authorization': localStorage.getItem('Authorization'),
        }
    })
    localStorage.removeItem('Authorization');
    window.location.href = host + "/admin/templates/login";
}
