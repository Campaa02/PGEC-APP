function formatDescription() {
    var descriptionContent = document.getElementById("description").innerHTML;
    document.getElementById("description-input").value = descriptionContent;
}

function resetTextColor() {
    document.execCommand('foreColor', false, '#212529');
}
