"use strict"

const geoLocation = callback => {
    if(navigator.geolocation) {
        window.navigator.geolocation.getCurrentPosition(callback)
    } else {
        alert("Browser doesn't support geolocation");
    }
}


let popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
let popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
    return new bootstrap.Popover(popoverTriggerEl)
})



