import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faGoogle } from "@fortawesome/free-brands-svg-icons";
import { useEffect } from "react";
import { useNavigate } from 'react-router-dom';
const CLIENT_ID = "888020322320-5277hc0lob72ggc8uka89jcj99thho4d.apps.googleusercontent.com";
const REDIRECT_URI = "http://localhost:5173/login"; // Your redirect URI after login
function Login() {
    const navigate = useNavigate();
    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const code = decodeURIComponent(urlParams.get('code'));
        if (code!=null && code.length>5) {
            const sendCodeToServer = async () => {
                try {
                    const response = await fetch("http://localhost:8080/login", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: JSON.stringify({
                            codeStr: code,
                        }),
                    });
                    const data = await response.json();
                    localStorage.setItem('email', data.email)
                    localStorage.setItem('uname', data.username)
                    localStorage.setItem('jwtToken', data.jwtToken)
                    navigate("/home")
                } catch (error) {
                    console.log("Error during the request:", error);
                }
            };

            sendCodeToServer();
        }
    }, []);

    const handleGoogleLogin = () => {
        const googleAuthURL = `https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&scope=https://www.googleapis.com/auth/userinfo.email&access_type=offline&prompt=consent`;

        window.location.href = googleAuthURL;
    };

    return (
        <div className="flex items-center justify-center bg-gray-100 h-screen">
            <div className="bg-white shadow-lg rounded-lg p-6 max-w-sm w-full text-center">
                <h2 className="text-xl font-semibold mb-4 text-gray-700">
                    Sign in with Google
                </h2>
                <button
                    type="button"
                    onClick={handleGoogleLogin}
                    className="cursor-pointer text-white w-full bg-[#4285F4] hover:bg-[#357ae8] focus:outline-none focus:ring-[#4285F4]/50 font-medium rounded-lg text-sm px-5 py-3 inline-flex items-center justify-center shadow-md"
                >
                    <FontAwesomeIcon icon={faGoogle} className="mr-3 w-5 h-5" />
                    Continue with Google
                </button>
            </div>
        </div>
    );
}

export default Login;
