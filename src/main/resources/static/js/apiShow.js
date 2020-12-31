const apiShow = async (id, url) => {
    const options = {
        method: 'GET',
        headers: {
            'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content'),
            'Content-Type': 'application/json'
        },
    }
    const response = await fetch(url + id, options)
    const confirmation = await response.json();
    return confirmation;
}