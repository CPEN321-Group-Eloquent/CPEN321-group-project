const mongoose = require('mongoose')
const Schema = mongoose.Schema

const Presentation = new Schema(
    {
        title: { type: String, required: true },
        cards: [{
            backgroundColor: { type: Number, required: true },
            transitionPhrase: { type: String, required: true },
            endWithPause: { type: Number, required: true },
            front: {
                backgroundColor: { type: Number, required: true },
                content: {
                    font: { type: String, required: true },
                    style: { type: String, required: true },
                    size: { type: Number, required: true },
                    colour: { type: Number, required: true },
                    message: { type: String, required: true },
                }
            },
            back: {
                backgroundColor: { type: Number, required: true },
                content: {
                    font: { type: String, required: true },
                    style: { type: String, required: true },
                    size: { type: Number, required: true },
                    colour: { type: Number, required: true },
                    message: { type: String, required: true },
                }
            }
        }],
        feedback: [{ 
            date: { type: String, required: true },
            duration: { type: Number, required: true },
            pauses: [{
                start: { type: Number, required: true },
                end: { type: Number, required: true }
            }],
            pacing: [{
                cueCardStartTime: { type: Number, required: true },
                cueCardEndTime: { type: Number, required: true }
            }]
        }],
        users: [{
            id: { type: String, required: true },
            permission: { type: String, required: true }
        }]
    }
)
Presentation.index({title: 1});
module.exports = mongoose.model('presentations', Presentation)




