const express = require('express')
const presentationManager = require('../presManager/presManager')
const userStore = require('../userStore/userStore')
const parser = require('../parser/parser')
const exporter = require('../parser/export')

const router = express.Router()

//Pres manager routes
router.put("/presentation", presentationManager.createPres);
router.get("/presentation", presentationManager.getPres); // Changed from get to post, as get requests aren't supposed to have a body. 
							   // Put is not correct, as each request, even if identical, needs a response.
router.get("/search", presentationManager.search); // Changed from get to post, as get requests aren't supposed to have a body.
router.delete("/presentation", presentationManager.deletePres);
router.get("/allPresentationsOfUser", presentationManager.getAllPresOfUser);
router.put("/savePresentation", presentationManager.savePres);

//User store routes
router.put("/login", userStore.login);
router.put("/addPresToUser", userStore.addPresToUser);

//Parser routes
router.put("/import", parser.parse);

//Exporter routes
router.post("/export", exporter.unParsePresentation);

module.exports = router;
