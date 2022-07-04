function main() {
    var importButton = document.getElementById("importButton");
    var exportButton = document.getElementById("exportButton");
    
    console.log(importButton);
    console.log(exportButton);

    importButton.addEventListener("click", () => {
        alert("Import button pressed!");

        var xhr = new XMLHttpRequest();
        
    });
    
    exportButton.addEventListener("click", () => {
        alert("Export button pressed!");
    });
}

window.addEventListener("load", main);
