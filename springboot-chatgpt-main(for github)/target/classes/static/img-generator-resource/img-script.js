function updateOptions() {
    var model = $("#model").val();
    var sizeSelect = $("#size");

    sizeSelect.empty(); // Clear previous options

    if (model === "dall-e-2") {
        $("#prompt").attr("maxlength", 1000).attr("placeholder", "Enter your prompt (maximum 1000 characters)");
        sizeSelect.append('<option value="256x256">256x256</option>');
        sizeSelect.append('<option value="512x512">512x512</option>');
        sizeSelect.append('<option value="1024x1024">1024x1024</option>');
    } else if (model === "dall-e-3") {
        $("#prompt").attr("maxlength", 4000).attr("placeholder", "Enter your prompt (maximum 4000 characters)");
        sizeSelect.append('<option value="1024x1024">1024x1024</option>');
        sizeSelect.append('<option value="1792x1024">1792x1024</option>');
        sizeSelect.append('<option value="1024x1792">1024x1792</option>');
    }
}

function generateImage() {
    var prompt = $("#prompt").val();
    var model = $("#model").val();
    var size = $("#size").val();
    var style = $("#style").val();

    var requestBody = {
        "prompt": prompt,
        "model": model,
        "size": size,
        "style": style
    };

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/generateImage",
        contentType: "application/json",
        data: JSON.stringify(requestBody),
        success: function(response) {
            $("#imageContainer").html(`<img src="${response}" alt="Generated Image" class="rounded" style="max-width: 500px; max-height: 430px;" >`);
        },
        error: function(xhr, status, error) {
            console.error("Error:", error);
        }
    });
}

// Initialize options
$(document).ready(function() {
    updateOptions();
});
