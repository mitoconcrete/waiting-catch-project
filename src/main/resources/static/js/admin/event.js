let host = 'http://' + window.location.host;

function getEvents(responseDto) {
    return `<tr>
                <th scope="row">${responseDto.id}</th>
                <td>${responseDto.name}</td>
                <td id="startDate">${responseDto.eventStartDate}</td>
                <td id="endDate">${responseDto.eventEndDate}</td>
                <td class="blacklist-request-btn">
                    <div class="eventBtn">
                        <input onclick="openUpdateEvent('${responseDto.id}', '${responseDto.name}', 
                        '${responseDto.eventStartDate}', '${responseDto.eventEndDate}')" type="button" class="btn" value="수정">
                        <input onclick="deleteEvents(${responseDto.id})" type="button" class="btn" value="삭제">
                    </div>
                </td>
                <td><input onclick="openCouponBox(${responseDto.id})" type="button" class="btn" value="쿠폰"></td>
            </tr>`
}

function openUpdateEvent(id, name, eventStartDate, eventEndDate) {
    $('.updateEvent').empty();
    $('.createEvent').hide();

    let tempHtml = getUpdateEventBox(id, name, eventStartDate, eventEndDate);
    $('.updateEvent').append(tempHtml);
    $('.updateEvent').show();
}

function closeUpdateEvent() {
    $('.updateEvent').hide();
    $('.createEvent').show();
}

function getUpdateEventBox(id, name, eventStartDate, eventEndDate) {
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
            <button type="button" class="btn long-btn" onclick="updateEvents(${id})">수정</button>
            <button type="button" class="btn long-btn" onclick="closeUpdateEvent()">닫기</button>
            </div>`
}

function openCouponBox(id) {
    $('.coupon').empty();
    $('#couponCreatorTable').empty()
    let tempHtml = getCouponBox(id);
    $('.coupon').append(tempHtml);
    getCouponCreators(id);
    $('.coupon').show();
    $('.couponCreator').show();
}

function closeCouponBox() {
    $('.coupon').hide();
    $('.couponCreator').hide();
    ;
}

function getCouponBox(id) {
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
            <button type="button" class="btn long-btn" onclick="createCouponCreator(${id})">생성</button>
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
    const auth = getToken();

    let name = $('#couponName').val();
    let discountPrice = $('#discountPrice').val();
    let discountType = $('#discountType').val();
    let quantity = $('#quantity').val();
    let expireDate = $('#expireDate').val();

    if (auth !== '') {
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
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", auth);
            },
            success: function (response) {
                alert("쿠폰 생성 완료")
                window.location.reload();
            },
            error(error, response) {
                console.error(error);
            }
        })
    }
}

function getCouponCreators(id) {
    const auth = getToken();

    if (auth !== '') {
        $.ajax({
            type: 'GET',
            url: `/api/admin/events/` + id + `/creator`,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", auth);
            },
            success: function (response, status) {
                console.log(response)
                if (status === 'success') {
                    for (let i = 0; i < response.length; i++) {
                        let responseDto = response[i];
                        let eventId = id;
                        let tempHtml = getCouponCreatorList(responseDto, eventId);
                        $('#couponCreatorTable').append(tempHtml);
                    }
                }
            },
            error(error, response) {
                console.error(error);
            }
        })
    }
}

function updateCouponCreator(id, eventId) {
    const auth = getToken();

    let name = $('#updateCouponName').val();
    let discountPrice = $('#updateDiscountPrice').val();
    let discountType = $('#updateDiscountType').val();
    let quantity = $('#updateQuantity').val();
    let expireDate = $('#updateExpireDate').val();

    if (auth !== '') {
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
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", auth);
            },
            success: function (response) {
                alert("쿠폰 생성자 수정 성공");
                window.location.reload();
            },
            error(error, response) {
                console.error(error);
            }
        })
    }
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
    const auth = getToken();
    $('.updateEvent').hide();
    $('.coupon').hide();
    $('.couponCreator').hide();
    $('.updateCoupon').hide();

    if (auth !== '') {
        $.ajax({
            type: 'GET',
            url: `/api/admin/events`,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", auth);
            },
            success: function (response, status) {
                console.log(response)
                if (status === 'success') {
                    for (let i = 0; i < response.length; i++) {
                        let responseDto = response[i];
                        let tempHtml = getEvents(responseDto);
                        $('#EventTable').append(tempHtml);
                    }
                }
            },
            error(error, response) {
                console.error(error);
                // window.location.href = host + "/admin/login-page";
            }
        });
    }
});

function createEvents() {
    const auth = getToken();

    let eventName = $('#eventName').val();
    let eventStartDate = $('#eventStartDate').val();
    let eventEndDate = $('#eventEndDate').val();

    if (auth !== '') {
        $.ajax({
            type: 'POST',
            url: `/api/admin/events`,
            contentType: "application/json",
            data: JSON.stringify({
                name: eventName,
                eventStartDate: eventStartDate,
                eventEndDate: eventEndDate
            }),
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", auth);
            },
            success: function (response) {
                alert("이벤트 생성 성공");
                window.location.reload();
            },
            error(error, response) {
                console.error(error);
                // window.location.href = host + "/admin/login-page";
            }
        })
    }
}

function updateEvents(id) {
    const auth = getToken();

    let name = $('#updateEventName').val();
    let eventStartDate = $('#updateEventStartDate').val();
    let eventEndDate = $('#updateEventEndDate').val();

    console.log(id);
    console.log(name);
    console.log(eventStartDate);
    console.log(eventEndDate);

    if (auth !== '') {
        $.ajax({
            type: 'PUT',
            url: `/api/admin/events/` + id,
            contentType: "application/json",
            data: JSON.stringify({
                name: name,
                eventStartDate: eventStartDate,
                eventEndDate: eventEndDate
            }),
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", auth);
            },
            success: function (response) {
                alert("이벤트 수정 성공");
                window.location.reload();
            },
            error(error, response) {
                console.error(error);
            }
        })
    }
}

function deleteEvents(id) {
    const auth = getToken();

    if (auth !== '') {
        $.ajax({
            type: 'DELETE',
            url: `/api/admin/events/` + id,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", auth);
            },
            success: function (response) {
                alert("이벤트 삭제 성공");
                window.location.reload();
            },
            error(error, response) {
                console.error(error);
            }
        })
    }
}

