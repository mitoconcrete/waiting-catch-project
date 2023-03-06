// let host = 'http://' + window.location.host;

function getEvents(responseDto) {
    return `<tr>
                <th scope="row">${responseDto.eventId}</th>
                <td>${responseDto.name}</td>
                <td id="startDate">${responseDto.eventStartDate}</td>
                <td id="endDate">${responseDto.eventEndDate}</td>
                <td class="blacklist-request-btn">
                    <div class="eventBtn">
                        <input onclick="openUpdateEvent('${responseDto.eventId}', '${responseDto.name}', 
                        '${responseDto.eventStartDate}', '${responseDto.eventEndDate}')" type="button" class="btn" value="수정">
                        <input onclick="deleteEvents(${responseDto.eventId})" type="button" class="btn" value="삭제">
                    </div>
                </td>
                <td><input onclick="openCouponBox(${responseDto.eventId})" type="button" class="btn" value="쿠폰"></td>
            </tr>`
}

function openUpdateEvent(eventId, name, eventStartDate, eventEndDate) {
    $('.updateEvent').empty();
    $('.createEvent').hide();

    let tempHtml = getUpdateEventBox(eventId, name, eventStartDate, eventEndDate);
    $('.updateEvent').append(tempHtml);
    $('.updateEvent').show();
}

function closeUpdateEvent() {
    $('.updateEvent').hide();
    $('.createEvent').show();
}

function getUpdateEventBox(eventId, name, eventStartDate, eventEndDate) {
    return `<div>
              <label>Event Name</label>
              <input type="text" id="updateEventName" placeholder="Event Name" value="${name}">
            </div>
            <div>
              <label>시작일자</label>
              <input type="datetime-local" id="updateEventStartDate" value="${eventStartDate}">
            </div>
            <div>
              <label>종료일자</label>
              <input type="datetime-local" id="updateEventEndDate" value="${eventEndDate}">
            </div>
            <div class="long-btn-group">
            <button type="button" class="btn long-btn" onclick="updateEvents(${eventId})">수정</button>
            <button type="button" class="btn long-btn" onclick="closeUpdateEvent()">닫기</button>
            </div>`
}

function openCouponBox(eventId) {
    $('.coupon').empty();
    $('#couponCreatorTable').empty()
    let tempHtml = getCouponBox(eventId);
    $('.coupon').append(tempHtml);
    getCouponCreators(eventId);
    $('.coupon').show();
    $('.couponCreator').show();
}

function closeCouponBox() {
    $('.coupon').hide();
    $('.couponCreator').hide();
    ;
}

function getCouponBox(eventId) {
    return `<div>
              <label>Coupon Name</label>
              <input type="text" id="couponName" placeholder="Coupon Name">
            </div>
            <div>
              <label>할인액(율)</label>
              <input type="number" id="discountPrice" placeholder="할인액(율)">
              <select name="discountType" id="discountType">
                <option>할인 종류를 선택하세요.</option>
                <option value="PRICE">PRICE</option>
                <option value="PERCENT">PERCENT</option>
              </select>
            </div>
            <div>
              <label>수량</label>
              <input type="number" id="quantity" placeholder="수량">
            </div>
            <div>
              <label>만료일</label>
              <input type="datetime-local" id="expireDate" placeholder="만료일">
            </div>
            <div class="long-btn-group">
            <button type="button" class="btn long-btn" onclick="createCouponCreator(${eventId})">생성</button>
            <button type="button" class="btn long-btn" onclick="closeCouponBox()">닫기</button>
            </div>`
}

function getCouponCreatorList(response, eventId) {
    return `<tr>
                <th scope="row">${response.couponCreatorId}</th>
                <td>${response.name}</td>
                <td>${response.discountPrice}</td>
                <td>${response.discountType}</td>
                <td>${response.quantity}</td>
                <td>${response.expireDate}</td>
                <td><button type="button" class="btn long-btn" onclick="openUpdateCoupon(
                    '${response.couponCreatorId}', '${response.name}', '${response.discountPrice}',
                     '${response.discountType}', '${response.quantity}', '${response.expireDate}', '${eventId}')">수정</button>
                </td>
            </tr>`
}

function getUpdateCouponBox(id, name, discountPrice, discountType, quantity, expireDate, eventId) {
    return `<div>
              <label>Coupon Name</label>
              <input type="text" id="updateCouponName" value="${name}" placeholder="Coupon Name">
            </div>
            <div>
              <label>할인액(율)</label>
              <input type="number" id="updateDiscountPrice" value="${discountPrice}" placeholder="할인액(율)">
              <select name="discountType" id="updateDiscountType">
                <option>할인 종류를 선택하세요.</option>
                <option value="PRICE">PRICE</option>
                <option value="PERCENT">PERCENT</option>
              </select>
            </div>
            <div>
              <label>수량</label>
              <input type="number" id="updateQuantity" value="${quantity}" placeholder="수량">
            </div>
            <div>
              <label>만료일</label>
              <input type="datetime-local" id="updateExpireDate" value="${expireDate}" placeholder="만료일">
            </div>
            <div class="long-btn-group">
            <button type="button" class="btn long-btn" onclick="updateCouponCreator('${id}', '${eventId}')">수정</button>
            <button type="button" class="btn long-btn" onclick="closeUpdateCoupon()">닫기</button>
            </div>`
}

