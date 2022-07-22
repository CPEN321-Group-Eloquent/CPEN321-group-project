const User = require('../models/users');
const presManager = require('../presManager/presManager')
const {OAuth2Client} = require('google-auth-library');

//const CLIENT_ID = "588466351198-96mgu43b4k81evnf387c5gpa2vc4d587.apps.googleusercontent.com";
const CLIENT_ID = "9332959347-o8lhle1t6p7oanp5rq08vosu7vct3as3.apps.googleusercontent.com"; // Aswin's Google OAuth2 web client id

//from https://developers.google.com/identity/sign-in/web/backend-auth
const client = new OAuth2Client(CLIENT_ID);
async function verify(token) {
  const ticket = await client.verifyIdToken({
      idToken: token,
      audience: CLIENT_ID,  // Specify the CLIENT_ID of the app that accesses the backend
      // Or, if multiple clients access the backend:
      //[CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3]
  });
  const payload = ticket.getPayload();
  const userid = payload['sub'];
  // If request specified a G Suite domain:
  // const domain = payload['hd'];
}


login = (req, res) => {
    verify(req.body.token).then(() => {
        User.findOne({userID: req.body.userID}, (err, data) => {
            if (err) {
                console.log(err);
                return res.status(500).json({ error: err });
            }
    
            // create user if it's not already in the DB, otherwise return user
            if (!data) {
                User.create({
                    userID: req.body.userID,
                    username: req.body.username,
                    presentations: []
                }).then((data) => {
                    return res.status(200).json({ userID: data.userID, username: data.username, presentations: data.presentations });
                })
            } else {
		var presentationTitles = [];
		for (let i = 0; i < data.presentations.length; i++) {
		    var title = presManager.getPresTitle(data.presentations[i], req.body.userID);
		    presentationTitles.push(title);
		}
                return res.status(200).json({ userID: data.userID, username: data.username, presentations: data.presentations, presentationTitles: presentationTitles });
            }
        })
    }).catch((error) => {
        console.log(error)
        return res.status(500).json({ error: error });
    })
}

// internal - called from presManager.js's createPres()
addPresToUser = (userId, presID) => {
    console.log("userStore: addPresToUser: Adding presentation " + presID + " to user " + userId);
    return new Promise((resolve, reject) => {
	    User.findOne(
        	{userID: userId},
    	).then((user) => {
		console.log(user.presentations);
		if (user.presentations.includes(presID) == false) {
			return User.findOneAndUpdate(
				{userID: userId},
				{$push: {presentations: presID}},
				{new: true}
			);
		} else {
			throw {err: "userStore: addPresToUser: Presentation " + presID  + " already included in user."};
		}
	}).then((data) => {
		console.log("userStore: addPresToUser: Added pres " + presID + " to user " + userId);
        	resolve(data);
    	}).catch((err) => {
		console.log(err);
        	reject(err);
    	})
    });

    //edit presentation to add user to array
}

// internal - called from presManager.js's deletePres()
removePresFromUser = (userID, presID) => {
    console.log("userStore: removePresFromUser: Deleting presentation " + presID + " from user " + userID);    
    return new Promise((resolve, reject) => {
        User.updateOne(
	    {userID: userID},
            {$pull: {presentations: presID}}
        ).then((data) => {
            console.log("userStore: removePresFromUser: Deleted presentation " + presID + " from user " + userID);
	    resolve(data);
        }).catch((err) => {
            console.log(err);
	    reject(err);
        })
    });
}

module.exports = {
    login: login,
    addPresToUser: addPresToUser,
    removePresFromUser: removePresFromUser
}
