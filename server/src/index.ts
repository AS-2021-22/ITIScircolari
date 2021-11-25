import express from 'express'
import bodyParser from 'body-parser'
import  mongoose  from 'mongoose'
import * as dotenv from 'dotenv'
import cors from 'cors'
import http from 'http'
import { Server } from 'socket.io'
import routerCircolari from './routes/circolari'

dotenv.config()

const app = express()
const server = http.createServer(app)
const io = new Server(server);
export default io

const PORT = process.env.PORT || 3000

console.log('connecting to DB.....');

const filters = ['quinte','quarte','terze','seconde','prime','biennio','triennio','studenti','professori','ATA','genitori']

app.use(bodyParser.urlencoded({extended:true}))
app.use(bodyParser.json())
app.use(cors())

app.use('/circolari',routerCircolari)

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
    //io.emit('update',{id:0,title:"Circolare test",tags:["quinte","tutti"]})
    io.emit('update',{id:203,title:"this is a random title"})
    res.send('hello i\'m the server')
})

app.post('/filters',(req:any,res:any) => {
    res.json(filters)
})