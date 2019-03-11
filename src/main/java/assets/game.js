var isSetup = true;
var placedShips = 0;
var game;
var shipType;
var vertical;
var sonar = false;
var firstShipSunk = false;
var sonarCount = 0;
var underWater = false;
var fleetMoveCount = 0;

function makeGrid(table, isPlayer) {
    for (i=0; i<10; i++) {
        let row = document.createElement('tr');
        for (j=0; j<10; j++) {
            let column = document.createElement('td');
            column.addEventListener("click", cellClick);
            row.appendChild(column);
        }
        table.appendChild(row);
    }
}

function checkSunk(elementId, ship) {

    if (elementId == "opponent" ) {


        for (var i = 0; i < ship.occupiedSquares.length; i++) {

            var square = ship.occupiedSquares[i];
            var cell = document.getElementById("opponent").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)];
            var div = cell.querySelector("div");

            if (i == 0 && (div != null || div == null)) {
                div = document.createElement('div');
                cell.appendChild(div);
                div.classList.add("marker");
                div.classList.add("hit");

                if (square.row != ship.occupiedSquares[i + 1].row) {
                    div.classList.add("up");
                }
                else {
                    div.classList.add("left");
                }
            }

            else if (i == ship.occupiedSquares.length - 1 && div != null) {
                if (square.row != ship.occupiedSquares[i - 1].row) {
                if(ship.occupiedSquares.length == 5){

                if(square.row - ship.occupiedSquares[i].row == 0){
 div.classList.add("up");
                }else{
                div.classList.add("left");
                }

                }else{
                div.classList.add("down");
                }

                }
                else {
                    div.classList.add("right");
                }
            }

            else if (i == ship.occupiedSquares.length - 1 && div == null) {
                div = document.createElement('div');
                cell.appendChild(div);
                div.classList.add("marker");
                div.classList.add("hit");

                if (square.row != ship.occupiedSquares[i - 1].row) {
                    if(ship.occupiedSquares.length == 5){

                                    if(square.row - ship.occupiedSquares[i].row == 0){
                     div.classList.add("up");
                                    }else{
                                    div.classList.add("left");
                                    }

                                    }else{
                                    div.classList.add("down");
                                    }
                }
                else {
                    div.classList.add("right");
                }
            }
        }
    }
}

function markHits(board, elementId, surrenderText) {
if(board.blockedShips != null){

         board.blockedShips.forEach((block) => {
                    var row = block.location.row-1;
                    var col = block.location.column.charCodeAt(0) - 'A'.charCodeAt(0);
                    var div = document.createElement('div');
                    var td = document.getElementById(elementId).rows[row].cells[col];
                     if (elementId == "opponent" || block.result === "MISS") {
                                td.appendChild(div);
                                div.classList.add("marker");
                            }
                            else
                                div = td.querySelector("div");

                    let className;

                     if (div != null) {
                        className = "miss";
                                            var inner = document.createElement('div');
                                                        inner.classList.add("innerCircle");
                                                        div.appendChild(inner);
                     }
                     if (div != null && !div.classList.contains(className)) {
                                div.classList.add(className);
                            }
                            if (!td.classList.contains(className)) {
                                td.classList.add(className);
                            }
            })
            }
    board.attacks.forEach((attack) => {
        if (attack.result != "FLEETMOVE" && attack.result != "INVALID") {
            var row = attack.location.row-1;
            var col = attack.location.column.charCodeAt(0) - 'A'.charCodeAt(0);
            var div = document.createElement('div');
            var td = document.getElementById(elementId).rows[row].cells[col];

            if (elementId == "opponent" || attack.result === "MISS") {
                td.appendChild(div);
                div.classList.add("marker");
            }
            else
                div = td.querySelector("div");

            let className;
            if (attack.result === "MISS") {
                className = "miss";
                var inner = document.createElement('div');
                inner.classList.add("innerCircle");
                div.appendChild(inner);
            }
            else if (attack.result === "HIT")
                className = (elementId == "player") ? "playerHit" : "hit";
            else if (attack.result === "SUNK") {
                className = (elementId == "player") ? "playerHit" : "hit";
                td.classList.add(className);
                checkSunk(elementId, attack.ship);
            }
            else if (attack.result === "SURRENDER") {
                className = "hit";
                td.classList.add(className);
                checkSunk(elementId, attack.ship);
                alert(surrenderText);
            }
            else if(attack.result == "BLOCKED")
            className = "block"
            else if (attack.result == "OCCUPIED") {
                className = "occupiedSonar";
            }
            else if (attack.result == "EMPTY") {
                className = "empty";
            }

            if (div != null && !div.classList.contains(className)) {
                div.classList.add(className);
            }
            if (div != null && !td.classList.contains(className)) {
                td.classList.add(className);
            }
        }
    })}


