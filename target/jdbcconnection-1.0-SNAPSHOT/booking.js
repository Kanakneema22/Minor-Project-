document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("carRentalForm");
    const summaryText = document.getElementById("summaryText");
    const proceedButton = document.getElementById("proceedToPayment");

    form.addEventListener("submit", (e) => {
        e.preventDefault();
       if (generateSummary()) {
           alert("Booking Confirmed! You can now proceed to payment.");
           proceedButton.style.display = "block";
       }
        form.reset();
    });

    proceedButton.addEventListener("click" , () => {
        window.location.href = "payment.html";
    });

    function generateSummary() {
        const name = document.getElementById("name").value;
        const email = document.getElementById("email").value;
        const phone = document.getElementById("phone").value;
        const pickupDate = document.getElementById("pickupDate").value;
        const dropoffDate = document.getElementById("dropoffDate").value;

        if (new Date(pickupDate) > new Date(dropoffDate)) {
            summaryText.textContent = "Error: Drop-off date must be after the pick-up date.";
            return false;
        }

        summaryText.innerHTML = `
            <strong>Name:</strong> ${name}<br>
            <strong>Email:</strong> ${email}<br>
            <strong>Phone:</strong> ${phone}<br>
            <strong>Pick-up Date:</strong> ${pickupDate}<br>
            <strong>Drop-off Date:</strong> ${dropoffDate}<br>
            `;
            return true;
    }
});