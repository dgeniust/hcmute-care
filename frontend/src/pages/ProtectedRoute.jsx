import React, { useEffect, useState } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import {jwtDecode} from 'jwt-decode';
import { useLocation } from 'react-router-dom';
import MainLoading from '../components/MainLoading';
const ProtectedRoute = ({ allowedRoles }) => {
  const [authorized, setAuthorized] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [roles, setRoles] = useState(null); // Store roles for redirect
  const location = useLocation(); 

  useEffect(() => {
    const checkAuth = async () => {
      const accessToken = localStorage.getItem('accessToken');
      const refreshToken = localStorage.getItem('refreshToken');
        console.log('Set Role to :', allowedRoles);
        console.log('Access Token in protected route:', accessToken);
        console.log('Refresh Token in protected route:', refreshToken);
      if (!accessToken || !refreshToken){
        console.log('Missing tokens');
        setIsAuthenticated(false);
        return setAuthorized(false);
      } 
      const payload = {
        refreshToken: refreshToken,
      }
      console.log('Payload:', JSON.stringify(payload, null, 2));
      try {
        const decoded = jwtDecode(accessToken);
        console.log('Decoded Access Token:', decoded);
        const currentTime = Date.now() / 1000;
        console.log('Current Time:', currentTime);

        if (decoded.exp < currentTime) {
          console.log('Token expired, attempting refresh');
          // Access token hết hạn → gọi refresh
          const res = await fetch('http://localhost:8080/api/v1/auth/refresh-token', {
            method: 'POST',
            headers: { 
              'Content-Type': 'application/json',
              //Authorization: `Bearer ${accessToken}`,
            },
            body: JSON.stringify(payload),
          });
          console.log('Refresh Response Status:', res.status);

          if (!res.ok) {
            console.log('Refresh failed:', await res.text());
            setIsAuthenticated(false);
            return setAuthorized(false);
          }

          const data = await res.json();
          localStorage.setItem('accessToken', data.data.accessToken);
          console.log('New Access Token from refresh token:', data.data.accessToken);
        }

        const finalToken = localStorage.getItem('accessToken');
        const roles = jwtDecode(finalToken).roles;
        localStorage.setItem('roles', roles); // Store roles in localStorage for later use
        console.log('Decode Role:', roles);
        console.log('Allowed Roles:', allowedRoles[0]);
        
        //Check role matching
        let hasMatchingRole = false;
        if (Array.isArray(roles)) {
          // Roles is an array
          hasMatchingRole = roles.some((role) => allowedRoles.includes(role));
        } else if (typeof roles === 'string') {
          // Roles is a single string
          hasMatchingRole = allowedRoles.includes(roles);
        } else {
          // Roles is null/undefined
          console.log('No valid roles found in token');
          hasMatchingRole = false;
        }

        console.log('Role Match:', hasMatchingRole);

        setRoles(roles); // Store roles for redirect
        setIsAuthenticated(true);
        setAuthorized(hasMatchingRole);
      } catch (err) {
        console.log('Error:', err);
        setIsAuthenticated(false);
        setAuthorized(false);
      }
    };

    checkAuth();
  }, [allowedRoles]);

  if (authorized === null) return <MainLoading />; // Loading state
  if (!isAuthenticated) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }
  if (!authorized) {
    const userRole = Array.isArray(roles) ? roles[0] : roles; // Handle array or string
    const redirectMap = {
      ROLE_CUSTOMER: '/',
      ROLE_ADMIN: '/admin',
      ROLE_DOCTOR: '/doctor',
      ROLE_STAFF: '/staff',
      ROLE_NURSE: '/nurse',
    };
    const redirectPath = redirectMap[userRole] || '/'; // Default to '/'
    return <Navigate to={redirectPath} replace state={{ from: location }} />;
  }
  return <Outlet />;
};

export default ProtectedRoute;
