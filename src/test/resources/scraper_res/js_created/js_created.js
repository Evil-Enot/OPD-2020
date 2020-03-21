function addContent() {
    const body = document.getElementsByTagName('body')[0];
    const added = document.createElement('div');
    added.innerText = 'added content';
    body.appendChild(added);
}

setTimeout(addContent, 1000);
