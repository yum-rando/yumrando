"use strict"
const geoLocation = () => {
    if (localStorage.getItem("yumCoord") === null) {
        window.navigator.geolocation.getCurrentPosition(
            ({coords})=> {
                localStorage.setItem("yumCoord",
                    JSON.stringify({ latitude: coords.latitude, longitude: coords.longitude}));
            }
        )
    }
}

geoLocation();