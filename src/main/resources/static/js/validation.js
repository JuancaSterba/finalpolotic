var expirationDateInput = document.getElementById("expirationDate");

expirationDateInput.addEventListener("change", function() {
  var selectedDate = new Date(expirationDateInput.value);
  var currentDate = new Date();

  if (selectedDate < currentDate) {
    alert("La fecha de vencimiento no puede ser anterior al día de hoy.");
    expirationDateInput.value = ""; // Limpiar el campo de fecha
  }
});