function createCouponCreator(id) {
    let name = $('#couponName').val();
    let discountPrice = $('#discountPrice').val();
    let discountType = $('#discountType').val();
    let quantity = $('#quantity').val();
    let expireDate = $('#expireDate').val();
    $.ajax({
        type: 'POST',
        url: `/api/admin/events/` + id + `/creator`,
        contentType: "application/json",
        data: JSON.stringify({
            name: name,
            discountPrice: discountPrice,
            discountType: discountType,
            quantity: quantity,
            expireDate: expireDate
        }),
        success: function (data, status, response) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            alert("쿠폰 생성 완료")
            window.location.reload();
        },
        error: function (response, status, error) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            console.error(error);
        }
    })
}

function getCouponCreators(id) {

    $.ajax({
        type: 'GET',
        url: `/api/admin/events/` + id + `/creator`,
        success: function (data, status, response) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            if (status === 'success') {
                for (let i = 0; i < data.length; i++) {
                    let responseDto = data[i];
                    let eventId = id;
                    let tempHtml = getCouponCreatorList(responseDto, eventId);
                    $('#couponCreatorTable').append(tempHtml);
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
        }
    })
}

function updateCouponCreator(id, eventId) {
    let name = $('#updateCouponName').val();
    let discountPrice = $('#updateDiscountPrice').val();
    let discountType = $('#updateDiscountType').val();
    let quantity = $('#updateQuantity').val();
    let expireDate = $('#updateExpireDate').val();

    $.ajax({
        type: 'PUT',
        url: `/api/admin/events/` + eventId + `/creator/` + id,
        contentType: "application/json",
        data: JSON.stringify({
            name: name,
            discountPrice: discountPrice,
            discountType: discountType,
            quantity: quantity,
            expireDate: expireDate
        }),
        success: function (data, status, response) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            alert("쿠폰 생성자 수정 성공");
            window.location.reload();
        },
        error: function (response, status, error) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            console.error(error);
        }
    })
}

function openUpdateCoupon(id, name, discountPrice, discountType, quantity, expireDate, eventId) {
    $('.updateCoupon').empty();
    $('.coupon').hide();

    let tempHtml = getUpdateCouponBox(id, name, discountPrice, discountType, quantity, expireDate, eventId);
    $('.updateCoupon').append(tempHtml);
    $('.updateCoupon').show();
}

function closeUpdateCoupon() {
    $('.updateCoupon').hide();
    $('.coupon').show();
}

$(document).ready(function () {
    $('.updateEvent').hide();
    $('.coupon').hide();
    $('.couponCreator').hide();
    $('.updateCoupon').hide();

    $.ajax({
        type: 'GET',
        url: `/api/admin/events`,
        success: function (data, status, response) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            if (status === 'success') {
                for (let i = 0; i < data.length; i++) {
                    let responseDto = data[i];
                    let tempHtml = getEvents(responseDto);
                    $('#EventTable').append(tempHtml);
                }
            }
        },
        error: function (response, status, error) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            window.location.href = host + "/admin/templates/login";
        }
    });
});

function createEvents() {
    let eventName = $('#eventName').val();
    let eventStartDate = $('#eventStartDate').val();
    let eventEndDate = $('#eventEndDate').val();

    $.ajax({
        type: 'POST',
        url: `/api/admin/events`,
        contentType: "application/json",
        data: JSON.stringify({
            name: eventName,
            eventStartDate: eventStartDate,
            eventEndDate: eventEndDate
        }),
        success: function (data, status, response) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            alert("이벤트 생성 성공");
            window.location.reload();
        },
        error: function (response, status, error) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            console.error(error);
        }
    })
}

function updateEvents(eventId) {
    let name = $('#updateEventName').val();
    let eventStartDate = $('#updateEventStartDate').val();
    let eventEndDate = $('#updateEventEndDate').val();

    console.log(eventId);
    console.log(name);
    console.log(eventStartDate);
    console.log(eventEndDate);

    $.ajax({
        type: 'PUT',
        url: `/api/admin/events/` + eventId,
        contentType: "application/json",
        data: JSON.stringify({
            name: name,
            eventStartDate: eventStartDate,
            eventEndDate: eventEndDate
        }),
        success: function (data, status, response) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            alert("이벤트 수정 성공");
            window.location.reload();
        },
        error: function (response, status, error) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            console.error(error);
        }
    })

}

function deleteEvents(id) {
    $.ajax({
        type: 'DELETE',
        url: `/api/admin/events/` + id,

        success: function (data, status, response) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            alert("이벤트 삭제 성공");
            window.location.reload();
        },
        error: function (response, status, error) {
            if (response.getResponseHeader("Authorization")) {
                document.cookie =
                    'Authorization' + "=" + response.getResponseHeader("Authorization") + "; " +
                    "path=/; expires=" + (new Date().getTime() + 30 * 60000) + ";";
            }
            console.error(error);
        }
    })
}