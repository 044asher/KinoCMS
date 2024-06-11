const dropArea = document.getElementById('drag-area');
const fileInput = document.getElementById('file');
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