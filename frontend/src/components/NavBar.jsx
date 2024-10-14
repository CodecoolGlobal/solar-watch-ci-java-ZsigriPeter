import { Link, Outlet } from 'react-router-dom';
import { useEffect, useState } from 'react';
import "./NavBar.css"

const fetchUserContext = () => {
    return fetch("user/context", {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("jwt")}`
        }
    }).then(res => res.json());
}

function NavBar() {
    const [user, setUser] = useState(null);

    useEffect(() => {
        fetchUserContext().then(resp => {
            setUser(resp);
        }).catch(err => {
            console.log('Error fetching user context:', err);
        });
    }, []);

    const logout = () => {
        setUser(null);
        localStorage.clear();
    }

    return (
        <div>
            <nav className="nav">
                <ul className="nav-links">
                    <li><Link to="/register">Sign Up</Link></li>
                    {user ? (
                        <>
                            <li><Link to="/">Main Page</Link></li>
                            <li>
                                <Link to="/login" onClick={logout}>Log out</Link>
                            </li>
                        </>
                    ) : (
                        <li><Link to="/login">Sign In</Link></li>
                    )}
                </ul>
            </nav>
                <Outlet />
        </div>
    );
}

export default NavBar;
