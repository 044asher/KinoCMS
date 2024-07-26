const dropArea = document.getElementById('drag-area');
const fileInput = document.getElementById('additionalFiles');
const preview = document.getElementById('preview');
const form = document.getElementById('your-form-id'); // Добавьте id вашей формы
const hiddenInputsContainer = document.getElementById('hidden-inputs-container');

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

            // Добавить скрытые поля для отправки информации о файле на сервер
            const hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = 'existingFiles';
            hiddenInput.value = e.target.result; // Можно изменить для хранения информации о файле
            hiddenInputsContainer.appendChild(hiddenInput);
        };
        reader.readAsDataURL(file);
    });
}

// Дополнительно, если вы хотите обработать форму
form.addEventListener('submit', (e) => {
    // Здесь можно добавить логику перед отправкой формы
});
