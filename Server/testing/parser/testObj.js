const testObj = {
    "title": "this is the title",
    "cards": [{
        "backgroundColor": "red",
        "transitionPhrase": "transition phrase",
        "endWithPause": 0,
        "front": {
            "backgroundColor": "front colour",
            "content": {
                "font": "front font",
                "style": "front style",
                "size": "front size",
                "colour": "front colour",
                "message": "> front message"
            }
        },
        "back": {
            "backgroundColor": "d",
            "content": {
                "font": "back font",
                "style": "back style",
                "size": "back size",
                "colour": "back colour",
                "message": "> back message \n> point 2"
            }
        }
    },
    {
        "backgroundColor": "d",
        "transitionPhrase": "w",
        "endWithPause": 0,
        "front": {
            "backgroundColor": "front colour",
            "content": {
                "font": "d",
                "style": "d",
                "size": "d",
                "colour": "d",
                "message": "d"
            }
        },
        "back": {
            "backgroundColor": "a",
            "content": {
                "font": "a",
                "style": "a",
                "size": "a",
                "colour": "a",
                "message": "a"
            }
        }
    }]
};

module.exports = {testObj}