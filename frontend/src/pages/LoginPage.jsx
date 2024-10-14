import {useState} from "react";
import {useNavigate} from "react-router-dom";

async function postLoginUser(userData) {
    console.log(userData)
    const res = await fetch("/user/signin", {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(userData)
    });
    return res;
}


function LoginPage() {

    const navigate = useNavigate();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    async function onSubmit(e) {
        e.preventDefault();
        const userData = {
            username,
            password,
        };
        try {
            const result = await postLoginUser(userData);
            const data = await result.json();
            console.log(data);
            localStorage.setItem("jwt", data.jwt);
            localStorage.setItem("role", data.roles[0]);
            localStorage.setItem("username", data.userName);
            navigate("/");
        } catch (error) {
            console.error('Error logging in:', error);
        }
    }

    return (
        <div className="content">
            <div className="sign-up">
                <form onSubmit={onSubmit}>
                    <label htmlFor="username">Username</label><br/>
                    <input name="username" id="username" type="text" placeholder="Username" value={username}
                           onChange={e => setUsername(e.target.value)}/><br/>
                    <label htmlFor="password">Password</label><br/>
                    <input name="password" id="password" type="password" value={password}
                           onChange={e => setPassword(e.target.value)}/><br/>
                    <button type="submit">Sign In</button>
                </form>
            </div>
        </div>
    );
}

export default LoginPage;