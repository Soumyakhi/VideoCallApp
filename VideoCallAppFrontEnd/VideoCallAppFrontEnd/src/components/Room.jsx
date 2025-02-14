import React, { useEffect, useRef, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPhone, faClipboard ,faMicrophoneSlash,faVideoSlash} from "@fortawesome/free-solid-svg-icons";

const Room = () => {
  const { roomid } = useParams();
  const wsUrl = `ws://localhost:8080/ws/${decodeURIComponent(roomid)}/${localStorage.getItem("jwtToken")}`;
  const wsRef = useRef(null);
  const peerRef = useRef(null);
  const localVideoRef = useRef(null);
  const remoteVideoRef = useRef(null);
  const [type, setType] = useState("slave");
  const [videoState, setVideoState] = useState(true);
  const [micState, setMicState] = useState(true);
  const navigate = useNavigate();
  let role = "slave";
  const handleClose = () => {
    if (peerRef.current) {
      peerRef.current.close();
      peerRef.current = null;
    }

    if (localVideoRef.current && localVideoRef.current.srcObject) {
      localVideoRef.current.srcObject.getTracks().forEach(track => track.stop());
      localVideoRef.current.srcObject = null;
    }

    if (wsRef.current) {
      wsRef.current.close();
    }

    navigate('/home');
  }

  useEffect(() => {
    const validateJwt = async () => {
      try {
        const rid = decodeURIComponent(roomid);
        const response = await fetch(`http://localhost:8080/index/isValidRoom/${rid}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${localStorage.getItem('jwtToken')}`,
          },
        });
        const res = await response;
        if (res.status == 403) {
          localStorage.removeItem('jwtToken')
          localStorage.removeItem('uname')
          localStorage.removeItem('email')
          navigate("/login");
        }
        if (!res.ok) {
          navigate("/home");
        }
      } catch (error) {
        console.log("Error during the request:", error);
      }

    };
    validateJwt();
    wsRef.current = new WebSocket(wsUrl);

    wsRef.current.onopen = () => {
      console.log("Connected to WebSocket");
      wsRef.current.send(JSON.stringify({ type: "ip-info", uname: localStorage.getItem('uname') }));
    };

    wsRef.current.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        console.log("Received:", data);
        if (data.type === "ip-info" && localStorage.getItem('uname') !== data.uname) {
          setType(() => "master");
          role = "master";
          startCall()
        } else if (data.type === "offer") {
          handleOffer(data.offer);
        } else if (data.type === "answer") {
          handleAnswer(data.answer);
        } else if (data.type === "ice-candidate") {
          handleICECandidate(data.candidate);
        }
      } catch (error) {
        console.error("Invalid WebSocket message format", error);
      }
    };

    return () => {
      if (peerRef.current) {
        peerRef.current.close();
        peerRef.current = null;
      }
  
      if (localVideoRef.current && localVideoRef.current.srcObject) {
        localVideoRef.current.srcObject.getTracks().forEach(track => track.stop());
        localVideoRef.current.srcObject = null;
      }
  
      if (wsRef.current) {
        wsRef.current.close();
      }
    };
  }, [wsUrl]);
  const startCall = async () => {
    if (role !== "master") return;

    peerRef.current = new RTCPeerConnection({ iceServers: [{ urls: "stun:stun.l.google.com:19302" }] });
    const stream = await navigator.mediaDevices.getUserMedia({ video: { width: { ideal: 640 }, height: { ideal: 480 },
      frameRate: { ideal: 30, max: 60 } },
      audio: true });
    localVideoRef.current.srcObject = stream;
    stream.getTracks().forEach(track => peerRef.current.addTrack(track, stream));
    peerRef.current.onicecandidate = (event) => {
      if (event.candidate) {
        wsRef.current.send(JSON.stringify({ type: "ice-candidate", candidate: event.candidate }));
      }
    };
    peerRef.current.ontrack = (event) => {
      remoteVideoRef.current.srcObject = event.streams[0];
    };
    const offer = await peerRef.current.createOffer();
    await peerRef.current.setLocalDescription(offer);
    console.log(JSON.stringify({ type: "offer", offer }));
    wsRef.current.send(JSON.stringify({ type: "offer", offer }));
  };

  const handleOffer = async (offer) => {
    peerRef.current = new RTCPeerConnection({ iceServers: [{ urls: "stun:stun.l.google.com:19302" }] });

    peerRef.current.onicecandidate = (event) => {
      if (event.candidate) {
        wsRef.current.send(JSON.stringify({ type: "ice-candidate", candidate: event.candidate }));
      }
    };
    const stream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
    localVideoRef.current.srcObject = stream;
    stream.getTracks().forEach(track => peerRef.current.addTrack(track, stream));

    peerRef.current.ontrack = (event) => {
      remoteVideoRef.current.srcObject = event.streams[0];
    };

    await peerRef.current.setRemoteDescription(new RTCSessionDescription(offer));

    const answer = await peerRef.current.createAnswer();
    await peerRef.current.setLocalDescription(answer);

    wsRef.current.send(JSON.stringify({ type: "answer", answer }));
  };

  const handleAnswer = async (answer) => {
    await peerRef.current.setRemoteDescription(new RTCSessionDescription(answer));
  };

  const handleICECandidate = async (candidate) => {
    await peerRef.current.addIceCandidate(new RTCIceCandidate(candidate));
  };
  const handleCopy = () => {
    navigator.clipboard.writeText(decodeURIComponent(roomid));
  }
  const handleVideo = () => {
    setVideoState(prevState => !prevState);
    if (localVideoRef.current && localVideoRef.current.srcObject) {
      localVideoRef.current.srcObject.getVideoTracks().forEach(track => {
        track.enabled = !track.enabled;
      });
    }
  };
  
  const handleMic = () => {
    setMicState(prevState => !prevState);
    if (localVideoRef.current && localVideoRef.current.srcObject) {
      localVideoRef.current.srcObject.getAudioTracks().forEach(track => {
        track.enabled = !track.enabled;
      });
    }
  };
  
  return (
    <div>
      <h2 className="text-5xl text-center p-6 cursor-default">WebRTC Call</h2>
      <h2 className="text-2xl text-center p-6 cursor-default">Room Id: {decodeURIComponent(roomid)}
        <span className="text-2xl text-blue-500 flex justify-center">
          <button className="cursor-pointer" onClick={handleCopy}>
            <FontAwesomeIcon icon={faClipboard} />
          </button>
        </span>
      </h2>
      <p className="text-2xl text-center p-6 cursor-default">Role: {type}</p>
      <div className="grid grid-rows-1 grid-cols-2 gap-4">
        <div className="flex flex-col">
          <video ref={localVideoRef} autoPlay playsInline muted style={{ width: "50vw", border: "2px solid black" 
            ,backgroundColor:"black"}}>
          </video>
          <div className="flex justify-center w-[50vw] gap-8">
          <button className={`h-12 w-12 p-4 cursor-pointer rounded-full mt-6 flex justify-center 
            items-center ${videoState ? 'bg-black' : 'bg-red-600'}
            hover:bg-red-400 east-in-out duration-200`} onClick={handleVideo}>
            <FontAwesomeIcon icon={faVideoSlash} className="text-white text-xl font-bold" />
          </button>
          <button className={`h-12 w-12 p-4 cursor-pointer rounded-full mt-6 flex justify-center 
            items-center ${micState ? 'bg-black' : 'bg-red-600'}
            hover:bg-red-400 east-in-out duration-200`} onClick={handleMic}>
            <FontAwesomeIcon icon={faMicrophoneSlash} className="text-white text-xl font-bold" />
          </button>
          <button className="h-12 w-12 p-4 cursor-pointer rounded-full mt-6 flex justify-center 
            items-center bg-red-600
            hover:bg-red-400 east-in-out duration-200" onClick={handleClose}>
            <FontAwesomeIcon icon={faPhone} className="text-white text-xl font-bold" />
          </button>
        </div>
        </div>

        <video ref={remoteVideoRef} autoPlay playsInline style={{ width: "50vw", border: "2px solid black"
          ,backgroundColor:"black" }}></video>
      </div>
      <div className="">
      </div>
    </div>
  );
};

export default Room;
