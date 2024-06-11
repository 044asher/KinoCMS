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
    var form = event.target;

    var submitButton = form.querySelector('button[type="submit"]');
    submitButton.disabled = true;
    var progressBar = document.getElementById('progress-bar');
    progressBar.style.width = '0%';

    fetch(form.action, {
    method: 'POST',
    headers: {
    'Content-Type': 'application/x-www-form-urlencoded'
},
    body: new URLSearchParams(new FormData(form))
}).then(response => response.json()).then(data => {
    // Initial response handling if needed
}).catch(error => {
    console.error('Error:', error);
    submitButton.disabled = false;
});

    var progressInterval = setInterval(function () {
    fetch('/admin/email-sending/progress').then(response => response.json()).then(progress => {
    var emailsSent = progress.emailsSent;
    var totalUsers = progress.totalUsers;
    var percentage = Math.min(emailsSent / totalUsers * 100, 100);
    progressBar.style.width = percentage + '%';
    progressBar.textContent = Math.round(percentage) + '%';
    if (percentage === 100) {
    clearInterval(progressInterval);
    submitButton.disabled = false;
}
}).catch(error => {
    console.error('Error fetching progress:', error);
});
}, 1000);
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
