function unParsePresentation(pres) {
    var indents = 0;
    var presStr = indent(indents) + "\\begin{presentation}\n";
    presStr += indent(indents + 1) + "\\title " + pres.title + "\n";
    presStr += unParseCard(pres.cards, indents + 1);
    presStr += indent(indents) + "\\end{presentation}";
    return presStr;
}

function unParseCard(cards, indents) {
    var cardStr = ""
    for (var i = 0; i < cards.length; i++) {
        var arr = getParams(cards[i]);

        //remove front and back from array; we will write that in another function
        if (arr.includes("front") || arr.includes("back")) {
            arr = arr.filter(data => (data != "front") && (data != "back"));
        }

        var paramStr = writeParams(cards[i], arr);
        var frontStr = unParseSide(cards[i].front, indents + 1, true);
        var backStr = unParseSide(cards[i].back, indents + 1, false);
        cardStr += indent(indents) + "\\begin{cuecard}" + paramStr + "\n";
        cardStr += frontStr + backStr;
        cardStr += indent(indents) + "\\end{cuecard}" + "\n";
    }
    return cardStr;
}


function unParseSide(side, indents, isFront) {
    var sideArr = getParams(side);
    var content = side.content;
    //remove content from array; we will write that in another function
    if (sideArr.includes("content")) {
        sideArr = sideArr.filter(data => data != "content");
    }
    var contentArr = getParams(content);
    if (contentArr.includes("message")) {
        contentArr = contentArr.filter(data => (data != "message"));
    }
    var contentParamStr = writeParams(content, contentArr);
    var sideParamStr = writeParams(side, sideArr);

    var paramStr = "";
    paramStr = sideParamStr.slice(0, -1) + ", " + contentParamStr.slice(1, contentParamStr.length);

    var contentStr = unParseContent(side.content, indents + 1);
    var tag = isFront ? "point" : "details"
    var sideStr = indent(indents) + `\\begin{${tag}}` + paramStr + "\n";
    sideStr += contentStr
    sideStr += indent(indents) + `\\end{${tag}}` + "\n";
    return sideStr;
}

function unParseContent(content, indents) {
    var messageArr = content.message.split("\n> ");
    if (messageArr[0]) messageArr[0] = messageArr[0].slice(2, messageArr[0].length) //chop off beginning bracket
    var contentStr = "";
    for (var i = 0; i < messageArr.length; i++) {
        contentStr += indent(indents) + "\\item " + messageArr[i] + "\n"
    }
    return contentStr;
}

//gets fields of an object
function getParams(obj) {
    var arr = []
    for (var key in obj) {
        if (obj.hasOwnProperty(key)) {
            arr.push(key);
        }
    }
    return arr;
}

//turns an array of paramaters into a string of params ie. [colour=salmon, endpause=true]
function writeParams(obj, arr) {
    if (arr.length == 0) {
        return "";
    }

    var paramStr = "[";
    for (var i in arr) {
        paramStr += `${arr[i]}=${obj[arr[i]]}, `
    }


    paramStr = paramStr.slice(0, -2); //remove last comma and space
    paramStr += "]";
    return paramStr
}

function indent(num) {
    var str = "";
    for (var i = 0; i < num; i++) {
        str += "\t"
    }
    return str
}

module.exports = {
    unParsePresentation
}