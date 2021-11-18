"use strict";
const mongoose_1 = require("mongoose");
const CircolareSchema = new mongoose_1.Schema({
    id: {
        type: Number,
        required: true,
        unique: true
    },
    title: {
        type: String,
        required: true
    },
    description: {
        type: String,
        required: true
    },
    tags: {
        type: [String],
        required: true
    }
});
const CircolareModel = (0, mongoose_1.model)('Circolare', CircolareSchema);
module.exports = CircolareModel;
