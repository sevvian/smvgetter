document.addEventListener('DOMContentLoaded', () => {
    const urlInput = document.getElementById('urlInput');
    const extractBtn = document.getElementById('extractBtn');
    const resultDiv = document.getElementById('result');

    extractBtn.addEventListener('click', async () => {
        const url = urlInput.value.trim();
        if (!url) {
            resultDiv.textContent = 'Please enter a URL.';
            return;
        }

        resultDiv.textContent = 'Extracting... Please wait.';
        
        try {
            const response = await fetch(`/extract?url=${encodeURIComponent(url)}`);
            const data = await response.json();

            if (response.ok) {
                resultDiv.textContent = JSON.stringify(data, null, 2);
            } else {
                resultDiv.textContent = `Error: ${data.message || response.statusText}`;
            }
        } catch (error) {
            resultDiv.textContent = `An error occurred: ${error.message}`;
        }
    });
});