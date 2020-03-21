
function firstButtonListener() {
    console.log('is clicked');
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'http://localhost:8080/first_button', false);
    // TODO stupid
    xhr.onreadystatechange = function () {
        if (xhr.readyState !== 4) return;
        if (xhr.status === 200) {
            const body = Array.from(document.getElementsByTagName('body'))[0];
            body.innerHTML += xhr.responseText;
        }
    };
    xhr.send()
}

const button1 = document.getElementById('content-button-1');
button1.addEventListener('click', firstButtonListener);

// Array.from(document.getElementsByClassName('login-form')).forEach(it => {
//     it.addEventListener('submit', formListener());
// });
//
// function formListener(event) {
//     const xhr = new XMLHttpRequest();
//     xhr.open('POST', '/login', true);
//     let form = document.getElementsByClassName('login-form')[0];
//     const username = encodeURIComponent(form.elements['username'].value);
//     const password = encodeURIComponent(form.elements['password'].value);
//     xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
//     xhr.onreadystatechange = function () {
//         if (xhr.readyState !== 4) return;
//         if (xhr.status === 200) {
//             form.hidden = true;
//         } else {
//             const errorText = document.getElementById("error-text");
//             errorText.hidden = false
//         }
//     };
//     xhr.send("username=" + username + "&password=" + password);
//
//     event.preventDefault();
//     return false;
// }

