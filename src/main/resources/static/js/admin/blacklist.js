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
    $.ajax({
        type: 'POST',
        url: `/api/admin/restaurants/blacklist-demands/` + blacklistRequestId,
        data: {},
        success: function (data, status, response) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            alert("승인 완료")
            window.location.reload();
        },
        error: function (response, status, error) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            window.location.reload();
        }
    });
}

function rejectBlacklistRequest(blacklistRequestId) {
    $.ajax({
        type: 'PUT',
        url: `/api/admin/restaurants/blacklist-demand/` + blacklistRequestId,
        data: {},
        success: function (data, status, response) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            if (status === 'success') {
                alert("요청 거절 완료")
                window.location.reload();
            }
        },
        error: function (response, status, error) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            window.location.reload();
        }
    })
}

$(document).ready(function () {
    $.ajax({
        type: 'GET',
        url: `/api/admin/restaurants/blacklist-demands`,
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", auth);
        },
        success: function (data, status, response) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            let responseData = data.data;
            console.log(responseData)
            if (status === 'success') {
                for (let i = 0; i < responseData.length; i++) {
                    let responseDto = responseData[i];
                    let tempHtml = getBlacklistRequest(responseDto);
                    $('#blacklistDemand').append(tempHtml);
                }
            }
        },
        error: function (response, status, error) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
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
        success: function (data, status, response) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            let responseData = data.data;
            console.log(responseData)
            if (status === 'success') {
                for (let i = 0; i < responseData.length; i++) {
                    let responseDto = responseData[i];
                    let tempHtml = getBlacklist(responseDto);
                    $('#blacklist').append(tempHtml);
                }
            }
        },
        error: function (response, status, error) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            console.error(error);
            // window.location.href = host + "/admin/templates/login";
        }
    })
});