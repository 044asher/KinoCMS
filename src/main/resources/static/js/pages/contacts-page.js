function changeStatusContact(id) {
    fetch('/KinoCMS-Nizar/admin/pages/contacts/' + id + '/change-status', {
        method: 'POST'
    }).then(function(response) {
        if (response.ok) {
            var button = document.getElementById('contacts-status-button-' + id);
            if (button.classList.contains('btn-success')) {
                button.classList.remove('btn-success');
                button.classList.add('btn-warning');
                button.textContent = 'ВЫКЛ';
            } else {
                button.classList.remove('btn-warning');
                button.classList.add('btn-success');
                button.textContent = 'ВКЛ';
            }
        } else {
            console.error('Failed to change status');
        }
    });
}