document.getElementById('transcriptionForm').addEventListener('submit', async (event) => {
            event.preventDefault();

            const formData = new FormData(event.target);
            try {
                const response = await fetch('/transcribe', {
                    method: 'POST',
                    body: formData
                });
                if (response.ok) {
                    const data = await response.json();
                    document.getElementById('transcriptionResult').innerText = data.text;
                } else {
                    document.getElementById('transcriptionResult').innerText = 'Transcription failed. Please try again.';
                }
            } catch (error) {
                document.getElementById('transcriptionResult').innerText = 'An error occurred. Please try again.';
                console.error('Error:', error);
            }
        });