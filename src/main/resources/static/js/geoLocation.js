const geoLocation = () => {
    if (localStorage.getItem("yumCoord") === null) {
        window.navigator.geolocation.getCurrentPosition(
            ({coords})=> {
                let place = { latitude: coords.latitude, longitude: coords.longitude}
                localStorage.setItem("yumCoord", JSON.stringify(place));
            }
        )
    }
}

geoLocation();