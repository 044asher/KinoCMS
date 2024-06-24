const dropArea = document.getElementById('drag-area');
const fileInput = document.getElementById('additionalFiles');
const preview = document.getElementById('preview');

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
    handleFiles(files);
});

fileInput.addEventListener('change', (e) => {
    const files = e.target.files;
    handleFiles(files);
});

function handleFiles(files) {
    preview.innerHTML = ''; // Очистить предыдущие превью
    if (files.length === 0) return;
    const maxFiles = 5;
    const filesArray = Array.from(files).slice(0, maxFiles); // Ограничить до 5 файлов

    filesArray.forEach(file => {
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
    });
}
