// Define a grammar called Hello
grammar Rv;

oneRaceReadPart  : 'Race:' '<' classDeclare ':' returnType methodSig '>' '|' ID '=' '<' classDeclare ':' variableType variable '>' '|' lineNumber;
oneRaceWritePart : 'Race:' '<' classDeclare ':' returnType methodSig '>' '|' '<' classDeclare ':' variableType variable '>' '=' ID '|' lineNumber;

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