import express from 'express'
import CircolareModel from '../schema/circolare'
import { Request,Response } from 'express'
import io from './../index'

const routerCircolari = express.Router()

routerCircolari.get('/',(req:any,res:any) => {
    CircolareModel.find({}).sort({id:"desc"}).then(result => res.json(result)).catch(e => res.json([]))
})

routerCircolari.get('/:id',(req:Request,res:Response) => {
    let n = parseInt(req.params.id)
    CircolareModel.find({"id":n}).then(result => res.json(result[0])).catch(e => res.json([]))
})

routerCircolari.post('/write', async (req:Request,res:Response) => {
    if(req.body.password === process.env.PW_WRITE){
        const {id,titolo,descrizione,tags} = req.body
        if(!id || !titolo || !descrizione || !tags) res.status(500).json('missing fields')
        const newCircolare = new CircolareModel({id,title:titolo,description:descrizione,tags})
        newCircolare.save()
            .then(() => {
                io.emit('update',{id,title:titolo,tags})
                res.status(200).json('inviata')
            })
            .catch(err => {
                console.log('element already exists')
                res.status(500).json('db error')
            })
        
    } else res.status(500).json('wrong password')
})

routerCircolari.post('/circolari',(req:any,res:any) => {
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

export default routerCircolari