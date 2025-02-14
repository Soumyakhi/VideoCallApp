import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import './App.css';
import Login from './components/Login.jsx';
import Home from './components/Home.jsx';
import Room from './components/Room.jsx';

const router = createBrowserRouter([
  {
    path: '/',
    element: <Home />,
    
  },
  {
    path: '/home',
    element: <Home />,
    
  },{
    path: '/login',
    element: <Login />,
  },
  {
    path: '/room/:roomid',
    element: <Room/>,
  }
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>
);
