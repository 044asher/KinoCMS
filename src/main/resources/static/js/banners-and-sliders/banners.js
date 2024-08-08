document.addEventListener('DOMContentLoaded', function () {
    const imageFile = document.getElementById('imageFile');
    const imageUrl = document.getElementById('imageUrl');
    const newsImageFile = document.getElementById('newsImageFile');
    const newsImageUrl = document.getElementById('newsImageUrl');

    function toggleInputs(fileInput, urlInput) {
        fileInput.addEventListener('change', function () {
            if (fileInput.files.length > 0) {
                urlInput.disabled = true;
            } else {
                urlInput.disabled = false;
            }
        });

        urlInput.addEventListener('input', function () {
            if (urlInput.value.trim() !== '') {
                fileInput.disabled = true;
            } else {
                fileInput.disabled = false;
            }
        });
    }

    toggleInputs(imageFile, imageUrl);
    toggleInputs(newsImageFile, newsImageUrl);
});