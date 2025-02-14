import join from '../assets/join.png'
import { useState } from "react";
import { useNavigate } from "react-router-dom";
function Join() {
    const [query, setQuery] = useState();
    const [isValid, setIsValid] = useState(false);
    const navigate = useNavigate();
    const handleChange = async (e) => {
        setQuery(e.target.value)
        if (e.target.value.trim().length > 0) {
            try {
                const response = await fetch(`http://localhost:8080/index/isValid/${e.target.value}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${localStorage.getItem('jwtToken')}`,
                    },
                });
                if (response.status === 403) {
                    localStorage.removeItem('jwtToken');
                    localStorage.removeItem('uname');
                    localStorage.removeItem('email');
                    navigate("/login");
                    return;
                }
                if (response.ok) {
                    setIsValid(true)
                    return;
                }
                else {
                    setIsValid(false)
                }
            } catch (error) {

            }
        }
    }
    const handleJoin=()=>{
        navigate(`/room/${query}`);
    }
    return (
        <div>
            <h1 className='text-center text-5xl'>Join a Call</h1>
            <div className='flex justify-center h-[60vh] items-center'>
                <img src={join} className='w-150' />
            </div>
            <div className="flex justify-start items-center bg-blue-50 flex-col min-h-fit">
                <div className="flex justify-center items-center w-150">
                    <input
                        type="text"
                        placeholder="Enter Link"
                        value={query}
                        onChange={(e) => { handleChange(e) }}
                        className="w-[70%] p-3 mr-6 pl-10 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
                    />
                    {isValid && (
                        <div className="flex justify-center items-center cursor-pointer">
                            <button
                                className="px-6 py-3 bg-blue-600 text-white 
                                                font-semibold rounded-lg cursor-pointer transition-all
                                                shadow-md hover:bg-blue-700 
                                                focus:outline-none focus:ring-2 focus:ring-blue-400 
                                                focus:ring-opacity-75 duration-200 ease-in-out
                                                "
                                onClick={ handleJoin}
                            >
                                join
                            </button>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default Join;
