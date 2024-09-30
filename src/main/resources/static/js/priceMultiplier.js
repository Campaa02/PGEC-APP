document.getElementById("event").addEventListener("change", updateTotalPrice);
document.getElementById("num").addEventListener("input", updateTotalPrice);
document.getElementById("type").addEventListener("change", updateTotalPrice);

function updateTotalPrice() {
    var selectedOption = document.getElementById("event").options[document.getElementById("event").selectedIndex];
    var selectedPrice = parseFloat(selectedOption.getAttribute("data-price"));
    var numInput = document.getElementById("num").value;
    var type = document.getElementById("type").value;
    var priceMultiplier = 1.0;


    if (type === 'VIP') {
        priceMultiplier = 1.5;
    } else if (type === 'PREMIUM') {
        priceMultiplier = 2.0;
    }

    var totalPrice = numInput > 0 ? (selectedPrice * parseFloat(numInput) * priceMultiplier).toFixed(2) + " $" : " Selecciona las entradas";
    document.getElementById("totalPrice").textContent = totalPrice;
}

updateTotalPrice();