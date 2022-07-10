//var presentationManager = require("PresManager");

var colors = {
    "black":        0x000000,
    "white":        0xffffff,
    "red":          0xff0000,    
    "green":        0x00ff00,
    "blue":         0x0000ff,
    "silver":       0xc0c0c0,
    "gray":         0x808080,
    "maroon":       0x800000,
    "olive":        0x808000,
    "lime":         0x00ff00,
    "aqua":         0x00ffff,
    "teal":         0x008080,
    "navy":         0x000080,
    "fuschia":      0xff00ff,
    "purple":       0x800080,
    "indianred":    0xcd5c5c,
    "salmon":       0xfa8072,
    "darksalmon":   0xe9967a,
    "lightsalmon":  0xffa07a
}



function Presentation() {
    this.title = "unnamed";
    this.cards = [];
    this.feedback = [];
    this.users = [];
}

function Cuecard() {
    this.backgroundColor = colors["white"]; // white by default
    this.transitionPhrase = ""; // If empty transition phrase, default to manual cuecard transition
    this.endpause = true;
    this.front = {
        backgroundColor: colors["white"], 
        content: {
            // TODO add font, size, and style attributes
            color: colors["black"], // black by default
            message: ""
        }
    };
    this.back = null; 
}

function CuecardBack() {
    this.backgroundColor = colors["white"];
    this.content = [];
}



Presentation.prototype.addCard = function(card) {
    this.cards.push(card);
}

Presentation.prototype.addUser = function(userID, permission) {
    this.users.push({id: userID, permission: permission});
}


CuecardBack.prototype.addContent = function(font, style, size, color, message) {
    var c = colors["black"];
    if (color) {
        c = colors[color];
    }
    // TODO add support for font, style, and size attributes
    this.content.push({
        color: c,
        message: message
    });
}


/* Creates and stores away a presentation object, given its text representation, 
 * and a user ID. Returns true if parsing and storing are successful. Otherwise, 
 * throws an error.
 *
 * Number userID: 
 *      ID of user who owns the presentation to be parsed.
 * String text: 
 *      Text representation of presentation (e.g. contents of 
 *      sampleInputText.txt).
 */ 
