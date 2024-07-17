const mainDropArea = document.getElementById('drag-area');
const mainFileInput = document.getElementById('file');
const mainPreview = document.getElementById('preview');

mainDropArea.addEventListener('click', () => mainFileInput.click());

mainDropArea.addEventListener('dragover', (e) => {
    e.preventDefault();
    mainDropArea.classList.add('hover');
});

mainDropArea.addEventListener('dragleave', () => {
    mainDropArea.classList.remove('hover');
});

mainDropArea.addEventListener('drop', (e) => {
    e.preventDefault();
    mainDropArea.classList.remove('hover');
    const files = e.dataTransfer.files;
    handleMainFile(files);
});

mainFileInput.addEventListener('change', (e) => {
    const files = e.target.files;
    handleMainFile(files);
});

function handleMainFile(files) {
    if (files.length === 0) return;
    const file = files[0];
    if (!file.type.startsWith('image/')) {
        alert('Можно загружать только изображения!');
        return;
    }
    const reader = new FileReader();
    reader.onload = (e) => {
        mainPreview.innerHTML = `<img src="${e.target.result}" alt="Предпросмотр главного изображения">`;
    };
    reader.readAsDataURL(file);
}

const additionalDropArea = document.getElementById('additional-drag-area');
const additionalFileInput = document.getElementById('additionalFiles');
const additionalPreview = document.getElementById('additional-preview');

additionalDropArea.addEventListener('click', () => additionalFileInput.click());

additionalDropArea.addEventListener('dragover', (e) => {
    e.preventDefault();
    additionalDropArea.classList.add('hover');
});

additionalDropArea.addEventListener('dragleave', () => {
    additionalDropArea.classList.remove('hover');
});

additionalDropArea.addEventListener('drop', (e) => {
    e.preventDefault();
    additionalDropArea.classList.remove('hover');
    const files = e.dataTransfer.files;
    handleAdditionalFiles(files);
});

additionalFileInput.addEventListener('change', (e) => {
    const files = e.target.files;
    handleAdditionalFiles(files);
});

function handleAdditionalFiles(files) {
    additionalPreview.innerHTML = ''; // Очистить предыдущие превью
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
            img.alt = 'Предпросмотр дополнительного изображения';
            additionalPreview.appendChild(img);
        };
        reader.readAsDataURL(file);
    });
}



document.addEventListener('DOMContentLoaded', function () {
    const dateField = document.getElementById('date');
    const prePremiereCheckbox = document.getElementById('prePremiere');

    function checkDate() {
        const today = new Date().toISOString().split('T')[0];
        const selectedDate = dateField.value;

        if (selectedDate && selectedDate <= today) {
            prePremiereCheckbox.checked = false;
            prePremiereCheckbox.disabled = true;
        } else {
            prePremiereCheckbox.disabled = false;
        }
    }

    dateField.addEventListener('change', checkDate);

    // Check on page load
    checkDate();
});
