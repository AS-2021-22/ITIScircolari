import { FunctionComponent, useEffect, useState } from 'react'
import { useParams } from "react-router-dom";
import CircolareInterface from '../../types/Circolare'
import '../../style/css/circolare.css'

interface CircolareProps {
    
}
 
const Circolare: FunctionComponent<CircolareProps> = () => {
    let params = useParams() // :id

    let [circolare,setCircolare] = useState<CircolareInterface>({id:-1,title:'empty',description:'error, can\'t find on DB',tags:[]})

    useEffect(
        () => {
        function getCircolare() {
            fetch(`http://localhost:5000/circolari/${params.id || ''}`)
                .then(res => res.json())
                .then(res => {
                    setCircolare(res as CircolareInterface)
                })
                .catch(e => console.log(e))
        }
        getCircolare()
    },[]
    )

    return ( <>
        <div className='circolare'>
            <p className='id'>N: {circolare.id}</p>
            <p className='title'>title: {circolare.title}</p>
            <p className='description'>{circolare.description}</p>
            <p className='tags'>{JSON.stringify(circolare.tags)}</p>
        </div>
    </> );
}
 
export default Circolare;