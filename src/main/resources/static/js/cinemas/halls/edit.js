const dropAreascheme = document.getElementById('drag-area-scheme');
const fileInputscheme = document.getElementById('scheme');
const previewscheme = document.getElementById('preview-scheme');

const dropAreatopBanner = document.getElementById('drag-area-topBanner');
const fileInputtopBanner = document.getElementById('topBanner');
const previewtopBanner = document.getElementById('preview-topBanner');

function addDragAndDropEvents(dropArea, fileInput, preview) {
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
        handleFiles(files, preview);
    });

    fileInput.addEventListener('change', (e) => {
        const files = e.target.files;
        handleFiles(files, preview);
    });
}

function handleFiles(files, preview) {
    if (files.length === 0) return;
    const file = files[0];
    if (!file.type.startsWith('image/')) {
        alert('Можно загружать только изображения!');
        return;
    }
    const reader = new FileReader();
    reader.onload = (e) => {
        preview.innerHTML = `<img src="${e.target.result}" alt="Предпросмотр">`;
    };
    reader.readAsDataURL(file);
}

addDragAndDropEvents(dropAreascheme, fileInputscheme, previewscheme);
addDragAndDropEvents(dropAreatopBanner, fileInputtopBanner, previewtopBanner);
