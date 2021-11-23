import { useState, useEffect,FunctionComponent, FormEvent } from 'react'
import './../../style/css/writeCircolare.css'
import 'dotenv/config'

interface WriteCircolareProps {
    
}
 
const WriteCircolare: FunctionComponent<WriteCircolareProps> = () => {

    let [tags,setTags] = useState<string[]>(["tutti"])
    const requireFilters = (sf: Function) => {
        fetch((process.env.REACT_APP_URL as string) + 'filters',{
            method: 'POST',
            mode: 'cors',
            headers: {'Content-Type': 'application/json'},
        }).then(res => res.json()).then(res => sf(['tutti',...res])).catch(err => console.log(err))
    }

    useEffect(() => requireFilters(setTags),[])

    const handleSubmit = (e:FormEvent<HTMLFormElement>) => {
        let obj : any = {}
        let l : string[] = []
        e.preventDefault();
        const formElements = e.currentTarget.elements
        
        for(let i = 0; i<formElements.length - 1;i++){
            let key : string = (formElements.item(i) as HTMLInputElement).name
            let value : any = (formElements.item(i) as HTMLInputElement).value
            if(value === 'on') value = (formElements.item(i) as HTMLInputElement).checked
            if(typeof(value) !== 'boolean'){
                obj[key] = value
            } else if(value === true) l.push(key)
        }
        obj['tags'] = l
        fetch((process.env.REACT_APP_URL as string) + 'circolari/write',{
            method: 'POST',
            mode: 'cors',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(obj) 
        }).then(res => {
            if(res.status === 200){
                alert('Circolare pubblicata')
            }
            else alert('wrong password')
        }).catch(err => console.log(err))
    }

    return (
        <form onSubmit={handleSubmit} className='formWrite' id='form'>
            <input name='id' type="number" placeholder='id:' className='inputID' required/><br/>
            <input name='titolo' type="text" placeholder='titolo:' className='inputTitolo' required/><br/>
            <textarea name='descrizione' placeholder='descrizione:' className='inputDescrizione' required/><br/>
            <input name='password' type="text" placeholder='password:' className='inputPassword' required/><br/>
            {tags?.map((t:string, index:number) => {
                return(
                    <label className="switch" key={index}>
                        {t}
                        <input name={t} type="checkbox" className='filter' />
                        <span className="slider"></span>
                    </label>
                    )
                })}<br/>
            <input type='submit' value='Submit' className='submitButton'/>
        </form>
     );
}
 
export default WriteCircolare;