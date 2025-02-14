import { useState } from "react";
import { useNavigate } from "react-router-dom";
function SearchAndCreate() {
    const [query, setQuery] = useState("");
    const [results, setResults] = useState("");
    const navigate = useNavigate();
    const handleClick=async (uid) => {
            try {
                const response = await fetch(`http://localhost:8080/index/CreateRoom/${uid}`, {
                    method: "POST",
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
                const data = await response.text();
                console.log(data)
                navigate(`/room/${data}`);
            } catch (error) {
                console.error("Error during the request:", error);
            }
    }
    const handleSearch = async (e) => {
        setQuery(e.target.value)
        if (e.target.value.trim().length > 0) {
            try {
                const response = await fetch(`http://localhost:8080/index/search/${e.target.value}`, {
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
                const data = await response.json();
                setResults(data);
                console.log(data)
            } catch (error) {
                console.error("Error during the request:", error);
            }
        } else {
            setResults([]);
        }
    }
    return (
        <div className="flex justify-start items-center pt-10 bg-blue-50 flex-col max-h-fit">
            <div className="flex justify-center items-center">
                <div className="p-6 rounded-2xl shadow-lg w-80 sm:w-96 bg-white">
                    <h2 className="text-xl font-bold mb-3 text-gray-800">Search</h2>
                    <div className="relative">
                        <input
                            type="text"
                            placeholder="Search..."
                            value={query}
                            onChange={(e) => { handleSearch(e) }}
                            className="w-full p-3 pl-10 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
                        />
                        <svg
                            className="absolute left-3 top-3 w-5 h-5 text-gray-500"
                            xmlns="http://www.w3.org/2000/svg"
                            fill="none"
                            viewBox="0 0 24 24"
                            stroke="currentColor"
                        >
                            <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                strokeWidth={2}
                                d="M21 21l-4.35-4.35M15 10a5 5 0 10-10 0 5 5 0 0010 0z"
                            />
                        </svg>
                    </div>
                </div>
            </div>
            <div className="w-full p-6 pt-20 text-2xl cursor-default">
                {results.length > 0 && (
                    <div className="mt-6 bg-white p-4 rounded-lg shadow-md overflow-y-auto h-140">
                        <h3 className="text-4xl mb-4 font-extrabold text-gray-800">Results:</h3>
                        <ul className="space-y-2 mt-3">
                            {results.map((user, index) => (
                                <li key={index} className="p-2 h-40 w-fit min-w-full bg-gray-100 rounded-md flex items-center justify-between">
                                    <span className="text-gray-700r">{user.uname}</span>
                                    <div className="flex justify-center items-center cursor-pointer">
                                        <button
                                            className="px-6 py-3 bg-blue-600 text-white 
                                            font-semibold rounded-lg cursor-pointer
                                            shadow-md hover:bg-blue-700 
                                            focus:outline-none focus:ring-2 focus:ring-blue-400 
                                            focus:ring-opacity-75 transition duration-200 ease-in-out"
                                            onClick={() => handleClick(user.uid)}
                                        >
                                            Create Meet
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>)}
            </div>
        </div>
    );
}

export default SearchAndCreate;
