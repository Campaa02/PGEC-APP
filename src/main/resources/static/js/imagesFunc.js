function previewImage(input) {
    console.log('Input:', input);
    const preview = document.getElementById('image-preview');
    const file = input.files[0];
    console.log('File:', file);
    const reader = new FileReader();

    reader.onload = function(e) {
        preview.src = e.target.result;
    };

    if (file) {
        reader.readAsDataURL(file);
    }
}