import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login.jsx';
import Dashboard from './pages/Dashboard.jsx';

function App() {
  const [token, setToken] = useState(localStorage.getItem('token') || '');
  const [theme, setTheme] = useState(localStorage.getItem('theme') || 'default');

  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('theme', theme);
  }, [theme]);

  const handleLogin = (newToken) => {
    setToken(newToken);
    localStorage.setItem('token', newToken);
  };

  const handleLogout = () => {
    setToken('');
    localStorage.removeItem('token');
  };

  return (
    <Router>
      <div className="app-container animate-fade-in">
        <header style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '2rem', alignItems: 'center' }}>
          <h1 className="gradient-text" style={{ fontSize: '2rem' }}>Snowball Tracker</h1>
          <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
            <select 
              className="input-field" 
              style={{ width: 'auto', padding: '8px', cursor: 'pointer' }}
              value={theme}
              onChange={(e) => setTheme(e.target.value)}
            >
              <option value="default">Blue Theme</option>
              <option value="purple">Purple Theme</option>
              <option value="emerald">Emerald Theme</option>
              <option value="rose">Rose Theme</option>
            </select>
            {token && <button onClick={handleLogout} className="btn-primary" style={{ padding: '8px 16px' }}>Logout</button>}
          </div>
        </header>

        <Routes>
          <Route path="/login" element={!token ? <Login onLogin={handleLogin} /> : <Navigate to="/dashboard" />} />
          <Route path="/dashboard" element={token ? <Dashboard token={token} /> : <Navigate to="/login" />} />
          <Route path="*" element={<Navigate to={token ? "/dashboard" : "/login"} />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
