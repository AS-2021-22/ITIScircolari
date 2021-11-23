
import './style/css/app.css'
import { Link} from "react-router-dom";

function App() {
  return (
    <div className="App">
      <Link to={`/writeCircolare`}>
         scrivi la circolare
      </Link><br/>
      <Link to={`/circolari`}>
         visualizza le circolari
      </Link>
    </div>
  );
}

export default App
