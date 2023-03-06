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
        // beforeSend: function (xhr) {
        //     xhr.setRequestHeader("Authorization", auth);
        // },
        success: function (data, status, response) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            let responseData = data.data.content;
            let totalPage = data.data.totalPages;
            console.log(responseData)
            console.log(data.data);
            console.log(totalPage)
            if (status === 'success') {
                for (let i = 0; i < responseData.length; i++) {
                    let responseDto = responseData[i];
                    let tempHtml = getBlacklistRequest(responseDto);
                    $('#blacklistDemand').append(tempHtml);
                }
                for (let i = 0; i < totalPage; i++) {
                    let page = i + 1;
                    let tempHtml = getBlacklistRequestPagination(page);
                    $('#blacklistRequestPagination').append(tempHtml);
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
        // beforeSend: function (xhr) {
        //     xhr.setRequestHeader("Authorization", auth);
        // },
        success: function (data, status, response) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            let responseData = data.data.content;
            let totalPage = data.data.totalPages;
            console.log(responseData)
            console.log(data.data);
            console.log(totalPage);
            if (status === 'success') {
                for (let i = 0; i < responseData.length; i++) {
                    let responseDto = responseData[i];
                    let tempHtml = getBlacklist(responseDto);
                    $('#blacklist').append(tempHtml);
                }
                for (let i = 0; i < totalPage; i++) {
                    let page = i + 1;
                    let tempHtml = getBlacklistPagination(page);
                    $('#blacklistPagination').append(tempHtml);
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

function getBlacklistRequestPage(page) {
    $('#blacklistDemand').empty();
    $('#blacklistRequestPagination').empty();

    let pageNumber = page - 1;

    $.ajax({
        type: 'GET',
        url: `/api/admin/restaurants/blacklist-demands?page=` + pageNumber,
        success: function (data, status, response) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            let responseData = data.data.content;
            let totalPage = data.data.totalPages;
            console.log(responseData);
            console.log(data.data);
            console.log(totalPage);
            if (status === 'success') {
                for (let i = 0; i < responseData.length; i++) {
                    let responseDto = responseData[i];
                    let tempHtml = getBlacklistRequest(responseDto);
                    $('#blacklistDemand').append(tempHtml);
                }
                for (let i = 0; i < totalPage; i++) {
                    let page = i + 1;
                    let tempHtml = getBlacklistRequestPagination(page);
                    $('#blacklistRequestPagination').append(tempHtml);
                }
            }
        }
    })
}

function getBlacklistPage(page) {
    $('#blacklist').empty();
    $('#blacklistPagination').empty();

    let pageNumber = page - 1;

    $.ajax({
        type: 'GET',
        url: `api/admin/blacklists?page=` + pageNumber,
        success: function (data, status, response) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            let responseData = data.data.content;
            let totalpage = data.data.totalPages;
            console.log(responseData);
            console.log(data.data);
            console.log(totalpage);
            if (status === 'success') {
                for (let i = 0; i < responseData.length; i++) {
                    let responseDto = responseData[i];
                    let tempHtml = getBlacklist(responseDto);
                    $('#blacklist').append(tempHtml);
                }
                for (let i = 0; i < totalpage; i++) {
                    let page = i + 1;
                    let tempHtml = getBlacklistPagination(page);
                    $('#blacklistPagination').append(tempHtml);
                }
            }
        }
    })
}

function getBlacklistRequestPagination(page) {
    return `<li class="page-item"><a onclick="getBlacklistRequestPage(${page})" style="color: #8B60C7" class="page-link" href="#">${page}</a></li>`
}

function getBlacklistPagination(page) {
    return `<li class="page-item"><a onclick="getBlacklistPage(${page})" style="color: #8B60C7" class="page-link" href="#">${page}</a></li>`
}