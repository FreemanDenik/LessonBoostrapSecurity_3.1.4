/**
 * @param json
 * @param json.brand
 * @param json.isAdmin
 * @param json.rolesToString
 * @param json.rolesToList
 * @param elm
 * @param elm.email
 * @param elm.roles
 * @param object.modal
 * @param object.tab
 */
// Глобальная переменная админ или нет
let IsADMIN = false;
// let IdUSER = 0;
// Стартовая инициализация и вызов функции
$(async function () {

    let admin_link = $("a#admin_link");
    let user_link = $("a#user_link");
    let home = {
        button: $("#nav-home-tab"),
        tab: $("#nav-home"),
        invisible: function () {
            this.button.addClass('display-none');
        },
        visible: function () {
            this.button.removeClass('display-none');
        },
        active: function () {
            this.button.tab('show');
        }
    };
    let create = {
        button: $("#nav-create-tab"),
        tab: $("#nav-create"),
        invisible: function () {
            this.button.addClass('display-none');
        },
        visible: function () {
            this.button.removeClass('display-none');
        },
        active: function () {
            this.button.tab('show');
        }
    };
    let profile = {
        button: $("#nav-profile-tab"),
        tab: $("#nav-profile"),
        invisible: function () {
            this.button.addClass('display-none');
        },
        visible: function () {
            this.button.removeClass('display-none');
        },
        active: function () {
            this.button.tab('show');
        }
    };

    let result = await fetch("/api/startup");
    if (result.ok) {
        let json = await result.json();
        // Заполняем бренд, т.е. надпись вверхне левом углу
        const brand = $("#navbar-brand");
        brand.text(json.username + " with roles: " + json.rolesToString);
        // Заполняем там profile
        profile.tab.find("#table-body").append(
            $("<tr/>")
                .append($("<td/>", {text: json.id}))
                .append($("<td/>", {text: json.username}))
                .append($("<td/>", {text: json.email}))
                .append($("<td/>", {text: json.rolesToString}))
        )
        IsADMIN = json.isAdmin;
        if (IsADMIN === false)
            admin_link.addClass("display-none");
        // IdUSER = json.id;
        // Заполняем пустые select'ы на странице ролями
        const selects = $("select.roles");
        $.each(selects, function (num, elm) {
            let select = $(this);
            $.each(json.rolesToList, function (n, e) {
                select.append(
                    $("<option/>", {value: e.id, text: e.name})
                );
            })
        })
    }
    // Событие показа таба
    home.button.on('show.bs.tab', async function (event) {
        let tab = $(this).attr('data-bs-target');
        let url = $(this).attr('data-bs-url');

        await printTab(tab, url);
    });
    // Submit соьытие всех форм
    $("#edit_form, #delete_form, #create_form").on('submit', async function (event) {
        event.preventDefault();
        const formData = new FormData(event.target);
        let action = $(this).attr('action');
        let response = await fetch(action, {
            method: 'POST',
            'Content-Type': 'application/json;charset=utf-8',
            body: formData
        });
        if (response.ok) {
            await printTab("#nav-home", "/api/admin/users");
        }
        $("#editModal, #deleteModal").modal('hide');
        home.button.tab('show');
    });
    // События модальных окон
    $("#editModal, #deleteModal").on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget);

        let id = button.attr('data-bs-id');
        let username = button.attr('data-bs-username');
        let email = button.attr('data-bs-email');

        $(this).find('input#id').val(id);
        $(this).find('input#labelId').val(id);
        $(this).find('input#username').val(username);
        $(this).find('input#email').val(email);

    })

    // Стартовый вызов для отрисовки главного таба
    let get = {
        admin: async function (){
            //home.button.addClass("active");
            //home.tab.addClass("active show");
            home.button.tab('show');
            home.visible();
            create.visible();
            profile.visible();
            $("#user_link").removeClass('active');
            $("#admin_link").addClass('active');
            //await printTab("#nav-home", "/api/admin/users");
        },
        user: async function (){
            //profile.button.addClass("active");
            //profile.tab.addClass("active show");
            profile.button.tab('show');
            profile.visible();
            home.invisible();
            create.invisible();
            $("#user_link").addClass('active');
            $("#admin_link").removeClass('active');
        }
    }
    $("#admin_link").on('click', async function (){
        await get.admin();

    })
    $("#user_link").on('click', async function (){
        await get.user();
    })
    if (IsADMIN) {
       await get.admin();
    } else {
        await get.user();
    }
})

async function printTab(parent, url) {
    const navHome = $(parent);
    const tableBody = navHome.find("#table-body");
    tableBody.empty();
    let result = await fetch(url);
    let json = await result.json();
    $.each(json, function (num, elm) {
        let tr = $("<tr/>")
            .append($("<td/>", {text: elm.id}))
            .append($("<td/>", {text: elm.username}))
            .append($("<td/>", {text: elm.email}))
            .append($("<td/>", {text: elm.roles}))
            .append($("<td/>")
                .append($("<button/>", {
                    'type': 'button',
                    'text': 'edit',
                    'data-bs-id': elm.id,
                    'data-bs-username': elm.username,
                    'data-bs-email': elm.email,
                    'data-bs-toggle': 'modal',
                    'data-bs-target': '#editModal',
                    'class': 'btn btn-primary',
                    click: function () {
                        $('#editModal').modal('show')
                    }
                })))
            .append($("<td/>")
                .append($("<button/>", {
                    'type': 'button',
                    'text': 'delete',
                    'data-bs-id': elm.id,
                    'data-bs-username': elm.username,
                    'data-bs-email': elm.email,
                    'data-bs-toggle': 'modal',
                    'data-bs-target': '#deleteModal',
                    'class': 'btn btn-danger',
                    click: function () {
                        $('#deleteModal').modal('show')
                    }
                })));
        tableBody.append(tr);
    });
}
