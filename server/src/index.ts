import express from 'express'
import { Request,Response } from 'express'
import bodyParser from 'body-parser'
import  mongoose  from 'mongoose'
import * as dotenv from 'dotenv'
import cors from 'cors'
import http from 'http'
import { Server } from 'socket.io'

import CircolareModel from './schema/circolare'

dotenv.config()

const app = express()
const server = http.createServer(app)
const io = new Server(server);

const PORT = process.env.PORT || 3000

console.log('connectiong to DB.....');

const filters = ['quinte','quarte','terze','seconde','prime','biennio','triennio','studenti','professori','ATA','genitori']

app.use(bodyParser.urlencoded({extended:true}))
app.use(bodyParser.json())
app.use(cors())

mongoose.connect(process.env.DB_URL || 'error')
    .then(() => {
        console.log(`DB connected`)      
        server.listen(PORT, () => console.log(`>Server listening on port ${PORT}`))
        //let newCircolare = new CircolareModel({id:2,title:"second",description:"bgfdbdf",tags:["quinte","seconde"]})
        //newCircolare.save().catch(e => console.log("impossible duplicate keys"))
    }).catch((e) => {
        console.log(e)
        console.log(`DB not connected`)
    })

io.on('connection',(socket) => {
    //console.log('user has connected')

    socket.on('disconnect',() => {
        //console.log(`Socket ${socket.id} has disconnected`)
    })
})

app.get('/',(req:any,res:any) => {
    io.emit('update',{id:0,title:"Circolare test",tags:["quinte","tutti"]})
    res.send('hello i\'m the server')
})

app.get('/circolari',(req:any,res:any) => {
    CircolareModel.find({}).sort({id:"desc"}).then(result => res.json(result)).catch(e => res.json([]))
})

app.get('/circolari/:id',(req:Request,res:Response) => {
    let n = parseInt(req.params.id)
    //await CircolareModel.findOneAndDelete({id:n})
    CircolareModel.find({"id":n}).then(result => res.json(result[0])).catch(e => res.json([]))
})

app.post('/circolari/write', async (req:Request,res:Response) => {
    if(req.body.password === process.env.PW_WRITE){
        const {titolo,descrizione,tags} = req.body
        const n = await CircolareModel.countDocuments()
        const newCircolare = new CircolareModel({id:n + 1,title:titolo,description:descrizione,tags:tags})
        newCircolare.save().catch(err => console.log('element already exists'))
        res.status(200).send('inviata')
    } else res.status(500).send('wrong password')
})

app.post('/circolari',(req:any,res:any) => {
    if(typeof(req.body.filter) === 'string'){
        try{
            req.body.filter = JSON.parse(req.body.filter)
        } catch (e){
            req.body.filter = undefined
        }        
    }
    if(!req.body.filter || JSON.stringify(req.body.filter) === JSON.stringify([]) || typeof([]) !== typeof(req.body.filter)) {
        CircolareModel.find({},'id title').sort({id:"desc"}).then(result => res.json(result)).catch(e => res.json([]))
    }
    else{
        CircolareModel.find({"tags":{"$in":[...req.body.filter,'tutti']}},'id title').sort({id:"desc"}).then(result => res.json(result)).catch(e => res.json([]))
    }
})

app.post('/filters',(req:any,res:any) => {
    res.json(filters)
})