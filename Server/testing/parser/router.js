var path = require("path");
var filesystem = require("fs");
var express = require("express");
var parser = require("../../parser/parser.js");
var unparser = require("../../parser/export.js");

var app = express();

const host = "localhost";
const port = 3000;
const clientApp = path.join(__dirname, "");
const client = path.join(__dirname, "/import_export.html");

app.use(express.urlencoded({ extended: true }));
app.use(express.text({ type: "text/plain"}));
app.use("/", express.static(clientApp));
app.use(express.json());

app.listen(3000, () => {
   console.log(`${new Date()}   App started. Server listening on ${host}:${port}, serving ${clientApp}`); 
});

app.get("/", (req, res) => {
    res.sendFile(client);
});

app.post("/importTest", (req, res) => {
    console.log(req.body);
    res.send(parser.parse(0, req.body));
});

app.post("/exportTest", (req, res) => {   
    console.log(req.body)
    res.send(unparser.unParsePresentation(req.body));
});
