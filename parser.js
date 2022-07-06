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
    this.cards.append(card);
}

Presentation.prototype.addUser = function(userID, permission) {
    this.users.append({id: userID, permission: permission});
}


CuecardBack.prototype.addContent = function(font, style, size, color, message) {
    var c = colors["black"];
    if (color) {
        c = colors[color];
    }
    // TODO add support for font, style, and size attributes
    this.content.append({
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

    // for (let i = 0; i < tokens.length; i++) {
    //     if (contexts.presentation == true) {
    //         if (contexts.cuecard == true) {
    //             if (contexts.details == true) {

    //             } else {

    //             }
    //         } else {

    //         }
    //     } else {
    //         
    //     }
    // }

    console.log(JSON.stringify(p));
    return JSON.stringify(p);

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
