var presentationManager = require("PresManager");

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
    // check syntax
    // assemble title
    // assemble cue-cards
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
