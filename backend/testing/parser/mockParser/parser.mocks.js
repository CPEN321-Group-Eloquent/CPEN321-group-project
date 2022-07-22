// Mock parser.js and export.js (i.e. parse() from parser.js and unParsePresentation() from export.js)

const validParseText = "\\begin{presentation} \\title: hello \\end{presentation}";
const invalidParseText = "\\begin{presentation} \\title: hello";

const sampleUnparseInput = {
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
                "size": 4,
                "colour": 5,
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
    }]
}; 

const mockParse = jest.fn((req, res) => {
	return new Promise ((resolve, reject) => {
		var userID = req.body.userID;
		var text = req.body.text;
		setTimeout(() => {
			if (req.body.text === validParseText) resolve({title: "hello", cards: [], feedback: [], users: [{userID: req.body.userID, permission: "owner"}]});
			else if (req.body.text === invalidParseText) reject({err: "No \\end{presentation} token found after \\begin{presentation}."});
		}, 2000);
	});
	
}); 

const mockUnParsePresentation = jest.fn((req, res) => {
	var presObj = req.body;
	if (presObj == sampleUnparseInput) {
		return "\\begin{presentation}\n\t\\title this is the title\n\t\\begin{cuecard}[backgroundColor=1, transitionPhrase=transition phrase, endWithPause=0]\n\t\t\\begin{point}[backgroundColor=2, font=front font, style=front style, size=3, colour=4]\n\t\t\t\\item front message\n\t\t\\end{point}\n\t\t\\begin{details}[backgroundColor=5, font=back font, style=back style, size=6, colour=7]\n\t\t\t\\item back message\n\t\t\t\\item point 2\n\t\t\\end{details}\n\t\\end{cuecard}\n\t\\begin{cuecard}[backgroundColor=1, transitionPhrase=w, endWithPause=0]\n\t\t\\begin{point}[backgroundColor=2, font=d, style=d, size=4, colour=5]\n\t\t\t\\item\n\t\t\\end{point}\n\t\t\\begin{details}[backgroundColor=5, font=a, style=a, size=6, colour=7]\n\t\t\t\\item\n\t\t\\end{details}\n\t\\end{cuecard}\n\\end{presentation}";
	}

});

module.exports = {
	validParseText,
	invalidParseText,
	sampleUnparseInput,
	mockParse,
	mockUnParsePresentation
}
