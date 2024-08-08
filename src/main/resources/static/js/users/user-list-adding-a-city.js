$('#cityEditDialog').on('shown.bs.modal', function (event) {
    $.get({
        url: '/api/v1/city/all',
        success: (data) => {
            let cityList = $('#city-list');
            cityList.empty(); // Clear the list
            data.forEach(city => {
                $.get({
                    url: '/api/v1/city/' + city.id + '/user-count',
                    success: (userCount) => {
                        let deleteButton = userCount === 0 ? `<button class="btn btn-danger delete-city" data-id="${city.id}">Удалить</button>` : '';
                        cityList.append(`
                            <tr>
                                <td>${city.name}</td>
                                <td>
                                    <button class="btn btn-primary edit-city" data-id="${city.id}" data-name="${city.name}">Изменить</button>
                                    ${deleteButton}
                                </td>
                            </tr>
                        `);

                        $('.edit-city').click(function() {
                            let id = $(this).data('id');
                            let name = $(this).data('name');
                            $('#city-id').val(id);
                            $('#city-name').val(name);
                        });

                        $('.delete-city').click(function() {
                            let id = $(this).data('id');
                            if (confirm('Вы уверены что хотите удалить этот город?')) {
                                $.ajax({
                                    url: '/api/v1/city/' + id,
                                    type: 'DELETE',
                                    success: () => {
                                        location.reload();
                                    },
                                    error: (err) => {
                                        alert('Ошибка при удалении города: ' + err);
                                    }
                                });
                            }
                        });
                    },
                    error: (err) => {
                        alert('Error loading user count: ' + err);
                    }
                });
            });
        },
        error: (err) => {
            alert('Error loading cities: ' + err);
        }
    });
});

$('#save-city-button').click(function() {
    let city = {
        id: $('#city-id').val(),
        name: $('#city-name').val()
    };

    $.ajax({
        url: '/api/v1/city',
        type: 'POST',
        data: JSON.stringify(city),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: () => {
            location.reload();
        },
        error: (err) => {
            alert('При редактировании города оставлено пустое поле!');
        }
    });

    $('#cityEditDialog').modal('hide');
});