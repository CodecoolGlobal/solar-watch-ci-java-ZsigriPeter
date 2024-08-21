import {useState} from "react";
import {useNavigate} from "react-router-dom";

async function postNewUser(userData) {
    console.log(userData)
    const res = await fetch("/user/register", {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(userData)
    });
    return res;
}


function RegistrationPage() {

    const navigate = useNavigate();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const onSubmit = (e) => {
        e.preventDefault();
        const userData = {
            username,
            password,
        }
        postNewUser(userData).then(
            navigate('/')
        );
    };

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

                    <button type="submit">Sign Up</button>
                </form>
            </div>
        </div>
    );
}

export default RegistrationPage;