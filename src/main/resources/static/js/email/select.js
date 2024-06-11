(function () {
    var quill = new Quill('#message', {
        theme: 'snow',
        modules: {
            toolbar: '#snow-toolbar'
        }
    });

    window.submitForm = function (event) {
        event.preventDefault();
        var messageContent = quill.root.innerHTML;
        document.getElementById('hiddenMessage').value = messageContent;
        event.target.submit();
    }

    document.getElementById('applyTemplate').addEventListener('click', function () {
        var templateSelect = document.getElementById('template');
        var selectedOption = templateSelect.options[templateSelect.selectedIndex];
        var subject = selectedOption.getAttribute('data-subject');
        var content = selectedOption.getAttribute('data-content');
        if (subject && content) {
            document.getElementById('subject').value = subject;
            quill.root.innerHTML = content;
        }
    });
})();

    function toggleAll(source) {
    var checkboxes = document.getElementsByName('mailingUsers');
    for (var i = 0; i < checkboxes.length; i++) {
    checkboxes[i].checked = source.checked;
}
}

    function toggleSingle() {
    var selectAll = document.getElementById('selectAll');
    var checkboxes = document.getElementsByName('mailingUsers');
    var allChecked = true;
    for (var i = 0; i < checkboxes.length; i++) {
    if (!checkboxes[i].checked) {
    allChecked = false;
    break;
}
}
    selectAll.checked = allChecked;
}