function addLog(board, user){ /* CHANGE HERE */
    if (isSetup){
        return
        }

    document.getElementById("Log").style.display = "block";
    if (board.attacks[board.attacks.length - 1].result == "FLEETMOVE") {
        document.getElementById("LogMessages").append("||" + user + " MOVED FLEET||" + '\n');
    }
    else {
        var row = board.attacks[board.attacks.length - 1].location.row;
            var col = board.attacks[board.attacks.length - 1].location.column;
        document.getElementById("LogMessages").append("||" + user + " " + board.attacks[board.attacks.length - 1].result + " " + col + row + "||" + '\n');
    }
    return
}

function sonarCheck(board) {                                /* checks if the player has sunk a ship */
    if (isSetup) {
       return
    }
    var isSunk = board.attacks[board.attacks.length - 1].result;
    if(isSunk == "SUNK") {
        if (!firstShipSunk) {
                document.getElementById("LogMessages").append("||PLAYER UNLOCKED SPACE LASER||" + '\n');
            }
        firstShipSunk = true;
    }
}

function redrawGrid() {

/*    clearLogMessage();*/
    Array.from(document.getElementById("opponent").childNodes).forEach((row) => row.remove());
    Array.from(document.getElementById("player").childNodes).forEach((row) => row.remove());
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    if (game === undefined) {
        return;
    }

    game.playersBoard.ships.forEach((ship) => {

var isSub = false;
var vertical = false;
var sub = ship.occupiedSquares.length;
    if(ship.occupiedSquares.length == 5){
           isSub = true;
           sub -=1;
    }
    for (var i = 0; i < sub; i++) {
    var square = ship.occupiedSquares[i];
     var cell = document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)];
     var div = cell.querySelector("div");
        if (div == null) {
            div = document.createElement("div");
            div.classList.add("ship");
            cell.appendChild(div);
        }
          if(underWater && isSub){
           div.classList.add("occupiedSubmerged");
          }else{
           div.classList.add("occupied");
          }

        if(i == ship.occupiedSquares.length - 2){
            div.classList.add("captain");
        }
        if (i == 0) {
            if (square.row != ship.occupiedSquares[i + 1].row) {
                div.classList.add("up");
            }
            else {
                div.classList.add("left");
            }
        }
        else if (i == ship.occupiedSquares.length - 1 && !isSub) {
            if (square.row != ship.occupiedSquares[i - 1].row) {
                div.classList.add("down");
            }
            else {
                div.classList.add("right");
            }
        }
         else if (i == ship.occupiedSquares.length - 2 && isSub) {
                    if (square.row != ship.occupiedSquares[i - 1].row) {
                        div.classList.add("down");
                        vertical =true;
                    }
                    else {
                        div.classList.add("right");
                        vertical = false;
                    }
                }
    }
    if (isSub) {
                if(vertical){
    			 var cell = document.getElementById("player").rows[ship.occupiedSquares[ship.occupiedSquares.length-4].row-1].cells[square.column.charCodeAt(0) - 'B'.charCodeAt(0)];
                 var div = cell.querySelector("div");
                 if (div == null) {
                     div = document.createElement("div");
                     div.classList.add("ship");
                      cell.appendChild(div);
                        }
                        if(underWater){
                         div.classList.add("occupiedSubmerged");
                         }else{
                         div.classList.add("occupied");
                         }

                        div.classList.add("left");
    } else{
        var cell = document.getElementById("player").rows[ship.occupiedSquares[ship.occupiedSquares.length-1].row-1].cells[square.column.charCodeAt(0) - 'B'.charCodeAt(0)];
        var div = cell.querySelector("div");
           if (div == null) {
                div = document.createElement("div");
                div.classList.add("ship");
                cell.appendChild(div);
               }
               if(underWater){
               div.classList.add("occupiedSubmerged");
               }else{
               div.classList.add("occupied");
                div.classList.add("up");
    		}
    }
    }

    });
    /*var overlapped = document.getElementsByClassName('occupied occupiedSubmerged');
    var i;
    for (i = 0; i < overlapped.length; i++) {
      overlapped[i].classList.remove("occupied");
      overlapped[i].classList.remove("occupiedSubmerged");
      overlapped[i].classList.add("overlapShip");
    }*/


    markHits(game.opponentsBoard, "opponent", "You won the game");
    addLog(game.opponentsBoard, "PLAYER");
    sonarCheck(game.opponentsBoard);
    markHits(game.playersBoard, "player", "You lost the game");
    addLog(game.playersBoard, "OPPONENT");
    if(!isSetup){
    document.getElementById("LogMessages").append("=====================================" + '\n');
    };
}

