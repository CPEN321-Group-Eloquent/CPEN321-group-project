const Presentation = require('../models/presentations');
const userStore = require('../userStore/userStore');

//@TODO should this also have a title in the request body
createPres = (req, res) => {
    console.log("Create presentation request received!");
    if (!req.body.presObj) {
	var presID;
    var presTitle = "unnamed";
	if (req.body.title) presTitle = req.body.title;
        
	//@TODO check for userID
        Presentation.create({
            title: presTitle,
            cards: [],
            feedback: [],
            users: [{id: req.body.userID, permission: "owner"}]     
        }).then((data) => {
            presID = data._id;
	    return userStore.addPresToUser(req.body.userID, data._id);
        }).then((result) => { 
            return res.status(200).send( presID );
	}).catch((err) => {
	    console.log(err);
            return res.status(500).json({ err: err });
        })
    } else {
        Presentation.create(req.body.presObj).then((data) => {
	    presID = data._id;
	    return userStore.addPresToUser(req.body.userID, data._id);
	}).then((result) => {
            return res.status(200).send( presID );
        }).catch((err) => {
            return res.status(500).json({ err: err });
        })
    }
}

// expects presID and userID in query
getPres = (req, res) => {
    Presentation.find({
        "_id": req.query.presID,
        "users.id": req.query.userID
    }).then((pres) => {
        return res.status(200).json({ data: pres });
    }).catch((err) => {
        return res.status(500).json({ err: err });
    })
}

// Internal - for calls from login() of userStore.js, rather than
// for responding to requests from the frontend.
module.exports.getPresTitle = (presentationID, userID) => {
    console.log("presManager: getPresTitle: Retrieving title of presentation " + presentationID + " for user " + userID);
    Presentation.find({
        "_id": presentationID,
        "users.id": userID
    }).then((pres) => {
        return pres.title;
    }).catch((err) => {
        return err;
    })	
}

// Internal - for calls from parse() of parser.js, rather than 
// for responding to requests form the frontend.
storeImportedPres = (presObj, userID) => {
    console.log("presManager: storeImportedPresentation: Storing imported presentation for user " + userID);
    var presID;
    return new Promise ((resolve, reject) => {
        Presentation.create(presObj).then((data) => {
            presID = data._id;
            return userStore.addPresToUser(userID, data._id);
        }).then((result) => {
            resolve( presID );
        }).catch((err) => {
            reject( err );
        })
    });
}

checkPermission = (userID, presID, permission) => {
    console.log("presManager: checkPermission: Checking if user " + userID + " has " + permission + " permission for presentation " + presID );
    return new Promise(async (resolve,reject)=>{
        await Presentation.findById(presID).then((pres) => {
            for (var i = 0; i < pres.users.length; i++) {
                if (pres.users[i].id === userID && pres.users[i].permission === permission) {
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

// expects query and userID in query
search = (req, res) => {
    Presentation.find({
        "title": {
            "$regex": req.query.query,
            "$options": "i"
          },
        "users.id": req.query.userID
    }).then((pres) => {
        return res.status(200).json({ data: pres });
    }).catch((err) => {
        return res.status(500).json({ err: err });
    })
}

deletePres = (req, res) => {
    var deletedPres;
    checkPermission(req.body.userID, req.body.presID, "owner")
    .then((permissionToDelete) => {
        if (permissionToDelete) {
	    Presentation.findOneAndDelete({
                "_id": req.body.presID,
                "users.id": req.body.userID
            });
        } else {
            console.log("presManager: deletePres: User " + req.body.userID + " does not have adequate permission to delete presentation " + req.body.presID);
	    throw {err: "presManager: deletePres: User " + req.body.userID + " does not have adequate permission to delete presentation " + req.body.presID};
        }
    }).then((pres) => {
        deletedPres = pres;
        console.log("presManager: deletePres: Calling userStore.removePresFromUser( " + req.body.userID + " , " + req.body.presID + " )");
        return userStore.removePresFromUser(req.body.userID, req.body.presID);
    }).then((data) => {
        return res.status(200).json({ deletedDoc: deletedPres });
    }).catch((err) => {
        return res.status(500).json({ err: err });
    })
}


savePres = (req, res) => {
    const filter = {"_id": req.body.presID};
    const update = {
        "title": req.body.title,
        "cards": req.body.cards,
        "feedback": req.body.feedback,
        "users": req.body.users
    }
    Presentation.findOneAndUpdate(filter, update, {new: true})
        .then((pres) => {
            return res.status(200).json({data: pres});
        }).catch((err) => {
            return res.status(500).json({err: err});
        })
}

// expects userID in query
getAllPresOfUser = (req, res) => {
    Presentation.find({
        "users.id": req.query.userID
    }).then((data) => {
        var titleArr = [];
        for (var i = 0; i < data.length; i++) {
            titleArr.push({[data[i].title]: data[i]._id})
        }
        return res.status(200).json({data: titleArr});
    }).catch((err) => {
        return res.status(500).json({err: err});
    })
}

module.exports = {
    createPres        : createPres,
    storeImportedPres : storeImportedPres,
    getPres           : getPres,
    //getPresTitle      : getPresTitle,
    editPres          : editPres,
    search            : search,
    deletePres        : deletePres,
    savePres          : savePres,
    getAllPresOfUser  : getAllPresOfUser
}
