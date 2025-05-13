import { useNavigate } from "react-router-dom";
import SearchBar from "./ui/SearchBar";

function AgentSearchBar() {
  const navigate = useNavigate();

  const onSearchSubmit = (e) => {
    e.preventDefault();
    const searchRequest = e.target.elements.search.value;
    const param = searchRequest ? '?nameFilter=' + searchRequest : ''
    navigate('/agents' + param)
  }

  return <SearchBar
    placeholder="Найти посредника"
    onSubmit={onSearchSubmit}
  />;
};

export default AgentSearchBar;

