function setupDragAndDrop(dragAreaId, inputId, previewId) {
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

setupDragAndDrop('drag-area-logo', 'logo', 'preview-logo');
setupDragAndDrop('drag-area-banner', 'banner', 'preview-banner');


function addHallFields() {
    var div = document.createElement('div');
    div.className = "border p-3 mb-3";
    div.innerHTML = `
                    <h5 class="mb-3">Новый зал:</h5>
                    <div class="mb-3">
                        <label for="hallNumber" class="form-label">Номер зала</label>
                        <input type="number" class="form-control" name="hallNumber" placeholder="Номер зала" required>
                    </div>
                    <div class="mb-3">
                        <label for="hallDescription" class="form-label">Описание</label>
                        <textarea class="form-control" name="hallDescription" placeholder="Описание" required></textarea>
                    </div>
                    <div class="mb-3">
                        <label for="hallScheme" class="form-label">Схема</label>
                        <input type="file" class="form-control" name="hallScheme" accept="image/*">
                    </div>
                    <div class="mb-3">
                        <label for="hallBanner" class="form-label">Баннер</label>
                        <input type="file" class="form-control" name="hallBanner" accept="image/*">
                    </div>
                    <div class="mb-3">
                        <label for="urlSeo" class="form-label">SEO URL</label>
                        <input type="text" class="form-control" name="urlSeo" placeholder="SEO URL" required>
                    </div>
                    <div class="mb-3">
                        <label for="titleSeo" class="form-label">SEO Заголовок</label>
                        <input type="text" class="form-control" name="titleSeo" placeholder="SEO Title" required>
                    </div>
                     <div class="mb-3">
                        <label for="keywordsSeo" class="form-label">SEO ключевые слова</label>
                        <input type="text" class="form-control" name="keywordsSeo" placeholder="SEO Keywords" required>
                    </div>
                    <div class="mb-3">
                        <label for="descriptionSeo" class="form-label">SEO описание</label>
                        <textarea class="form-control" name="descriptionSeo" placeholder="SEO Description" required></textarea>
                    </div>
                    <input type="hidden" name="hallTimeOfAdd" value="">



                `;
    document.getElementById('hallFieldsContainer').appendChild(div);
}
