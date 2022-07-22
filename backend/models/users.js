const mongoose = require('mongoose')
const Schema = mongoose.Schema

const User = new Schema(
    {
        userID: { type: String, required: true },
        username: { type: String, required: true },
        presentations: [{ type: Schema.ObjectId }]
    }
)

module.exports = mongoose.model('users', User)