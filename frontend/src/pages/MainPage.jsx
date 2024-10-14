import { useState} from "react";
import {useNavigate} from "react-router-dom";
import "./MainPage.css"

async function getSunsetSunRiseTime(date, city) {
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

function MainPage() {

    const navigate = useNavigate();

    const [dateInput, setDateInput] = useState("");
    const [city, setCity] = useState("");
    const [data, setData] = useState("");

    const onSubmit = (e) => {
        e.preventDefault();
        getSunsetSunRiseTime(dateInput, city).then(
            result => {
                setData(result);
                if(result.status === 401) {
                    navigate("/login");
                }
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
                                <th>State</th>
                                <th>Country</th>
                                <th>City</th>
                                <th>Date</th>
                                <th>Sun Rise Time</th>
                                <th>Sun Set Time</th>
                            </tr>
                            {data.map((city,index) => (
                                <tr key={index}>
                                    <td>{city.state}</td>
                                    <td>{city.country}</td>
                                    <td>{city.city}</td>
                                    <td>{city.date}</td>
                                    <td>{city.sunrise}</td>
                                    <td>{city.sunset}</td>

                                </tr>
                            ))}
                            </tbody>
                        </table>
                        <button onClick={()=>setData(null)}>Search New</button>
                    </div>
                ) : (
                    <div className="form-div">
                        <form onSubmit={onSubmit}>
                            <label htmlFor="date">Date:</label><br/>
                            <input name="date" id="date" type="date" value={dateInput}
                                   onChange={e => setDateInput(e.target.value)}/><br/>
                            <label htmlFor="city">City:</label><br/>
                            <input name="city" id="city" type="text" value={city}
                                   onChange={e => setCity(e.target.value)}/><br/>
                            <button type="submit">Get Results</button>
                        </form>
                    </div>
                )
            }
        </div>
    );
}

export default MainPage;