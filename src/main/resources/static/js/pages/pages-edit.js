function setupDragAndDrop(dragAreaId, inputId, previewId, multiple = false, maxFiles = 1) {
    const dropArea = document.getElementById(dragAreaId);
    const fileInput = document.getElementById(inputId);
    const preview = document.getElementById(previewId);

    dropArea.addEventListener('click', () => fileInput.click());

    dropArea.addEventListener('dragover', (e) => {
        e.preventDefault();
        dropArea.classList.add('hover');
    });

    dropArea.addEventListener('dragleave', () => {
        dropArea.classList.remove('hover');
    });

    dropArea.addEventListener('drop', (e) => {
        e.preventDefault();
        dropArea.classList.remove('hover');
        const files = e.dataTransfer.files;
        handleFiles(files, preview, multiple, maxFiles);
    });

    fileInput.addEventListener('change', (e) => {
        const files = e.target.files;
        handleFiles(files, preview, multiple, maxFiles);
    });

    function handleFiles(files, preview, multiple, maxFiles) {
        if (files.length === 0) return;

        if (!multiple && files.length > maxFiles) {
            alert(`Можно загрузить только ${maxFiles} файл(а)`);
            return;
        }

        preview.innerHTML = '';

        for (let i = 0; i < Math.min(files.length, maxFiles); i++) {
            const file = files[i];
            if (!file.type.startsWith('image/')) {
                alert('Можно загружать только изображения!');
                return;
            }
            const reader = new FileReader();
            reader.onload = (e) => {
                const img = document.createElement('img');
                img.src = e.target.result;
                img.alt = 'Предпросмотр';
                preview.appendChild(img);
            };
            reader.readAsDataURL(file);
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    setupDragAndDrop('drag-area-main', 'file-main', 'preview-main');
    setupDragAndDrop('drag-area-additional', 'additionalFiles', 'preview-additional', true, 5);
});