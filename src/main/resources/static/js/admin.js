function sendTokenDelete() {
    fetch("/admin/signout", {
        method: 'get',
        headers: {
            'content-type': 'application/json',
            'Authorization': localStorage.getItem('Authorization'),
        }
    })
    localStorage.removeItem('Authorization');
}
