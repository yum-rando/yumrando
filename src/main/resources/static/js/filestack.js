// Make sure to put your personal FileStackKey in the keys.js file
const client = filestack.init(FileStackApiKey);

// we will pass this object as an argument in the picker method.
const options = {

    //onFileUploadFinished is called when the the user uploads a image in the
    // picker and they have successfully uploaded the image to filestack servers.
    //
    onFileUploadFinished: callback =>{

        // I save the filestack image url to a const because I plan to use it in multiple places.
        const photoURL = callback.url;

        $('#photo').val(photoURL);

        $('#photoPreview').attr('src',photoURL);
    }
}
// This is an event listen for listening to a click on a button
$('#add-photo').click(function (event){
    event.preventDefault();
    client.picker(options).open();
})