function parse(userID, text) {
    // strip comments, whitespace at start of lines, and newlines
    text = parsePreProcessing(text);
    console.log(text);
    
    // tokenize text with delimiter "\"
    var tokens = text.split("\\");
    for (let i = 0; i < tokens.length; i++) {
        tokens[i] = tokens[i].trim();
    } 
    console.log(tokens);

    var keywords = {begin:"begin", end:"end", title:"title", point:"point", item:"item"};
    var contexts = {presentation:false, cuecard:false, details:false};
    var p = new Presentation();
    var c = null; // Cuecard

    for (let i = 0; i < tokens.length; i++) {
        var tokenNoWhitespace = tokens[i].replace(/\s/g, "");
        var attributesStartIndex = tokenNoWhitespace.indexOf("[");
        var attributesEndIndex = tokenNoWhitespace.indexOf("]");
        if (contexts["presentation"] == true) {
            if (contexts["cuecard"] == true) {
                if (contexts["details"] == true) {
                    if (tokenNoWhitespace === keywords["end"] + "{details}") {
                        contexts["details"] = false;
                    } else if (i == tokens.length - 3) {
                        throw {err: "No \\end{details} token found after \\begin{details}."};
                    } else {
                        var tokenNoKeyword = tokens[i].slice("item".length - tokens[i].length);
                        if (tokenNoKeyword[0] === "[" && tokenNoKeyword.slice(1, tokenNoKeyword.indexOf("=")) === "color") { 
                            var color = tokenNoKeyword.slice(tokenNoKeyword.indexOf("=") + 1, tokenNoKeyword.indexOf("]"));
                            if (color in colors) c.back.addContent(null, null, null, color, tokenNoKeyword.slice(tokenNoKeyword.indexOf("]") + 1, tokenNoKeyword.length))
                        } else {
                            c.back.addContent(null, null, null, null, tokenNoKeyword);
                        }
                        console.log(c.back.content);
                    }
                } else {
                    if (tokenNoWhitespace.slice(0, attributesStartIndex) === keywords["begin"] + "{details}"
                        || tokenNoWhitespace === keywords["begin"] + "{details}") {
                        contexts["details"] = true;
                    } else if (tokenNoWhitespace.slice(0, "point".length) === "point") {
                        var tokenNoKeyword = tokens[i].slice("point".length - tokens[i].length);
                        c.front.content["message"] = tokenNoKeyword;
                    }
                }

                if (tokenNoWhitespace === keywords["end"] + "{cuecard}") {
                    contexts["cuecard"] = false;
                    c.transitionPhrase = c.back.content[c.back.content.length - 1]["message"]
                    p.addCard(c);
                    
                } else if (i == tokens.length - 2) {
                    throw {err: "No \\end{cuecard} token found after \\begin{cuecard}."};
                }
            } else {
                if (tokenNoWhitespace.slice(0, attributesStartIndex) === keywords["begin"] + "{cuecard}"
                    || tokenNoWhitespace === keywords["begin"] + "{cuecard}") {
                    contexts["cuecard"] = true;
                    c = new Cuecard();
                    c.back = new CuecardBack();
                    
                    if (attributesStartIndex != -1 && attributesEndIndex > attributesStartIndex) {
                        var attributes = tokenNoWhitespace.slice(attributesStartIndex + 1, attributesEndIndex).split(",");
                        for (let j = 0; j < attributes.length; j++) {
                            attributes[j].trim();
                            var attributeKey = attributes[j].slice(0, attributes[j].indexOf("="));
                            var attributeValue = attributes[j].slice(attributes[j].indexOf("=") + 1, attributes[j].length);
                            
                            if (attributeKey === "color" && attributeValue in colors) c.backgroundColor = colors[attributeValue];
                            else if (attributeKey === "endpause" && attributeValue === "false") c.endpause = false;
                        }
                    }
                } else if (tokens[i].slice(0, "title".length) === "title") {
                    p.title = tokens[i].slice("title".length - tokens[i].length);
                }
            }

            if (tokenNoWhitespace === keywords["end"] + "{presentation}") {
                contexts["presentation"] = false;
            } else if (i == tokens.length - 1) {
                throw {err: "No \\end{presentation} token found after \\begin{presentation}."};
            }
        } else {
            if (tokenNoWhitespace === keywords["begin"] + "{presentation}") {
                contexts["presentation"] = true;
            }
        }
        console.log(contexts);
    }

    p.addUser(userID, "owner");

    console.log(JSON.stringify(p));
    return JSON.stringify(p);
}

/* Strip comments (all characters on a line, after a "%"), tabs, and newlines
 * from text representation of a presentation.
 */
function parsePreProcessing(text) {
    
    function stripCommentsIn(str) {
        var leanText = "";
        
        // strip comments
        var comment = false;
    
        for (let i = 0; i < str.length; i++) {
            if (!comment) {
                if (str[i] != "%") {
                    leanText = leanText + str[i];
                } else {
                    comment = true;
                }
            } else {
                if (str[i] == "\n") {
                    comment = false;
                }
            }
        }
    
        return leanText;
    }
    
    function ltrimLinesAndStripNewlinesIn(str) {
        var leanText = "";
    
        // https://stackoverflow.com/questions/24282158/how-to-remove-the-white-space-at-the-start-of-the-string
        var ltrim = (line) => (line.replace(/^\s+/g, ''));
        
        var lines = str.split(/\r?\n/);
        for (let i = 0; i < lines.length; i++) {
            leanText = leanText + ltrim(lines[i]);
        }
    
        return leanText;
    }

    text = stripCommentsIn(text);
    text = ltrimLinesAndStripNewlinesIn(text);    

    return text;
}

/* Returns a text representation string of the presentation object identified by 
 * userID, and presID. See the contents of SampleInputText.txt for an example of 
 * a presentation's text representation. Throws an error if unable to retrieve 
 * the requested presentation object.
 *
 * Number userID:
 *      ID of user with owner or collaborator access to the requested 
 *      presentation.
 * Number presID:
 *      ID of presentation whose text representation has been requested.
 */
function textify(userID, presID) {
    // retrieve presentation object using userId and presId
    // add presentation tag
    // add title block
    // add cue-card blocks 
}


// module exports
module.exports = {
    parse,
    textify
}
