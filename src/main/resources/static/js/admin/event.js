$(document).ready(function () {
    const auth = getToken();
    closeUpdate();


})

function openUpdate() {
    $('#updateBox').show()
}

function closeUpdate() {
    $('#updateBox').hide()
}

function createEvents() {
    var eventName = $('#eventName').val();
    let eventStartDate = $('#eventStartDate').val();
    // let eventStartDate = document.getElementById('eventStartDate').toString();
    let eventEndDate = $('#eventEndDate').val();
    // let eventEndDate = document.getElementById('eventStartDate').toString();
    // let date = new Date(new Date().getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, -8);
    console.log(eventStartDate);

    let data = {
        'eventName': eventName,
        'eventStartDate': eventStartDate,
        'eventEndDate': eventEndDate
    }

    const auth = getToken();

    $.ajax({
        type: "POST",
        url: "/admin/events",
        contentType: "application/json",
        data: JSON.stringify(data),
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", auth);
        },
        success: function (response) {
            alert('이벤트 생성 성공')
            window.location.reload();
        }
    })
}

function getEvents() {
    const auth = getToken();
    $('#eventsTable').empty();

    $.ajax({
        type: "GET",
        url: "/admin/events",
        contentType: "application/json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", auth);
        },
        success: function (response) {
            let rows = response['eventsResponses']
            for (let i = 0; i < rows.length; i++) {
                let id = rows[i]['id'];
                let name = rows[i]['name'];
                let eventStartDate = rows[i]['eventStartDate'];
                let eventEndDate = rows[i]['eventEndDate'];
                let tempHtml = `<tr>
                                    <th scope="row">${id}</th>
                                    <td>${name}</td>
                                    <td>${eventStartDate}</td>
                                    <td>${eventEndDate}</td>
                                    <td class="blacklist-request-btn">
                                        <div class="eventBtn">
                                            <button onclick="openUpdate()" type="button" class="btn">수정</button>
                                            <button type="button" class="btn">삭제</button>
                                        </div>
                                    </td>
                                </tr>`
                $('#eventsTable').append(tempHtml);
            }
            window.location.reload();
        }
    })
}

function getToken() {
    let cName = 'Authorization' + '=';
    let cookieData = document.cookie;
    let cookie = cookieData.indexOf('Authorization');
    let auth = '';
    if (cookie !== -1) {
        cookie += cName.length;
        let end = cookieData.indexOf(';', cookie);
        if (end === -1) end = cookieData.length;
        auth = cookieData.substring(cookie, end);
    }

    // kakao 로그인 사용한 경우 Bearer 추가
    if (auth.indexOf('Bearer') === -1 && auth !== '') {
        auth = 'Bearer ' + auth;
    }

    return auth;
}