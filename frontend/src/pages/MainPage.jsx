import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

async function getSunsetSunRiseTime(date, city) {
    console.log(date, city);
    return fetch(`/api/getBy?date=${date}&city=${city}`,
        {
            method: "GET",
            headers:
                {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem("jwt")}`
                }
        }).then(res => res.json());
}

const fetchUserContext = (token) => {
    return fetch("user/context",
        {
            method: "GET",
            headers:
                {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem("jwt")}`
                }
        }
    ).then(res => res.json());
}


function MainPage() {

    const navigate = useNavigate();

    const [dateInput, setDateInput] = useState("");
    const [city, setCity] = useState("");
    const [data, setData] = useState("");

    const [page, setPage] = useState("");
    const [authorized, setAuthorized] = useState(false);

    useEffect(() => {
        fetchUserContext().then(resp => {
            const roles = resp.authorities.map(authority => authority.authority);
            if (roles.includes("ROLE_ADMIN")) setAuthorized(true);
            else setAuthorized(false);
        })
    }, [])

    const onSubmit = (e) => {
        e.preventDefault();
        getSunsetSunRiseTime(dateInput, city).then(
            result => {
                setData(result);
                console.log(result);
                console.log(data);
            }
        );
    };

    return (
        <div className="content">
            {
                data ? (
                    <div>
                        <table>
                            <tbody>
                            <tr>
                                <th>City</th>
                                <th>Date</th>
                                <th>Sun Rise Time</th>
                                <th>Sun Set Time</th>
                            </tr>
                            <tr>
                                <td>{data.city}</td>
                                <td>{data.date}</td>
                                <td>{data.sunrise}</td>
                                <td>{data.sunset}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                ) : (
                    <div className="sign-up">
                        <form onSubmit={onSubmit}>
                            <label htmlFor="date">Date</label><br/>
                            <input name="date" id="date" type="date" value={dateInput}
                                   onChange={e => setDateInput(e.target.value)}/><br/>
                            <label htmlFor="city">City</label><br/>
                            <input name="city" id="city" type="text" value={city}
                                   onChange={e => setCity(e.target.value)}/><br/>

                            <button type="submit">Get Result</button>
                        </form>
                    </div>
                )
            }
        </div>
    );
}

export default MainPage;