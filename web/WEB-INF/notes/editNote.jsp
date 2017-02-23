<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/header.jsp" %>

    <h4>Note ID: <span id="noteID">${noteid}</span></h4>

        <c:choose>
            <c:when test="${editNote}">
                
        <div id="firepad-container"></div>

        <script>
            function init() {
                //// Initialize Firebase.
                //// TODO: replace with your Firebase project configuration.
                var config = {
                    apiKey: "AIzaSyAPke83ZtNk7OGvJvY42nE5tJSqV_9xBRM",
                    authDomain: "final-5f423.firebaseapp.com",
                    databaseURL: "https://final-5f423.firebaseio.com",
                    storageBucket: "final-5f423.appspot.com",
                    messagingSenderId: "698208670431"
                };
                firebase.initializeApp(config);
                //// Get Firebase Database reference.
                var firepadRef = getExampleRef();
                //// Create CodeMirror (with lineWrapping on).
                var codeMirror = CodeMirror(document.getElementById('firepad-container'), { lineWrapping: true });
                //// Create Firepad (with rich text toolbar and shortcuts enabled).
                var firepad = Firepad.fromCodeMirror(firepadRef, codeMirror,
                    { richTextToolbar: true, richTextShortcuts: true });

                firepad.on('ready', function() {
                });

                firepad.on('synced', function(isSynced) {
                    sendAJAXSave(firepad.getText());
                });

            }
          // Helper to get hash from end of URL or generate a random one.
            function getExampleRef() {
                var ref = firebase.database().ref();
                var hash = window.location.hash.replace(/#/g, '');
                if (hash) {
                    ref = ref.child(hash);
                } else {
                    ref = ref.push(); // generate unique location.
                    window.location = window.location + '#' + ref.key; // add it as a hash to the URL.
                }
                if (typeof console !== 'undefined') {
                    console.log('Firebase data: ', ref.toString());
                }
                return ref;
            }
            
            function sendAJAXSave(content){
                var noteID=document.getElementById("noteID").innerHTML;
                
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function() {
                    if (this.readyState == 4 && this.status == 200) {
                    }
                };
                xhttp.open("POST", "EditNote", true);
                xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                xhttp.send("action=save&noteID="+noteID+"&content="+content);

            }

        </script>
        
            </c:when>
        </c:choose>
        
    </body>
</head>