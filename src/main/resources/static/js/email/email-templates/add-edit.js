(function () {
    var quill = new Quill('#content', {
        theme: 'snow',
        modules: {
            toolbar: '#snow-toolbar'
        }
    });

    window.submitForm = function (event) {
        event.preventDefault();
        var content = quill.root.innerHTML;
        document.getElementById('hiddenContent').value = content;
        event.target.submit();
    }
})();