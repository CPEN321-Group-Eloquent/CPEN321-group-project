const userStore = jest.createMockFromModule('../userStore');

const addPresToUser = (userID, presID) => {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            //console.log("mock userStore: addPresToUser: userID = " + userID + ", presID = " + presID);
            if (userID === null) {
                reject("Unspecified user");
            } else if (userID === "Idontexist") {
                reject("User not found");
            } else {
                resolve("Presentation added to user!");
            }
        }, 100);
    });
}

const userExistsWithID = (userID) => {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            if (userID === "Idontexist" || userID === null) {
                reject(false);
            } else {
                resolve(true);
            }
        }, 100);
    });
}

// const removePresFromUser = (userID, presID) => {
//     console.log("userStore: removePresFromUser: Deleting presentation " + presID + " from user " + userID);
//     return new Promise((resolve, reject) => {
//         User.updateOne(
//             { userID },
//             {$pull: {presentations: presID}}
//         ).then((data) => {
//             console.log("userStore: removePresFromUser: Deleted presentation " + presID + " from user " + userID);
//             resolve(data);
//         }).catch((err) => {
//             console.log(err);
//             reject(err);
//         })
//     });
// }

const removePresFromUser = (userID, presID) => {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            if (presID === null && userID === "104866131128716891939") {
                reject("Unspecified presentation");
            }
        }, 100);
    });
}

userStore.addPresToUser = addPresToUser;
userStore.userExistsWithID = userExistsWithID;
// userStore.removePresFromUser = removePresFromUser;

module.exports = userStore;
