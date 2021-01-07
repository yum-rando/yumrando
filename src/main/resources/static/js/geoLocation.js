"use strict"

const geoLocation = () => {
        window.navigator.geolocation.getCurrentPosition(
            ({coords})=> {
                localStorage.setItem("yumCoord",
                    JSON.stringify({ latitude: coords.latitude, longitude: coords.longitude}));
            }
        )
}
geoLocation();