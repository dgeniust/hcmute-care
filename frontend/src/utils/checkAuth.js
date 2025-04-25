import { jwtDecode } from 'jwt-decode';

export const checkRole = (allowedRoles) => {
  const accessToken = localStorage.getItem('accessToken');
  if (!accessToken) return false;

  const decodedToken = jwtDecode(accessToken);
  const roles = decodedToken.roles;

  // Check if the user has any of the allowed roles
  if (Array.isArray(roles)) {
    return roles.some(role => allowedRoles.includes(role));
  } else if (typeof roles === 'string') {
    return allowedRoles.includes(roles);
  }
  return false;
}

export const checkAuth = async () => {
  const accessToken = localStorage.getItem('accessToken');
  if (!accessToken) return false;

  try {
    const response = await fetch('http://localhost:8080/api/v1/auth/refresh-token', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${accessToken}`,
      },
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem('accessToken', data.accessToken);
      return true;
    } else {
      return false;
    }
  } catch (error) {
    console.error('Error refreshing token:', error);
    return false;
  }
}
export const checkToken = () => {
  const accessToken = localStorage.getItem('accessToken');
  if (!accessToken) return false;

  try {
    const decoded = jwtDecode(accessToken);
    const currentTime = Date.now() / 1000;
    return decoded.exp > currentTime;
  } catch (error) {
    console.error('Error decoding token:', error);
    return false;
  }
}
export const checkUserId = () => {
  const userId = localStorage.getItem('userId');
  return userId ? userId : null;
}
export const checkUserFullname = () => {
  const userFullname = localStorage.getItem('userFullname');
  return userFullname ? userFullname : null;
}
export const getUserFromJWT = () => {
  const accessToken = localStorage.getItem('accessToken');
  if (!accessToken) {
    console.warn('No access token found in localStorage');
    return 'ROLE_CUSTOMER'; // Fallback role
  }
  try {
    const decoded = jwtDecode(accessToken);
    const role = decoded.roles || decoded.role || 'ROLE_CUSTOMER'; // Handle array or string
    return Array.isArray(role) ? role[0] : role;
  } catch (error) {
    console.error('Error decoding JWT token:', error);
    return 'ROLE_CUSTOMER'; // Fallback role
  }
}