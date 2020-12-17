const urlGet = 'https://developers.zomato.com/api/v2.1/'

const searchLocal = (lat, lon) => `geocode?lat=${lat}&lon=${lon}`;

const searchName = (name, lat, lon) => `search?q=${name}&lat=${lat}&lon=${lon}`;

let resultSet = [];
const apiSearch = async search => {
    const response = await fetch(urlGet + search,
        {
            headers: {
                'user-key': zKey,
                'Content-Type': 'application/json'
            }
        })
    const restaurants = await response.json();
    return restaurants;
}



