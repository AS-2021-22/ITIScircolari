import express from 'express'
import {Circolare,Tag} from './types/circolare'
import { Request,Response } from 'express'
import bodyParser from 'body-parser'
import  mongoose  from 'mongoose'
import * as dotenv from 'dotenv'
import cors from 'cors'

import CircolareModel from './schema/circolare'

dotenv.config()

const app = express()
const PORT = process.env.PORT || 3000

const filters = ['quinte','quarte','terze','seconde','prime','biennio','triennio','studenti','professori','ATA','genitori']

app.use(bodyParser.urlencoded({extended:true}))
app.use(bodyParser.json())
app.use(cors())

mongoose.connect(process.env.DB_URL || 'error')
    .then(() => {
        console.log(`DB connected`)      
        app.listen(PORT, () => console.log(`>Server listening on port ${PORT}`))
        //let newCircolare = new CircolareModel({id:2,title:"second",description:"bgfdbdf",tags:["quinte","seconde"]})
        //newCircolare.save().catch(e => console.log("impossible duplicate keys"))
    }).catch((e) => {
        console.log(`DB not connected`)
    })

app.get('/',(req:any,res:any) => {
    res.send('hello i\'m the server')
})

app.get('/circolari',(req:any,res:any) => {
    CircolareModel.find({}).sort({id:"desc"}).then(result => res.json(result)).catch(e => res.json([]))
    

    // res.json(
    //     [
    //         {"_id":"618e27e7ae0e9ab0fd8e59ec","id":1,"title":"first","description":"vdiuscvsi9hs","tags":["tutti"],"__v":0},
    //         {"_id":"618e2d37377ca81f671edd40","id":2,"title":"second","description":"bgfdbdf","tags":["quinte","seconde"],"__v":0},
    //         {"_id":"618e3088876b596dc64baea2","id":7,"title":"inizio scuola","description":"inizia a settembre","tags":["tutti"],"__v":0},
    //         {"_id":"618e3088876b596dc64baea3","id":6,"title":"consiglio di classe","description":"straordinatio per fine anno","tags":["quinte"],"__v":0},
    //         {"_id":"618e3088876b596dc64baea4","id":3,"title":"incontro con esperto","description":"disabilità e startup","tags":["quarte","seconde"],"__v":0},
    //         {"_id":"618e3088876b596dc64baea6","id":5,"title":"gita a Trieste","description":"accompagnatori: Prof 1, Prof 2","tags":["prime","seconde"],"__v":0},
    //         {"_id":"618e3088876b596dc64baea5","id":4,"title":"corso di sicurezza","description":"con maestri del lavoro in bibblioteca","tags":["terze"],"__v":0}
    //     ])
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
        //console.log(newCircolare)
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
        CircolareModel.find({}).sort({id:"desc"}).then(result => res.json(result)).catch(e => res.json([]))
    }
    else{
        CircolareModel.find({"tags":{"$in":[...req.body.filter,'tutti']}}).sort({id:"desc"}).then(result => res.json(result)).catch(e => res.json([]))
    }
})

app.post('/filters',(req:any,res:any) => {
    res.json(filters)
})