const express = require('express')
const presentationManager = require('../presManager/presManager')
const userStore = require('../userStore/userStore')
const parser = require('../parser/parser')
const router = express.Router()

//Pres manager routes
router.post("/presentation", presentationManager.createPres);
router.get("/presentation", presentationManager.getPres); //@TODO change to put -> get requests arent supposed to have a body
router.get("/search", presentationManager.search); //@TODO change to put -> get requests arent supposed to have a body
router.delete("/presentation", presentationManager.deletePres);

//User store routes
router.put("/login", userStore.login);
router.put("/addPresToUser", userStore.addPresToUser);

//Parser routes
router.put("/import", parser.parse));

module.exports = router;
