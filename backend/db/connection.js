const mongoose = require('mongoose')

mongoose.connect('mongodb://localhost:27017/CPEN321', { useNewUrlParser: true })
    .then(() => {
        console.log("connected to DB");
    }).catch(e => {
        console.error('Connection error', e.message)
    })

const db = mongoose.connection

module.exports = db