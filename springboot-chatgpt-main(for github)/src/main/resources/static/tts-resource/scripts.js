document.addEventListener('DOMContentLoaded', function() {
    const playButton = document.getElementById('playButton');
    const reloadButton = document.getElementById('reloadButton');
    const voiceButton = document.getElementById('voiceButton');
    const voiceOptions = document.getElementById('voiceOptions');
    const textInput = document.getElementById('textInput');
    const speedInput = document.getElementById('speedInput');
    const sendButton = document.getElementById('sendButton');
    const audioContainer = document.getElementById('audioContainer');
    const progressBar = document.getElementById('progressBar').querySelector('div');
    const loadingMessage = document.getElementById('loadingMessage');
    const downloadButton = document.getElementById('downloadButton');

    let audio = new Audio(); // Audio will be dynamically loaded
    let selectedVoice = 'alloy'; // Default voice

    sendButton.addEventListener('click', function() {
        audio.currentTime = 0;
        progressBar.style.width = '0%';
        playButton.innerHTML = '&#9654;'; // Change to play icon
    });

    textInput.addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            generateAudio();
        }
    });

    playButton.addEventListener('click', function() {
        if (audio.paused) {
            audio.playbackRate = parseFloat(speedInput.value);
            audio.play();
            playButton.innerHTML = '&#10074;&#10074;'; // Change to pause icon
        } else {
            audio.pause();
            playButton.innerHTML = '&#9654;'; // Change to play icon
        }
    });

    reloadButton.addEventListener('click', function() {
        audio.currentTime = 0;
        audio.pause();
        audio.playbackRate = parseFloat(speedInput.value); // Reset the playback rate
        audio.play();
        playButton.innerHTML = '&#10074;&#10074;'; // Change to pause icon
    });

    audio.addEventListener('timeupdate', () => {
        const percent = (audio.currentTime / audio.duration) * 100;
        progressBar.style.width = percent + '%';
    });

    // Change playback position on progress bar click
    progressBar.parentElement.addEventListener('click', function(event) {
        const clickX = event.clientX - progressBar.parentElement.getBoundingClientRect().left;
        const percent = clickX / progressBar.parentElement.offsetWidth;
        audio.currentTime = audio.duration * percent;
    });

    // Change playback speed on dropdown change
    speedInput.addEventListener('change', function() {
        if (!audio.paused) {
            audio.playbackRate = parseFloat(speedInput.value);
        }
    });

    // Toggle voice options dropdown
    voiceButton.addEventListener('click', function() {
        voiceOptions.style.display = voiceOptions.style.display === 'block' ? 'none' : 'block';
    });

    // Change voice on option click
    voiceOptions.addEventListener('click', function(e) {
        if (e.target.tagName === 'A') {
            selectedVoice = e.target.dataset.voice;
            console.log('Selected voice:', selectedVoice);
            voiceButton.innerText = e.target.innerText; // Update button text to selected voice
            voiceOptions.style.display = 'none'; // Hide voice options dropdown
            audio.pause(); // Stop current audio
            audio.currentTime = 0; // Reset audio
            progressBar.style.width = '0%'; // Reset progress bar
            playButton.innerHTML = '&#9654;'; // Change to play icon
        }
    });

window.generateAudio = function() {
    var text = document.getElementById("textInput").value;

    var requestBody = {
        "input": text,
        "model": "tts-1",
        "voice": selectedVoice,
        "response_format": "mp3",
        "speed": parseFloat(speedInput.value)
    };

    loadingMessage.style.display = 'block'; // Show loading message

    fetch('/generateAudio', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
    .then(response => response.blob())
    .then(blob => {
        loadingMessage.style.display = 'none'; // Hide loading message
        var audioUrl = URL.createObjectURL(blob);
        audio.src = audioUrl;
        audio.style.display = "block";
        audio.play();

        // Show download button
        downloadButton.style.display = 'inline-block';
    })
    .catch(error => {
        loadingMessage.style.display = 'none'; // Hide loading message in case of error
        console.error('Error:', error);
    });
};


});
