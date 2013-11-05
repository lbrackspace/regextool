A simple tool to test Java, Perl and PCRE regex validation.

Compile: mvn -e clean compile assembly:single
Compile C: make; make install
           Then place on your path.

Run: java -jar pregex-{version}-jar-with-dependencies.jar

Usage: 

Validates regex against Java, Perl and PCRE libraries.

help: Returns this help menu.
Example usage: <regex=^*$> <mstr=200>
type: Use 'j' or 'p' for Java or Perl regex validation or 'pcre' for PCRE validation. Defaults to Perl validation
regex: The regex to test with. 
mstr: The string to match against. 
You can also run this program in interactive mode by not supplying any arguments.
Interactive usage: $: <type> <regex> <mstr>
Example: $: p ^*$ 200 :: type and regex are required.
While in interactive mode type 'help' to display this menu or 'exit' to quit.

PCRE validation uses libpcre and will make a call to the compiled PCRE C application which inturn uses libpcre to validate the regex.
Java will validate using the current Javax Pattern class.
Perl will use JREGEX which is a application built using Perl 5.6 syntax and rules.
     (Caution: there are additional rules allowed with this library that may not be accepted in regexp of Perl.)

WARNING: not compiling and placing the C 'pcrevalidate' application in your path without supplying a 'type' you will receive errors!
