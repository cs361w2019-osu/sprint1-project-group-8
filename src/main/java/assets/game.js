var isSetup = true;
var placedShips = 0;
var game;
var shipType;
var vertical;

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
    if (elementId == "opponent") {
        for (var i = 0; i < ship.occupiedSquares.length; i++) {
            var square = ship.occupiedSquares[i];
            var cell = document.getElementById("opponent").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)];
            var div = cell.querySelector("div");

            if (i == 0) {
                if (square.row != ship.occupiedSquares[i + 1].row) {
                    div.classList.add("up");
                }
                else {
                    div.classList.add("left");
                }
            }
            else if (i == ship.occupiedSquares.length - 1) {
                if (square.row != ship.occupiedSquares[i - 1].row) {
                    div.classList.add("down");
                }
                else {
                    div.classList.add("right");
                }
            }
        }
    }
}

function markHits(board, elementId, surrenderText) {
    board.attacks.forEach((attack) => {
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

        div.classList.add(className);
        td.classList.add(className);
    });
}

function redrawGrid() {
    Array.from(document.getElementById("opponent").childNodes).forEach((row) => row.remove());
    Array.from(document.getElementById("player").childNodes).forEach((row) => row.remove());
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    if (game === undefined) {
        return;
    }

    game.playersBoard.ships.forEach((ship) => { for (var i = 0; i < ship.occupiedSquares.length; i++) {
        var square = ship.occupiedSquares[i];
        var cell = document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)];
        var div = cell.querySelector("div");

        if (div == null) {
            div = document.createElement("div");
            div.classList.add("ship");
            cell.appendChild(div);
        }
        div.classList.add("occupied");

        if (i == 0) {
            if (square.row != ship.occupiedSquares[i + 1].row) {
                div.classList.add("up");
            }
            else {
                div.classList.add("left");
            }
        }
        else if (i == ship.occupiedSquares.length - 1) {
            if (square.row != ship.occupiedSquares[i - 1].row) {
                div.classList.add("down");
            }
            else {
                div.classList.add("right");
            }
        }
    }});
    markHits(game.opponentsBoard, "opponent", "You won the game");
    markHits(game.playersBoard, "player", "You lost the game");
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
        sendXhr("POST", "/place", {game: game, shipType: shipType, x: row, y: col, isVertical: vertical}, function(data) {
            game = data;
            redrawGrid();
            placedShips++;
            if (placedShips == 3) {
                isSetup = false;
                registerCellListener((e) => {});
            }
        });
    } else {
        sendXhr("POST", "/attack", {game: game, x: row, y: col}, function(data) {
            game = data;
            redrawGrid();
        })
    }
}

function sendXhr(method, url, data, handler) {
    var req = new XMLHttpRequest();
    req.addEventListener("load", function(event) {
        if (req.status != 200) {
            alert("Cannot complete the action");
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
        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;
        vertical = document.getElementById("is_vertical").checked;
        let table = document.getElementById("player");
        for (let i=0; i<size; i++) {
            let cell;
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
            }
            else if (i == size - 1) {
                div.classList.toggle((vertical) ? "down" : "right")
            }

            div.classList.toggle("placed");
        }
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
    sendXhr("GET", "/game", {}, function(data) {
        game = data;
    });
};