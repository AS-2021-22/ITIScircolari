
import './style/css/app.css'
import { Link} from "react-router-dom";

function App() {
  return (
    <div className="App">
      <Link to={`/writeCircolare`}>
        <a> scrivi la circolare </a>
      </Link><br/>
      <Link to={`/circolari`}>
        <a> visualizza le circolari </a>
      </Link>
    </div>
  );
}

export default App