var oldListener;
function registerCellListener(f) {

    let el = document.getElementById("player");
    for (i=0; i<10; i++) {
        for (j=0; j<10; j++) {
            let cell = el.rows[i].cells[j];
            cell.removeEventListener("mouseover", oldListener);
            cell.removeEventListener("mouseout", oldListener);
            cell.addEventListener("mouseover", f);
            cell.addEventListener("mouseout", f);
        }
    }
    oldListener = f;
}

function cellClick() {
    let row = this.parentNode.rowIndex + 1;
    let col = String.fromCharCode(this.cellIndex + 65);
    if (isSetup) {

    var water = false;
    if(shipType == "SUBMARINE"){

    underWater = document.getElementById("is_submerged").checked;
    water = underWater;
    }

        sendXhr("POST", "/place", {game: game, shipType: shipType, x: row, y: col, isVertical: vertical, isSubmerged: water}, function(data) {
            game = data;



            redrawGrid();
            placedShips++;
            if (placedShips == 4) {
                isSetup = false;
                registerCellListener((e) => {});
            }
        });
    } else {
            sendXhr("POST", "/attack", {game: game, x: row, y: col, sonar: sonar}, function(data) {

               game = data;
               redrawGrid();
               if(sonar) {
                    sonarCount++;
                    if(sonarCount == 2) {
                    document.getElementById("sonar").click();
                    sonar = false;
                    document.getElementById("opponent").classList.remove("sonar");
                    }
               }
               if (game.opponentsBoard.hasLaser && (!sonar || sonarCount >= 2)) {
                           document.getElementById("opponent").classList.add("laser");
                       }
        })
    }
}

function clearUserMessage(){
    document.getElementById("ErrorBox").style.display = "none";
var div = document.getElementById("UserMessages");
while(div.firstChild){
    div.removeChild(div.firstChild);
}
}

function clearLogMessage() {
    document.getElementById("Log");
    var div = document.getElementById("LogMessages");
    while(div.firstChild) {
        div.removeChild(div.firstChild);
    }
}

function sendXhr(method, url, data, handler) {

var elementExists = document.getElementById("ErrorBox");

if(elementExists){

           document.getElementById("ErrorBox").style.display = "none";
}

    var req = new XMLHttpRequest();
    req.addEventListener("load", function(event) {
        if (req.status != 200) {
        clearUserMessage();
        document.getElementById("Log").style.display = "none";
        document.getElementById("ErrorBox").style.display = "block";
            document.getElementById("UserMessages").append("Cannot complete the action");
            return;
        }
        handler(JSON.parse(req.responseText));
    });
    req.open(method, url);
    req.setRequestHeader("Content-Type", "application/json");
    req.send(JSON.stringify(data));
}

function place(size) {
    return function() {
        let submarine = false;
        let sub = size;

        if(size == 5){
        sub = size-1;
        submarine = true;


        }

        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;

        vertical = document.getElementById("is_vertical").checked;
        underWater = document.getElementById("is_submerged").checked;

        let table = document.getElementById("player");
        for (let i=0; i<sub; i++) {

            let cell;
            if(submarine && i ==2){
                                   if(vertical) {

                                      let tableRow = table.rows[row+1];
                                      if (tableRow === undefined) {
                                                                // ship is over the edge; let the back end deal with it
                                                                break;
                                                            }
                                                            tableRow = table.rows[row+1];
                                                            cell = tableRow.cells[col-1];
                                                             if (cell === undefined || col <= 0 ) {
                                                             // ship is over the edge; let the back end deal with it
                                                              break;
                                                             }
                                                        } else {

                                                            cell = table.rows[row-1].cells[col+2];
                                                             if (cell === undefined) {
                                                             // ship is over the edge; let the back end deal with it
                                                              break;
                                                           }
                                                        }


                                             var div = cell.querySelector("div");

                                                         if (div == null) {
                                                             div = document.createElement("div");
                                                             div.classList.add("ship");
                                                             cell.appendChild(div);
                                                         }
                                                         div.classList.toggle((vertical) ? "left" : "up")
                                                          cell.classList.toggle((vertical) ? "sidesBorderLeft" : "sidesBorderTop")
                                                div.classList.toggle("placed");
                                                cell.classList.toggle((vertical) ? "sidesBorderVert" : "sidesBorderHoriz")
                                            }

            if(vertical) {
                let tableRow = table.rows[row+i];
                if (tableRow === undefined) {
                    // ship is over the edge; let the back end deal with it
                    break;
                }
                cell = tableRow.cells[col];
            } else {
                cell = table.rows[row].cells[col+i];
            }
            if (cell === undefined) {
                // ship is over the edge; let the back end deal with it
                break;
            }
            var div = cell.querySelector("div");

            if (div == null) {
                div = document.createElement("div");
                div.classList.add("ship");
                cell.appendChild(div);
            }

            if (i == 0) {
                div.classList.toggle((vertical) ? "up" : "left")
                cell.classList.toggle((vertical) ? "sidesBorderTop" : "sidesBorderLeft")
            }
            else if (i == sub - 1) {

                div.classList.toggle((vertical) ? "down" : "right")
                cell.classList.toggle((vertical) ? "sidesBorderBottom" : "sidesBorderRight")
            }

            div.classList.toggle("placed");

            cell.classList.toggle((vertical) ? "sidesBorderVert" : "sidesBorderHoriz")
        }



if(submarine){
    size = 5;

    }
    }

}

