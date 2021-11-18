"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    Object.defineProperty(o, k2, { enumerable: true, get: function() { return m[k]; } });
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = __importDefault(require("express"));
const body_parser_1 = __importDefault(require("body-parser"));
const mongoose_1 = __importDefault(require("mongoose"));
const dotenv = __importStar(require("dotenv"));
dotenv.config();
const app = (0, express_1.default)();
const PORT = process.env.PORT || 3000;
let circolari = [
    { id: 1, title: 'inizio scuola', description: 'inizia a settembre', tags: ['tutti'] },
    { id: 2, title: 'consiglio di classe', description: 'straordinatio per fine anno', tags: ['quinte'] },
    { id: 3, title: 'incontro con esperto', description: 'disabilità e startup', tags: ['quarte', 'seconde'] },
    { id: 4, title: 'corso di sicurezza', description: 'con maestri del lavoro in bibblioteca', tags: ['terze'] },
    { id: 5, title: 'gita a Trieste', description: 'accompagnatori: Prof 1, Prof 2', tags: ['prime', 'seconde'] }
];
const filters = ['quinte', 'quarte', 'terze', 'seconde', 'prime', 'biennio', 'triennio', 'studenti', 'professori', 'ATA', 'genitori'];
const findIfCommonValues = (arr1, arr2) => {
    for (let i = 0; i < arr1.length; i++)
        if (arr2.includes(arr1[i]) || arr2.includes('tutti'))
            return true;
    return false;
};
app.use(body_parser_1.default.urlencoded({ extended: true }));
app.use(body_parser_1.default.json());
const url = process.env.DB_URL || 'error';
mongoose_1.default.connect(url)
    .then(() => {
    console.log(`DB connected`);
    // let newCircolare = new CircolareModel({id:1,title:"first",description:"vdiuscvsi9hs",tags:["tutti"]})
    // newCircolare.save().catch(e => console.log("impossible duplicate keys"))
}).catch((e) => {
    console.log(`DB not connected ${url}`);
});
app.get('/', (req, res) => res.send('hello I\'m the server'));
app.get('/circolari', (req, res) => {
    res.json(circolari);
});
app.get('/circolari/:id', (req, res) => {
    let n = parseInt(req.params.id);
    if (circolari[n])
        res.json(circolari[n]);
    else
        res.json({ 'message': 'questa circolare non esiste' });
});
app.post('/circolari', (req, res) => {
    if (typeof (req.body.filter) === 'string') {
        try {
            req.body.filter = JSON.parse(req.body.filter);
        }
        catch (e) {
            req.body.filter = undefined;
        }
    }
    if (!req.body.filter)
        res.json(circolari);
    else if (JSON.stringify(req.body.filter) === JSON.stringify([]))
        res.json(circolari);
    else if (typeof ([]) !== typeof (req.body.filter))
        res.json(circolari);
    else {
        let circToSend = circolari.filter(c => findIfCommonValues(req.body.filter, c.tags));
        res.json(circToSend);
    }
});
app.post('/filters', (req, res) => {
    ///console.log("called")
    res.json(filters);
});
app.listen(PORT, () => console.log(`>Server listening on port ${PORT}`));
