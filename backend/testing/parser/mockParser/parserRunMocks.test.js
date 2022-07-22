const parserMocks = require("./parser.mocks.js");

test('Valid parse text', () => {
	return parserMocks.mockParse({body: {userID: 0, text: parserMocks.validParseText}}, {body: null})
		.then((data) => {
			expect(data).toEqual({title: "hello", cards: [], feedback: [], users: [{userID: 0, permission: "owner"}]});
		});
})
test('Invalid parse text', () => {
        return parserMocks.mockParse({body: {userID: 0, text: parserMocks.invalidParseText}}, {body: null})
                .then((data) => {
                        expect(data).toThrow({err: "No \\end{presentation} token found after \\begin{presentation}."});
                });
})

test('Sample unparse', () => {
	var expectedOut = "\\begin{presentation}\n\t\\title this is the title\n\t\\begin{cuecard}[backgroundColor=1, transitionPhrase=transition phrase, endWithPause=0]\n\t\t\\begin{point}[backgroundColor=2, font=front font, style=front style, size=3, colour=4]\n\t\t\t\\item front message\n\t\t\\end{point}\n\t\t\\begin{details}[backgroundColor=5, font=back font, style=back style, size=6, colour=7]\n\t\t\t\\item back message\n\t\t\t\\item point 2\n\t\t\\end{details}\n\t\\end{cuecard}\n\t\\begin{cuecard}[backgroundColor=1, transitionPhrase=w, endWithPause=0]\n\t\t\\begin{point}[backgroundColor=2, font=d, style=d, size=4, colour=5]\n\t\t\t\\item\n\t\t\\end{point}\n\t\t\\begin{details}[backgroundColor=5, font=a, style=a, size=6, colour=7]\n\t\t\t\\item\n\t\t\\end{details}\n\t\\end{cuecard}\n\\end{presentation}"
	var actualOut = parserMocks.mockUnParsePresentation(parserMocks.sampleUnparseInput);
	console.log(actualOut);
	expect(actualOut).toEqual(expectedOut);
})
