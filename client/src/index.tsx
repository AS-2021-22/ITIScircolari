import ReactDOM from 'react-dom'
import App from './App'
import WriteCircolare from './routers/writeCircolare/writeCircolare';
import CircolariPreview from './routers/showCircolari/CircolariPreview'
import { BrowserRouter,Routes,Route } from "react-router-dom";
import Circolare from './routers/showCircolari/circolare'

ReactDOM.render(
  <BrowserRouter>
    <Routes>
      <Route path='/' element={<App/>} />
      <Route path='/writeCircolare' element={<WriteCircolare/>} />
      <Route path='circolari' element={<CircolariPreview/>} />
      <Route path='circolari/:id' element={<Circolare/>} />
    </Routes>
  </BrowserRouter>,
  document.getElementById('root')
)
