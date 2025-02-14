import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faRightFromBracket } from "@fortawesome/free-solid-svg-icons";
import SearchAndCreate from './SearchAndCreate.jsx'
import Join from './Join.jsx'
function Home() {
  const [uname, setUname] = useState("");
  const [query, setQuery] = useState("");
  const navigate = useNavigate();

  useEffect(() => {

    if (localStorage.getItem('jwtToken') === null) {
      navigate("/login");
    } else {
      setUname(localStorage.getItem('uname'))
      const validateJwt = async () => {
        try {
          const response = await fetch("http://localhost:8080/index/dummyreq", {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${localStorage.getItem('jwtToken')}`,
            },
          });
          navigate("/home")
          const res = await response;
          if (res.status == 403) {
            localStorage.removeItem('jwtToken')
            localStorage.removeItem('uname')
            localStorage.removeItem('email')
            navigate("/login");
          }
        } catch (error) {
          console.log("Error during the request:", error);
        }

      };
      validateJwt();
    }
  }, [navigate]);
  const logout = () => {
    localStorage.removeItem('jwtToken')
    localStorage.removeItem('uname')
    localStorage.removeItem('email')
    navigate("/login");
  }
  return (
    <>
      <div className="w-[100vw] flex justify-end py-4">
        <div className="flex justify-evemly cursor-default items-center mr-10">
          <div className="flex w-10 h-10 rounded-full text-white font-bold 
          bg-purple-600 text-3xl justify-center items-center">
            {uname.toUpperCase()[0]}
          </div>
          <div className="text-2xl ml-5">{uname}</div>
          <button onClick={logout} className="text-2xl ml-5 cursor-pointer h-full w-fit flex items-center">
            <FontAwesomeIcon icon={faRightFromBracket} className="
        transition-colors duration-200 hover:text-red-600"/></button>
        </div>
      </div>
      <div className="grid grid-cols-2 min-h-500 h-[90vh] max-w-screen">
          <Join/>
          <SearchAndCreate/>
      </div>
    </>
  );
}

export default Home;
