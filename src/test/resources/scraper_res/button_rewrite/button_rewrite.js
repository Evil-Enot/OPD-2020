function buttonListener() {
    console.log('is clicked');
    const deleted = document.getElementById('to-rewrite');
    const added = document.createElement('div');
    added.innerText = 'Content after';
    deleted.insertAdjacentElement('afterend', added);
    deleted.remove();
}

const button1 = document.getElementById('rewrite-button');
button1.addEventListener('click', buttonListener);