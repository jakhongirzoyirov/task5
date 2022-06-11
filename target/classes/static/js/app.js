const socket = new SockJS('/ws-endpoint');
var stompClient = Stomp.over(socket);

var currentUser = null;
var selectedContactId = null;

var users = [];

async function connect() {
    loader();
    console.log("=== INITIALIZED CURRENT USER AND CONTACTS ===")

    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

        subscribeToMessageIncome();
        console.log("=== SUBSCRIBED TO MESSAGES ===");
    });
}

async function loader() {
    await getCurrentUser();
    await getUsers();
}

function subscribeToMessageIncome() {
    stompClient.subscribe(
        '/user/' + currentUser.id + '/queue/messages',
        function (messageResponse) {
            const message = JSON.parse(messageResponse.body);

            if (selectedContactId !== null) {
                console.log(typeof message.sender.id);
                console.log(typeof selectedContactId);

                if (message.sender.id === selectedContactId || message.sender.id === currentUser.id) {
                    appendNewMessage(message);
                } else {
                    console.log('=== NOTIFICATION KELDI ===')
                    const oneContact = document.getElementById(message.sender.id.toString());
                    const notification = document.getElementById(message.sender.id.toString() + "badge");

                    if (notification != null) {
                        notification.innerText = (parseInt(notification.innerText, 10) + 1).toString();
                    } else {
                        const span = document.createElement('span');
                        span.classList.add(message.sender.id.toString() + "badge");
                        span.innerText = '1';
                        oneContact.appendChild(span);
                    }
                }
            }
        }
    );
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendMessage();
    });

});

var timeoutId = null;

function typing() {
    if (selectedContactId != null) {
        console.log("=== SELECTED CONTACT === " + selectedContactId);
        stompClient.send(
            "/app/chat/typing",
            {},
            JSON.stringify(
                {
                    'senderId': currentUser.id,
                    'receiverId': selectedContactId,
                    'typingText': $("#message").val()
                }
            )
        );

        if (timeoutId != null) {
            clearTimeout(timeoutId);
            console.log("=== BIRINCHI YOZMAYOTGANI UCHUN BOSHQATTAN 5 SEKUNDGA YOQILDI ===")
        }

        timeoutId = setTimeout(function () {
            stompClient.send(
                "/app/chat/notTyping",
                {},
                JSON.stringify(
                    {
                        'senderId': currentUser.id,
                        'receiverId': selectedContactId,
                        'typingText': $("#message").val()
                    }
                )
            );
        }, 5000);
    }
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#messages").html("");
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

async function sendMessage() {
    stompClient.send(
        "/app/chat/sendMessage",
        {},
        JSON.stringify(
            {
                'title': $("#message").val(),
                'body': $("#body").val(),
                'receiverId': selectedContactId,
                'senderId': currentUser.id
            }
        ));
}

async function getCurrentUser() {
    await fetch('/api/users/me', {method: 'GET'})
        .then(function (response) {
            response.json()
                .then(data => {
                    currentUser = data.data;
                })
        });
}

async function getUsers() {
    await fetch('/api/users', {method: 'GET'})
        .then(function (responseEntity) {
            responseEntity.json()
                .then(apiResponse => {
                    console.log("=== API RESPONSE DATA === " + apiResponse.data);
                    users = apiResponse.data;
                    apiResponse.data.forEach(contact => {
                        appendNewUser(contact)
                    })
                });
        });
}

function getChatMessages(receiverId) {
    fetch('/api/messages/byReceiverId/' + receiverId, {method: 'GET'})
        .then(function (responseEntity) {
            if (responseEntity.ok) {
                console.log("OK");
                responseEntity.json()
                    .then(apiResponse => {
                        apiResponse.data.forEach(message => {
                            appendNewMessage(message);
                        })
                    });
            } else console.log("=== NO MESSAGES ===");
        })
}

function appendNewMessage(message) {
    const messages = document.getElementById("chat-messages");
    // === IF CURRENT USER IS SENDER OF THIS MESSAGE
    if (message.sender.id === currentUser.id) {
        const outgoingMsgDiv = document.createElement('div');
        outgoingMsgDiv.classList.add('outgoing_msg');
        messages.appendChild(outgoingMsgDiv);

        const sentMsgDiv = document.createElement('div');
        sentMsgDiv.classList.add('sent_msg');
        outgoingMsgDiv.appendChild(sentMsgDiv);

        const title = document.createElement('h5');
        title.innerText = message.title;
        sentMsgDiv.appendChild(title);

        const paragraph = document.createElement('p');
        paragraph.innerText = message.body;
        sentMsgDiv.appendChild(paragraph);

        const span = document.createElement('span');
        span.classList.add('time_date');
        span.innerText = message.sentAt;
        sentMsgDiv.appendChild(span);
    } else { // IF MESSAGE SENT BY ANOTHER USER
        const incomingMsgDiv = document.createElement('div');
        incomingMsgDiv.classList.add('incoming_msg');
        messages.appendChild(incomingMsgDiv);

        const receiverMsgDiv = document.createElement('div');
        receiverMsgDiv.classList.add('received_msg');
        incomingMsgDiv.appendChild(receiverMsgDiv);

        const receivedWidthMsgDiv = document.createElement('div');
        receivedWidthMsgDiv.classList.add('received_withd_msg');
        receiverMsgDiv.appendChild(receivedWidthMsgDiv);

        const title = document.createElement('h5');
        title.innerText = message.title;
        receivedWidthMsgDiv.appendChild(title);

        const paragraph = document.createElement('p');
        paragraph.innerText = message.body;
        receivedWidthMsgDiv.appendChild(paragraph);

        const span = document.createElement('span');
        span.classList.add('time_date');
        span.innerText = message.sentAt;
        receivedWidthMsgDiv.appendChild(span);
    }
}

function appendNewUser(contact) {
    const chatName = contact.id === currentUser.id ? 'YOU' : contact.name;

    $('#chats').append(
        '<div onclick="chatClickedHandler(' + contact.id + ')" class="chat_list notification" id="' + contact.id + '">' +
        '    <div class="chat_people">' +
        '    <div class="chat_ib">\n' +
        '       <h5 style="color: white">' + chatName + ' </h5>' +
        '    </div>' +
        '    </div>' +
        '</div>'
    )

    if (contact.online) {
        $("#" + contact.id + "")
            .css("background-color", "green")
    } else {
        $("#" + contact.id + "")
            .css("background-color", "#444753")
    }
}

function chatClickedHandler(contactId) {
    // AGAR YANGI TANLANGAN CHAT TANLANIB TURGAN CHATGA TENG BO'LMASA
    if (selectedContactId !== null && selectedContactId !== contactId) {
        selectedContactId = contactId;
        $("#chat-messages").empty();

        getChatMessages(selectedContactId);
    } else if (selectedContactId === null) {

        // AGAR HALI HECH QANDAY CHAT TANLANMAGAN BO'LSA UNDA TANLA
        selectedContactId = contactId;
        getChatMessages(selectedContactId);
    }
}

function search() {
    const searchText = $("#search").val();

    if (searchText !== null) {

        if (searchText.trim() === "") {
            $("#chats").empty();
            users.forEach(contact => {
                appendNewUser(contact)
            });
        } else {
            fetch('/api/users/search', {
                method: 'post',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    'searchText': searchText
                })
            }).then(responseEntity => {
                responseEntity.json()
                    .then(data => {
                        $("#chats").empty();
                        data.forEach(user => {
                            appendNewUser(user);
                        })
                    });
            });
        }
    }
}
