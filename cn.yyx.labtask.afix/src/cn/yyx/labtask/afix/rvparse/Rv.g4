// Define a grammar called Hello
grammar Rv;

oneRaceReadPart  : '<' classDeclare ':' returnType methodSig '>' '|' ID '=' '<' classDeclare ':' variableType variable '>' '|' lineNumber;
oneRaceWritePart : '<' classDeclare ':' returnType methodSig '>' '|' '<' classDeclare ':' variableType variable '>' '=' ID '|' lineNumber;

oneRacePart : oneRaceReadPart | oneRaceWritePart;

oneRace : 'Race:' oneRacePart '-' oneRacePart;

classDeclare : ID;

variableType : ID;
variable : ID;

returnType : ID;
methodSig : ID;

lineNumber : NUMBER;

NUMBER : [0-9]+;

ID : [A-Za-z0-9_\$\[\]\./\(\),]+;

WS : [ \t\r\n]+ -> skip;