import {FunctionComponent,useState,useEffect} from 'react'
import CircolarePreview from './CircolarePreview'
import CircolarePreviewInterface from './../../types/circolarePreviewInterface'


interface CircolariPreviewProps {
    
}
 
const CircolariPreview: FunctionComponent<CircolariPreviewProps> = () => {

    let [circolariPreview,setCircolariPreview] = useState<CircolarePreviewInterface[]>([])

    useEffect(
        function getCircolari() {
            fetch('http://localhost:5000/circolari')
                .then(res => res.json())
                .then(res => {
                    setCircolariPreview((res as CircolarePreviewInterface[]).sort((e1:CircolarePreviewInterface,e2:CircolarePreviewInterface) => e2.id - e1.id)) //decreasing order
                })
                .catch(e => console.log(e))
        },[]
    )    

    return (<>
        {circolariPreview.map(c => <CircolarePreview key={c.id}  circolare={c}/>)}
    </>);
}
 
export default CircolariPreview;