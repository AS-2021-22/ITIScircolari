export interface Circolare{
    id:number,
    title:String,
    description: String,
    tags:Tag[]
}

export type Tag = 'quinte' | 'quarte' | 'terze' | 'seconde' | 'prime' | 'docenti' | 'tutti'