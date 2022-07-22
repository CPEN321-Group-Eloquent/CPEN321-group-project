const express = require("express");

const db = require('./db/connection')
const router = require('./router/router')

const app = express();
const port = 8081;

app.use(express.urlencoded({ extended: true }))
app.use(express.json())
app.use(express.text({ type: "text/plain" }))
app.use('/api', router);

app.get("/", (req, res) => {
    res.send("Hello World");
})

var server = app.listen(8081, (req, res) => {
    var host = server.address().address;
    var port = server.address().port;
    console.log("running at http://%s:%s", host, port);
    console.log(server.address())
})
