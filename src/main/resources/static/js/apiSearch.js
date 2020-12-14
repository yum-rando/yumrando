const url = 'https://developers.zomato.com/api/v2.1/geocode'
const apiSearchLocal = (lat, lon) => {
    fetch(url + `?lat=${lat}&lon=${lon}`,
        {
            headers: {
                'user-key': zKey
            }
        }).then( response => {
        response.json().then( data=>{
            console.log(data);
        })
    })
}
