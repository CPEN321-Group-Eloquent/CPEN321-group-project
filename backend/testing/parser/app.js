function main() {
    var importForm = document.getElementsByClassName("fileImport")[0];
    var exportButton = document.getElementById("exportButton");
    
    console.log(importForm);
    console.log(exportButton);

    importForm.addEventListener("submit", (e) => {
        //alert("Import button pressed!");
        e.preventDefault();
        var file = e.target.importFile.files[0];

        var fileReader = new FileReader();

        fileReader.addEventListener("load", (e) => {
            console.log(fileReader.result);
            fetch("/importTest", {
                method: "POST",
                body: fileReader.result,
                headers: {
                    "Content-type": "text/plain"
                }
            })
            .then(resp => resp.json())
            .then(data => console.log(data));      
        });

        fileReader.readAsText(file);
    });
    
    exportButton.addEventListener("click", () => {
        alert("Export button pressed!");
    });
}

window.addEventListener("load", main);
