# Messages

## Networking
- login
- redirect
- disconnect
- user disconnect
- user reconnected
- surrender
- end game // removed

## Updates
- initial state -> (all model)  [Model m]
- fill cloud [List<List\<StudentColor>> l]

## Moves
- select card [int c]
- move in dining [StudentColor s]
- move in island [StudentColor s, int id]
- move mother nature [int steps]
- select cloud [int pos]

### play character:
- take student in island (1) -> bag [StudentColor take, int id, StudentColor bag]
- resolve island (3)  [int gId]
- no entry tile (5)  [int gId]
- swap 3 students with card (7)  [List\<StudentColor> take, List\<StudentColor> give]
- swap 2 students with dining (10) [List\<StudentColor> take, List\<StudentColor> give]
- take student in dining (11) -> bag [StudentColor, StudentColor bag]
- return students (12) [StudentColor]
