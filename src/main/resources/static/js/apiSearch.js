const url = 'https://developers.zomato.com/api/v2.1/'

const searchLocal = (lat, lon) => `geocode?lat=${lat}&lon=${lon}`;

const searchName = (name, lat, lon) => `search?q=${name}&lat=${lat}&lon=${lon}`;

let results = {};
const apiSearch = search => {
     fetch(url + search,
        {
            headers: {
                'user-key': zKey
            }
        }).then( response => {
        response.json().then(data=>{
            results.info = data.restaurants;

        })
    })
    return results;
}


