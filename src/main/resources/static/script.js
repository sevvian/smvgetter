document.addEventListener('DOMContentLoaded', () => {
    const urlInput = document.getElementById('urlInput');
    const extractBtn = document.getElementById('extractBtn');
    const resultDiv = document.getElementById('result');

    const handleExtract = async () => {
        const url = urlInput.value.trim();
        if (!url) {
            resultDiv.classList.add('error');
            resultDiv.textContent = 'Please enter a URL.';
            return;
        }

        // UI feedback for loading state
        extractBtn.disabled = true;
        extractBtn.textContent = 'Extracting...';
        resultDiv.classList.remove('error');
        resultDiv.textContent = 'Contacting server, this may take a moment...';

        try {
            const response = await fetch(`/extract?url=${encodeURIComponent(url)}`);
            
            const contentType = response.headers.get("content-type");
            
            // Handle successful JSON responses
            if (response.ok && contentType && contentType.includes("application/json")) {
                const data = await response.json();
                resultDiv.textContent = JSON.stringify(data, null, 2);
            } else {
                // Handle all other responses (like errors from the backend)
                const textData = await response.text();
                resultDiv.classList.add('error');
                resultDiv.textContent = `Error (${response.status} ${response.statusText}):\n\n${textData}`;
            }
        } catch (error) {
            // Handle network errors or other unexpected issues
            resultDiv.classList.add('error');
            resultDiv.textContent = `A network error occurred. The server might be down or unreachable.\n\n${error.message}`;
            console.error("Extraction failed:", error);
        } finally {
            // Always re-enable the button
            extractBtn.disabled = false;
            extractBtn.textContent = 'Extract Links';
        }
    };

    extractBtn.addEventListener('click', handleExtract);
    
    // Allow pressing Enter in the input field to trigger extraction
    urlInput.addEventListener('keypress', (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            handleExtract();
        }
    });
});