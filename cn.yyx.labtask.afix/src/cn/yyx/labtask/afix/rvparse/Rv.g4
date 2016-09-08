// Define a grammar called Hello
grammar Rv;

oneRaceValue : ID ('<' classDeclare ':' variableType variable '>')?
	| '<' classDeclare ':' variableType variable '>';

oneRaceReadPart  : '<' classDeclare ':' returnType methodSig '>' '|' idOrNumber '=' oneRaceValue '|' lineNumber;
oneRaceWritePart : '<' classDeclare ':' returnType methodSig '>' '|' oneRaceValue '=' idOrNumber '|' lineNumber;

oneRacePart : oneRaceReadPart | oneRaceWritePart;

oneRace : 'Race:' oneRacePart '-' oneRacePart;

classDeclare : ID;

variableType : ID;
variable : ID;

returnType : ID;
methodSig : ID;

lineNumber : NUMBER;

idOrNumber : ID | NUMBER;

NUMBER : [0-9]+;

ID : [A-Za-z_0-9\$\[\]\./\(\),]+;

WS : [ \t\r\n]+ -> skip;