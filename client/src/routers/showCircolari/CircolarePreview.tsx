import { FunctionComponent} from 'react'
import CircolarePreviewInterface from '../../types/circolarePreviewInterface'
import './../../style/css/circolarePreview.css'
import { Link} from "react-router-dom";

interface CircolarePreviewProps {
    circolare: CircolarePreviewInterface
}
 
const CircolarePreview: FunctionComponent<CircolarePreviewProps> = ({circolare}) => {
    return (<>
        <div className='container'>
            <Link to={`/circolari/${circolare.id}`} className='link'>
                <p className='number'>{circolare.id}</p> 
                <p className='title'>{circolare.title}</p>
            </Link>
        </div>        
    </>);
}
 
export default CircolarePreview;