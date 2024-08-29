const switchAction = () => {
    $('.form-check-input').each(function() {
        $(this).on("change", function() {
            let inputId = $(this).attr('id');
            let apiUrl = getApiUrl(inputId);
            let inputSwitch = $(this);
            let grantAdminRights = $(this).is(':checked');
            // let token = $('meta[name="_csrf"]').attr('content');
            let csrfTokenCookie = getCookie('XSRF-TOKEN');
            $.ajax({
                url: apiUrl,
                type: 'POST',
                contentType : 'application/json; charset=utf-8',
                headers: {
                    'X-XSRF-TOKEN' : csrfTokenCookie
                },
                data: JSON.stringify(grantAdminRights)
            }).then((data) => {
                modifyRightsLabel(inputSwitch, inputId);
            }).fail(() => {
                alert('Wystąpił błąd podczas próby zmiany uprawnień');
                reverseSwitchStatus(inputSwitch, inputId); // switch uległ przestawieniu, nawet jeżeli zmiana uprawnień nie udała się
            })
        });
    })
}

const reverseSwitchStatus = (inputSwitch, inputId) => {
    if (inputSwitch.is(':checked')) {
        $(inputSwitch).prop('checked', false);
    } else {
        $(inputSwitch).prop('checked', true);
    }
}

const modifyRightsLabel = (inputSwitch, inputId) => {
    let newText = inputSwitch.is(':checked') ? 'Administrator' : 'Użytkownik';
    $(`[for=${inputId}]`).text(newText);
}

const getApiUrl = (inputId) => {
    let id = inputId.slice('flexSwitchCheckChecked'.length);
    return $(`#api-link-${id}`).val();
}

const deleteBtnAction = () => {
    $('.btn-sm').each(function () {
        $(this).click(function (e) {
            e.preventDefault();
            const btn = $(this);
            const deleteUrl = btn.val();
            // let token = $('meta[name="_csrf"]').attr('content');
            let csrfTokenCookie = getCookie('XSRF-TOKEN');
            $.ajax({
                type: 'POST',
                url: deleteUrl,
                contentType : 'application/json; charset=utf-8',
                headers: {
                    'X-XSRF-TOKEN' : csrfTokenCookie
                }
            }).then(() => {
                console.log('success');
                const btnId = btn.attr('id');
                removeTableRowByBtnId(btnId);
                updateRowNumbers();
            }).fail(() => {
                console.log('failed');
            });
        });
    });
}

const updateRowNumbers = () => {
    let index = 1;
    let list = $('.row-number');
    if (list.length > 0) {
        list.each(function (i, v) {
            $(v).text(index++);
        });
    } else {
        $('#admin-user-table').remove();
        $('#admins-users-header').text('Brak administratorów i użytkowników');
    }
}

const removeTableRowByBtnId = (btnId) => {
    let id = btnId.slice('api-delete-btn-'.length);
    $(`#table-row-${id}`).remove();
}

$(document).ready(function() {
    switchAction();
    deleteBtnAction();
});