function setInput(button) {
    title.style.visibility = "hidden";
    button.style.visibility = "hidden";
    document.getElementById("am_vertical").style.display = "block";
    document.getElementById("Log").style.display = "block";
}

function moveShips(moveDir) {
    if(!firstShipSunk){
        clearUserMessage();
        document.getElementById("Log").style.display = "none";
        document.getElementById("ErrorBox").style.display = "block";
        document.getElementById("UserMessages").append("Sink one ship to access fleet movement.");
    }
    else if(fleetMoveCount == 2) {
        clearUserMessage();
        document.getElementById("Log").style.display = "none";
        document.getElementById("ErrorBox").style.display = "block";
        document.getElementById("UserMessages").append("Fleet movement can only be used twice per game.");
    }
    else {
    sendXhr("POST", "/attack", {game: game, moveShip: true, moveDir: moveDir}, function(data) {
        fleetMoveCount++;
        game = data;
        redrawGrid();
     })
     }
}

function initGame() {
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    document.getElementById("place_minesweeper").addEventListener("click", function(e) {
        shipType = "MINESWEEPER";
       registerCellListener(place(2));
    });
    document.getElementById("place_destroyer").addEventListener("click", function(e) {
        shipType = "DESTROYER";
       registerCellListener(place(3));
    });
    document.getElementById("place_battleship").addEventListener("click", function(e) {
        shipType = "BATTLESHIP";
       registerCellListener(place(4));
    });
    document.getElementById("place_submarine").addEventListener("click", function(e) {
            shipType = "SUBMARINE";
           registerCellListener(place(5));
        });
    document.getElementById("moveNorth").addEventListener("click", function(e) {
        moveShips(85);
    });
    document.getElementById("moveSouth").addEventListener("click", function(e) {
        moveShips(68);
    });
    document.getElementById("moveEast").addEventListener("click", function(e) {
        moveShips(82);
    });
    document.getElementById("moveWest").addEventListener("click", function(e) {
        moveShips(76);
    });
    document.getElementById("sonar").addEventListener("click", function(e) {
        if(!firstShipSunk){                                                         /*changes here, Chase! */
            clearUserMessage();
            document.getElementById("Log").style.display = "none";
            document.getElementById("ErrorBox").style.display = "block";
            document.getElementById("UserMessages").append("Sink one ship to access sonar.");
            document.getElementById("sonar").click();
        }
         if(sonarCount == 2) {
            clearUserMessage();
            document.getElementById("Log").style.display = "none";
            document.getElementById("ErrorBox").style.display = "block";
            document.getElementById("UserMessages").append("Sonar can only be used twice per game.");
            document.getElementById("sonar").click();
        }
        else {
            sonar = document.getElementById("sonar").checked;
                 if (sonar) {
                     document.getElementById("opponent").classList.add("sonar");
                     if (game.opponentsBoard.hasLaser) {
                        document.getElementById("opponent").classList.remove("laser");
                     }
                 }
                 else {
                     document.getElementById("opponent").classList.remove("sonar");
                     if (game.opponentsBoard.hasLaser) {
                        document.getElementById("opponent").classList.add("laser");
                     }
                 }

        }
    });
    sendXhr("GET", "/game", {}, function(data) {
        game = data;
    });
};
