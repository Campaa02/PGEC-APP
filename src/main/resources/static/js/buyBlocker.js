var buyButtons = document.querySelectorAll('.buyButton');

buyButtons.forEach((buyButton) => {
    buyButton.addEventListener('click', (event) => {
        console.log("click en botón de compra"); // Cambiado a console.log
        event.preventDefault();


        alert('PGEC dice: Para comprar entradas debes iniciar sesión.');
        setTimeout(() => {
            window.location.href = "/login";
        }, 1000);

    });
});
