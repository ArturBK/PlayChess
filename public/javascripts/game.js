var onChange = function(oldPos, newPos) {
  var newPos2 = JSON.parse(JSON.stringify(getChanges(oldPos, newPos)));
  var oldPos2 = JSON.parse(JSON.stringify(getChanges(newPos, oldPos)));
  var oldPos3, newPos3;

  console.log("newPos2[0].key");
  console.log(newPos2[0]);

  $.each(newPos2, function(key, value){
    newPos3 = key;
  });

  $.each(oldPos2, function(key, value){
    oldPos3 = key;
  });

  sendFen(oldPos3, newPos3, $("#fen").text())
};

function getChanges(prev, now) {
    var changes = {};
    for (var prop in now) {
        if (!prev || prev[prop] !== now[prop]) {
            if (typeof now[prop] == "object") {
                var c = getChanges(prev[prop], now[prop]);
                if (! _.isEmpty(c) ) // underscore
                    changes[prop] = c;
            } else {
                changes[prop] = now[prop];
            }
        }
    }
    return changes;
}

function loadFen(oldPos, newPos, uid, senderUid) {
  game.move({ from: oldPos,    to :newPos, promotion: 'q' });
  board1.position(game.fen());
}

var board1,
  game = new Chess(),
  statusEl = $('#status'),
  fenEl = $('#fen');
  // pgnEl = $('#pgn');

// do not pick up pieces if the game is over
// only pick up pieces for the side to move
var onDragStart = function(source, piece, position, orientation) {
  if (game.game_over() === true ||
      (game.turn() === 'w' && piece.search(/^b/) !== -1) ||
      (game.turn() === 'b' && piece.search(/^w/) !== -1)) {
    return false;
  }
};

var onDrop = function(source, target) {
  // see if the move is legal
  var move = game.move({
    from: source,
    to: target,
    promotion: 'q' // NOTE: always promote to a queen for example simplicity
  });

  // illegal move
  if (move === null) return 'snapback';

  updateStatus();
};

// update the board position after the piece snap 
// for castling, en passant, pawn promotion
var onSnapEnd = function() {
  board1.position(game.fen());
};

var updateStatus = function() {
  var status = '';

  var moveColor = 'White';
  if (game.turn() === 'b') {
    moveColor = 'Black';
  }

  // checkmate?
  if (game.in_checkmate() === true) {
    status = 'Game over, ' + moveColor + ' is in checkmate.';
  }

  // draw?
  else if (game.in_draw() === true) {
    status = 'Game over, drawn position';
  }

  // game still on
  else {
    status = moveColor + ' to move';

    // check?
    if (game.in_check() === true) {
      status += ', ' + moveColor + ' is in check';
    }
  }

  statusEl.html(status);
  fenEl.html(game.fen());
  // pgnEl.html(game.pgn());
};

var cfg = {
  draggable: true,
  position: 'start',
  onDragStart: onDragStart,
  onDrop: onDrop,
  onSnapEnd: onSnapEnd,
  onChange: onChange
};
board1 = ChessBoard('board1', cfg);

updateStatus();