import {Schema,model} from 'mongoose'

interface Circolare{
    id:number,
    title: string,
    description: string,
    tags: string[]
}

const CircolareSchema = new Schema<Circolare>({
    id:{
        type: Number,
        required:true,
        unique:true
    },
    title:{
        type:String,
        required:true
    },
    description:{
        type:String,
        required:true
    },
    tags:{
        type:[String],
        required:true
    }
})

const CircolareModel = model<Circolare>('Circolare',CircolareSchema)

export = CircolareModel