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
const cors_1 = __importDefault(require("cors"));
const http_1 = __importDefault(require("http"));
const socket_io_1 = require("socket.io");
const circolare_1 = __importDefault(require("./schema/circolare"));
dotenv.config();
const app = (0, express_1.default)();
const server = http_1.default.createServer(app);
const io = new socket_io_1.Server(server);
const PORT = process.env.PORT || 3000;
console.log('connectiong to DB.....');
const filters = ['quinte', 'quarte', 'terze', 'seconde', 'prime', 'biennio', 'triennio', 'studenti', 'professori', 'ATA', 'genitori'];
app.use(body_parser_1.default.urlencoded({ extended: true }));
app.use(body_parser_1.default.json());
app.use((0, cors_1.default)());
mongoose_1.default.connect(process.env.DB_URL || 'error')
    .then(() => {
    console.log(`DB connected`);
    server.listen(PORT, () => console.log(`>Server listening on port ${PORT}`));
    //let newCircolare = new CircolareModel({id:2,title:"second",description:"bgfdbdf",tags:["quinte","seconde"]})
    //newCircolare.save().catch(e => console.log("impossible duplicate keys"))
}).catch((e) => {
    console.log(e);
    console.log(`DB not connected`);
});
io.on('connection', (socket) => {
    //console.log('user has connected')
    socket.on('disconnect', () => {
        //console.log(`Socket ${socket.id} has disconnected`)
    });
});
app.get('/', (req, res) => {
    //io.emit('update',{id:0,title:"Circolare test",tags:["quinte","tutti"]})
    io.emit('update', { id: 203, title: "this is a random title" });
    res.send('hello i\'m the server');
});
app.get('/circolari', (req, res) => {
    circolare_1.default.find({}).sort({ id: "desc" }).then(result => res.json(result)).catch(e => res.json([]));
});
app.get('/circolari/:id', (req, res) => {
    let n = parseInt(req.params.id);
    circolare_1.default.find({ "id": n }).then(result => res.json(result[0])).catch(e => res.json([]));
});
app.post('/circolari/write', async (req, res) => {
    //console.log(req.body)
    if (req.body.password === process.env.PW_WRITE) {
        const { id, titolo, descrizione, tags } = req.body;
        if (!id || !titolo || !descrizione || !tags)
            res.status(500).json('missing fields');
        const newCircolare = new circolare_1.default({ id, title: titolo, description: descrizione, tags });
        newCircolare.save()
            .then(() => {
            io.emit('update', { id, title: titolo, tags });
            res.status(200).json('inviata');
        })
            .catch(err => {
            console.log('element already exists');
            res.status(500).json('db error');
        });
    }
    else
        res.status(500).json('wrong password');
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
    if (!req.body.filter || JSON.stringify(req.body.filter) === JSON.stringify([]) || typeof ([]) !== typeof (req.body.filter)) {
        circolare_1.default.find({}, 'id title').sort({ id: "desc" }).then(result => res.json(result)).catch(e => res.json([]));
    }
    else {
        circolare_1.default.find({ "tags": { "$in": [...req.body.filter, 'tutti'] } }, 'id title').sort({ id: "desc" }).then(result => res.json(result)).catch(e => res.json([]));
    }
});
app.post('/filters', (req, res) => {
    res.json(filters);
});
