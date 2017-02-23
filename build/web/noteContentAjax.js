$(document).ready(function(){
    
    var noteContentEditAreaID="noteContentEditArea";
    var noteContentEditAreaIDjQuery="#"+noteContentEditAreaID;
    
    if (document.getElementById(noteContentEditAreaID)!=null){
        
        setInterval(function (){
                setNoteContentUsingAJAX(noteContentEditAreaIDjQuery);
                alert("Hi!");
            }, 
            10000
        );
        
    }
});

function setNoteContentUsingAJAX(noteContentEditAreaIDjQuery){
    getNoteContent(noteContentEditAreaIDjQuery);
    setCaretToPos($(noteContentEditAreaIDjQuery)[0], 5);
    
//    var caretPosition = getCaretPosition($(noteContentEditAreaIDjQuery));
//    alert(caretPosition);
}

function getNoteContent(noteContentEditAreaIDjQuery){
    $.ajax({
        url: "Ajax?action=get&noteID=1",
        success:
            function (result) {
                $(noteContentEditAreaIDjQuery).text(result);
            }
    });
}

function setSelectionRange(input, selectionStart, selectionEnd) {
  if (input.setSelectionRange) {
    input.focus();
    input.setSelectionRange(selectionStart, selectionEnd);
  } else if (input.createTextRange) {
    var range = input.createTextRange();
    range.collapse(true);
    range.moveEnd('character', selectionEnd);
    range.moveStart('character', selectionStart);
    range.select();
  }
}

function setCaretToPos(input, pos) {
  setSelectionRange(input, pos, pos);
}

function getCaretPosition(editableDiv) {
  var caretPos = 0,
    sel, range;
  if (window.getSelection) {
    sel = window.getSelection();
    if (sel.rangeCount) {
      range = sel.getRangeAt(0);
      if (range.commonAncestorContainer.parentNode == editableDiv) {
        caretPos = range.endOffset;
      }
    }
  } else if (document.selection && document.selection.createRange) {
    range = document.selection.createRange();
    if (range.parentElement() == editableDiv) {
      var tempEl = document.createElement("span");
      editableDiv.insertBefore(tempEl, editableDiv.firstChild);
      var tempRange = range.duplicate();
      tempRange.moveToElementText(tempEl);
      tempRange.setEndPoint("EndToEnd", range);
      caretPos = tempRange.text.length;
    }
  }
  return caretPos;
}