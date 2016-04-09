// Define a grammar called Hello
grammar Rv;

oneRaceReadPart  : RH COMMA LA classDeclare COMMA returnType methodSig RA OR ID ASSIGN RA classDeclare COMMA variableType variable RA OR lineNumber;
oneRaceWritePart : RH COMMA LA classDeclare COMMA returnType methodSig RA OR RA classDeclare COMMA variableType variable RA ASSIGN ID OR lineNumber;

oneRacePart : oneRaceReadPart | oneRaceWritePart;

oneRace : oneRacePart '-' oneRacePart;

classDeclare : ID;

variableType : ID;
variable : ID;

returnType : ID;
methodSig : ID;

lineNumber : Number;

Number : [0_9]+;

ID : [A-Za-z0-9_\$\[\]\./\(\),]+ ;

WS : [ \t\r\n]+ -> skip ;

RH : 'Race';
COMMA : ':';
LA : '<';
RA : '>';
ASSIGN : '=';
OR : '|';