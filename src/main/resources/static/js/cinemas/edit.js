const dropAreaLogo = document.getElementById('drag-area-logo');
const fileInputLogo = document.getElementById('logo');
const previewLogo = document.getElementById('preview-logo');

const dropAreaBanner = document.getElementById('drag-area-banner');
const fileInputBanner = document.getElementById('banner');
const previewBanner = document.getElementById('preview-banner');

dropAreaLogo.addEventListener('click', () => fileInputLogo.click());
dropAreaBanner.addEventListener('click', () => fileInputBanner.click());

dropAreaLogo.addEventListener('dragover', (e) => {
    e.preventDefault();
    dropAreaLogo.classList.add('hover');
});

dropAreaLogo.addEventListener('dragleave', () => {
    dropAreaLogo.classList.remove('hover');
});

dropAreaLogo.addEventListener('drop', (e) => {
    e.preventDefault();
    dropAreaLogo.classList.remove('hover');
    const files = e.dataTransfer.files;
    handleFiles(files, previewLogo);
});

fileInputLogo.addEventListener('change', (e) => {
    const files = e.target.files;
    handleFiles(files, previewLogo);
});

dropAreaBanner.addEventListener('dragover', (e) => {
    e.preventDefault();
    dropAreaBanner.classList.add('hover');
});

dropAreaBanner.addEventListener('dragleave', () => {
    dropAreaBanner.classList.remove('hover');
});

dropAreaBanner.addEventListener('drop', (e) => {
    e.preventDefault();
    dropAreaBanner.classList.remove('hover');
    const files = e.dataTransfer.files;
    handleFiles(files, previewBanner);
});

fileInputBanner.addEventListener('change', (e) => {
    const files = e.target.files;
    handleFiles(files, previewBanner);
});

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