const uri = 'http://localhost:8080/restapi/AircraftDto';
let aircraft = [];

function getItems() {
    fetch(uri)
        .then(response => response.json())
        .then(data => _displayItems(data))
        .catch(error => console.error('Unable to get items.', error));
}

function addItem() {
    const modelTextbox = document.getElementById('add-model');
    const manufacturerTextbox = document.getElementById('add-manufacturer');
    const tailnumberTextbox = document.getElementById('add-tailnumber');

    const item = {
        model: modelTextbox.value.trim(),
        manufacturer: manufacturerTextbox.value.trim(),
        tailNumber: tailnumberTextbox.value.trim()
    };

    fetch(uri, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(item)
    })
        // .then(response => response.json())
        .then(() => {
            getItems();
            modelTextbox.value = '';
            manufacturerTextbox.value = '';
            tailnumberTextbox.value = '';
        })
        .catch(error => console.error('Unable to add item.', error));
}

function deleteItem(id) {
    fetch(`${uri}/${id}`, {
        method: 'DELETE'
    })
        .then(() => getItems())
        .catch(error => console.error('Unable to delete item.', error));
}

function displayEditForm(id, version) {
    const item = aircraft.find(item => item.id === id);

    document.getElementById('edit-model').value = item.model;
    document.getElementById('edit-id').value = item.id;
    document.getElementById('edit-version').value = item.version;
    document.getElementById('edit-manufacturer').value = item.manufacturer;
    document.getElementById('edit-tailnumber').value = item.tailNumber;
    document.getElementById('editForm').style.display = 'block';
}

function updateItem() {
    const itemId = document.getElementById('edit-id').value;
    const version = document.getElementById('edit-version').value;
    const item = {
        id: parseInt(itemId, 10),
        model: document.getElementById('edit-model').value.trim(),
        manufacturer: document.getElementById('edit-manufacturer').value.trim(),
        tailNumber: document.getElementById('edit-tailnumber').value.trim(),
        version: parseInt(version)
    };

    fetch(`${uri}/${itemId}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(item)
    })
        .then(() => getItems())
        .catch(error => console.error('Unable to update item.', error));

    closeInput();

    return false;
}

function closeInput() {
    document.getElementById('editForm').style.display = 'none';
}

function _displayCount(itemCount) {
    const name = 'aircraft';

    document.getElementById('counter').innerText = `${itemCount} ${name}`;
}

function _displayItems(data) {
    const tBody = document.getElementById('aircraft');
    tBody.innerHTML = '';

    _displayCount(data.length);

    const button = document.createElement('button');

    data.forEach(item => {
        let editButton = button.cloneNode(false);
        editButton.innerText = 'Edit';
        editButton.setAttribute('onclick', `displayEditForm(${item.id})`);

        let deleteButton = button.cloneNode(false);
        deleteButton.innerText = 'Delete';
        deleteButton.setAttribute('onclick', `deleteItem(${item.id})`);

        let tr = tBody.insertRow();

        let td1 = tr.insertCell(0);
        let textNode = document.createTextNode(`${item.model} - ${item.manufacturer} - ${item.tailNumber}`);
        td1.appendChild(textNode);

        let td2 = tr.insertCell(1);
        td2.appendChild(editButton);

        let td3 = tr.insertCell(2);
        td3.appendChild(deleteButton);
    });

    aircraft = data;
}