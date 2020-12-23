const apiEdit = async (object, url) => {
    const options = {
        method: 'PATCH',
        headers: {
            'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content'),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(object)
    }
    const response = await fetch(url, options)
    const confirmation = await response.json();
    return confirmation;
}