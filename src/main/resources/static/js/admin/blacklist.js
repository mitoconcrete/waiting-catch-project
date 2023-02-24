let targetId;

// let host = 'http://' + window.location.host;

function getBlacklistRequest(responseDto) {
    return `<tr>
                <th scope="row">${responseDto.id}</th>
                <td>${responseDto.sellerName}</td>
                <td>${responseDto.customerName}</td>
                <td>${responseDto.description}</td>
                <td class="blacklist-request-btn">
                    <div class="eventBtn">
                        <input onclick="approveBlacklistRequest(${responseDto.id})" type="button" class="btn" value="승인">
                        <input onclick="rejectBlacklistRequest(${responseDto.id})" type="submit" class="btn" value="거절">
                    </div>
                </td>
            </tr>`
}

function getBlacklist(responseDto) {
    return `<tr>
                <th scope="row">${responseDto.blacklistId}</th>
                <td>${responseDto.restaurantName}</td>
                <td>${responseDto.customerName}</td>
                <td class="blacklist-request-btn">
                    <div class="eventBtn">
                        <input type="button" class="btn" value="해제">
                    </div>
                </td>
            </tr>`
}

function approveBlacklistRequest(blacklistRequestId) {
    const auth = getToken();
    console.log(blacklistRequestId);

    if (auth !== '') {
        $.ajax({
            type: 'POST',
            url: `/api/admin/restaurants/blacklist-demands/` + blacklistRequestId,
            data: {},
            success: function (response, status) {
                alert("승인 완료")
                window.location.reload();
            },
            error(error, response) {
                console.error(error);
                // window.location.href = host + "/admin/templates/login"
                window.location.reload();
            }
        });
    }
}

function rejectBlacklistRequest(blacklistRequestId) {
    const auth = getToken();

    if (auth !== '') {
        $.ajax({
            type: 'PUT',
            url: `/api/admin/restaurants/blacklist-demand/` + blacklistRequestId,
            data: {},
            success: function (response, status) {
                if (status === 'success') {
                    alert("요청 거절 완료")
                    window.location.reload();
                }
            },
            error(error, response) {
                console.error(error);
                window.location.reload();
            }
        })
    }
}

$(document).ready(function () {
    const auth = getToken();

    if (auth !== '') {
        $.ajax({
            type: 'GET',
            url: `/api/admin/restaurants/blacklist-demands`,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", auth);
            },
            success: function (response, status) {
                let responseData = response.data;
                console.log(responseData)
                if (status === 'success') {
                    for (let i = 0; i < responseData.length; i++) {
                        let responseDto = responseData[i];
                        let tempHtml = getBlacklistRequest(responseDto);
                        $('#blacklistDemand').append(tempHtml);
                    }
                }
            },
            error(error, response) {
                console.error(error);
                // window.location.href = host + "/admin/templates/login";
            }
        });

        $.ajax({
            type: 'GET',
            url: `/api/admin/blacklists`,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", auth);
            },
            success: function (response, status) {
                let responseData = response.data;
                console.log(responseData)
                if (status === 'success') {
                    for (let i = 0; i < responseData.length; i++) {
                        let responseDto = responseData[i];
                        let tempHtml = getBlacklist(responseDto);
                        $('#blacklist').append(tempHtml);
                    }
                }
            },
            error(error, response) {
                console.error(error);
                // window.location.href = host + "/admin/templates/login";
            }
        })
    }
});