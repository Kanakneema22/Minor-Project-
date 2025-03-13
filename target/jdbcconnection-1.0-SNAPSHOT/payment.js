document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("paymentForm");

    form.addEventListener("submit", (e) => {
        e.preventDefault();
        alert("Payment Successful! Thank you for booking.");
        form.reset();
    });
});