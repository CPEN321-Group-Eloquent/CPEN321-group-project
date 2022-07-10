const Presentation = require('../models/presentations');

//@TODO should this also have a title in the request body
createPres = (req, res) => {
    if (!req.body.presObj) {
        //@TODO check for userID
        Presentation.create({
            title: "unnamed",
            cards: [],
            feedback: [],
            users: [{id: req.body.userID, permission: "owner"}]     
        }).then((data) => {
            return res.status(200).json({ data: data });
        }).catch((err) => {
            return res.status(500).json({ err: err });
        })
    } else {
        Presentation.create(req.body.presObj).then((data) => {
            return res.status(200).json({ data: data });
        }).catch((err) => {
            return res.status(500).json({ err: err });
        })
    }
}

getPres = (req, res) => {
    Presentation.find({
        "_id": req.body.presID,
        "users.id": req.body.userID
    }).then((pres) => {
        return res.status(200).json({ data: pres });
    }).catch((err) => {
        return res.status(500).json({ err: err });
    })
}

checkPermission = (userID, presID) => {
    return new Promise(async (resolve,reject)=>{
        await Presentation.findById(presID).then((pres) => {
            for (var i = 0; i < pres.users.length; i++) {
                if (pres.users[i].id == userID) {
                    resolve(true)
                    return
                }
            }
            resolve(false)
            return
        }).catch(() => {
            //@TODO throw error if presentation is not found?
            resolve(false)
            return
        })
    })
}

editPres = (req, res) => {
    //check permission and update in one
    Presentation.findOneAndUpdate({
        "_id": req.body.presID,
        "users.id": req.body.userID
    }, {[req.body.field]: req.body.content}, {new: true}).then((pres) => {
        return res.status(200).json({ data: pres });
    }).catch((err) => {
        return res.status(500).json({ err: err });
    })
}

search = (req, res) => {
    Presentation.find({
        "title": {
            "$regex": req.body.query,
            "$options": "i"
          },
        "users.id": req.body.userID
    }).then((pres) => {
        return res.status(200).json({ data: pres });
    }).catch((err) => {
        return res.status(500).json({ err: err });
    })
}

deletePres = (req, res) => {
    Presentation.findOneAndDelete({
        "_id": req.body.presID,
        "users.id": req.body.userID
    }).then((pres) => {
        return res.status(200).json({ deletedDoc: pres });
    }).catch((err) => {
        return res.status(500).json({ err: err });
    })
}

module.exports = {
    createPres,
    getPres,
    editPres,
    search,
    deletePres
}