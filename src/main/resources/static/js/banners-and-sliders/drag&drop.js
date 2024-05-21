    const dragArea = document.getElementById('drag-area');
    const fileInput = document.getElementById('background');
    const preview = document.getElementById('preview');
    const uploadForm = document.getElementById('upload-form');

    dragArea.addEventListener('click', () => fileInput.click());

    fileInput.addEventListener('change', handleFiles);

    dragArea.addEventListener('dragover', (event) => {
    event.preventDefault();
    dragArea.classList.add('dragging');
});

    dragArea.addEventListener('dragleave', () => {
    dragArea.classList.remove('dragging');
});

    dragArea.addEventListener('drop', (event) => {
    event.preventDefault();
    dragArea.classList.remove('dragging');
    handleFiles(event);
});

    function handleFiles(event) {
        const files = event.target.files || event.dataTransfer.files;
        preview.innerHTML = ''; // Clear previous preview
        for (const file of files) {
            if (file.type.startsWith('image/')) {
                const reader = new FileReader();
                reader.onload = (e) => {
                    const img = document.createElement('img');
                    img.src = e.target.result;
                    const fileName = document.createElement('div');
                    fileName.className = 'file-name';
                    fileName.textContent = file.name;
                    preview.appendChild(img);
                    preview.appendChild(fileName);
                };
                reader.readAsDataURL(file);
            } else {
                alert('Можно загружать только изображения!');
            }
        }
        // Set the files to the file input element for form submission
        fileInput.files = files;
    }