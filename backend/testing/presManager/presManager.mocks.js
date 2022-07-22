const presManager = require("../../presManager/presManager");

// mocking presManager.js


const validPresObj = {
    "title": "this is the title",
    "cards": [{
        "backgroundColor": 1,
        "transitionPhrase": "transition phrase",
        "endWithPause": 0,
        "front": {
            "backgroundColor": 2,
            "content": {
                "font": "front font",
                "style": "front style",
                "size": 3,
                "colour": 4,
                "message": "> front message"
            }
        },
        "back": {
            "backgroundColor": 5,
            "content": {
                "font": "back font",
                "style": "back style",
                "size": 6,
                "colour": 7,
                "message": "> back message \n> point 2"
            }
        }
    },
    {
        "backgroundColor": 1,
        "transitionPhrase": "w",
        "endWithPause": 0,
        "front": {
            "backgroundColor": 2,
            "content": {
                "font": "d",
                "style": "d",
                "size": 3,
                "colour": 4,
                "message": "d"
            }
        },
        "back": {
            "backgroundColor": 5,
            "content": {
                "font": "a",
                "style": "a",
                "size": 6,
                "colour": 7,
                "message": "a"
            }
        }
    }],
    "feedback": [{
        "date": "today",
	"duration": 1,
	"pauses": [{
            "start": 0,
	    "end": 2
	}],
	"pacing": [{
	    "cueCardStartTime": 0,
	    "cueCardEndTime": 2
	}]
    }],
    "users": [{
	"id": "this guy", 
	"permission": "owner"
    }]
};

const mockStoreImportedPres = jest.fn((presObj) => {
	console.log(validPresObj);
//	return new Promise ((resolve, reject) => {
//		switch (presObj) {
//			case validObject: 
//		}
//	});
});

module.exports = {
	mockStoreImportedPres,
	validPresObj
}
