/* Tokenizer for CSharp code */

var tokenizeC = (function() {
  // Advance the stream until the given character (not preceded by a
  // backslash) is encountered, or the end of the line is reached.
  function nextUntilUnescaped(source, end) {
    var escaped = false;
    var next;
    while (!source.endOfLine()) {
      var next = source.next();
      if (next == end && !escaped)
        return false;
      escaped = !escaped && next == "\\";
    }
    return escaped;
  }

  // A map of JavaScript's keywords. The a/b/c keyword distinction is
  // very rough, but it gives the parser enough information to parse
  // correct code correctly (we don't care that much how we parse
  // incorrect code). The style information included in these objects
  // is used by the highlighter to pick the correct CSS style for a
  // token.
  var keywords = function(){
    function result(type, style){
      return {type: type, style: "c-" + style};
    }
    // keywords that take a parenthised expression, and then a
    // statement (if)
    var keywordA = result("keyword a", "keyword");
    // keywords that take just a statement (else)
    var keywordB = result("keyword b", "keyword");
    // keywords that optionally take an expression, and form a
    // statement (return)
    var keywordC = result("keyword c", "keyword");
    var operator = result("operator", "keyword");
    var atom = result("atom", "atom");
    // just a keyword with no indentation implications
    var keywordD = result("keyword d", "keyword"); 
    
    return {
      "if": keywordA, "while": keywordA, "sizeof": keywordA,
      "else": keywordB, "do": keywordB,
      "return": keywordC, "break": keywordC, "continue": keywordC, "goto": keywordC,
	  
      "struct": operator, "typedef": operator, "union": operator,
	  
      "for": result("for", "keyword"), "switch": result("switch", "keyword"),
      "case": result("case", "keyword"), "default": result("default", "keyword"),
	  
      "true": atom, "false": atom, "null": atom,
	  
      "extern": keywordD, "enum": keywordD, "auto": keywordD, "volatile": keywordD, 
      "static": keywordD, "const": keywordD, "register": keywordD,
      
      "int": keywordD, "double": keywordD, "long": keywordD, "bool": keywordD, "char": keywordD, 
      "void": keywordD, "float": keywordD, "short": keywordD, "unsigned": keywordD, "signed": keywordD
    };
  }();

  // Some helper regexps
  var isOperatorChar = /[+\-*&%=<>!?|]/;
  var isHexDigit = /[0-9A-Fa-f]/;
  var isWordChar = /[\w\$_]/;

  // Wrapper around jsToken that helps maintain parser state (whether
  // we are inside of a multi-line comment and whether the next token
  // could be a regular expression).
  function jsTokenState(inside, regexp) {
    return function(source, setState) {
      var newInside = inside;
      var type = jsToken(inside, regexp, source, function(c) {newInside = c;});
      var newRegexp = type.type == "operator" || type.type == "keyword c" || type.type.match(/^[\[{}\(,;:]$/);
      if (newRegexp != regexp || newInside != inside)
        setState(jsTokenState(newInside, newRegexp));
      return type;
    };
  }

  // The token reader, inteded to be used by the tokenizer from
  // tokenize.js (through jsTokenState). Advances the source stream
  // over a token, and returns an object containing the type and style
  // of that token.
  function jsToken(inside, regexp, source, setInside) {
    function readHexNumber(){
      source.next(); // skip the 'x'
      source.nextWhileMatches(isHexDigit);
      return {type: "number", style: "c-atom"};
    }

    function readNumber() {
      source.nextWhileMatches(/[0-9]/);
      if (source.equals(".")){
        source.next();
        source.nextWhileMatches(/[0-9]/);
      }
      if (source.equals("e") || source.equals("E")){
        source.next();
        if (source.equals("-"))
          source.next();
        source.nextWhileMatches(/[0-9]/);
      }
      return {type: "number", style: "c-atom"};
    }
    // Read a word, look it up in keywords. If not found, it is a
    // variable, otherwise it is a keyword of the type found.
    function readWord() {
      source.nextWhileMatches(isWordChar);
      var word = source.get();
      var known = keywords.hasOwnProperty(word) && keywords.propertyIsEnumerable(word) && keywords[word];
      return known ? {type: known.type, style: known.style, content: word} :
      {type: "variable", style: "c-variable", content: word};
    }
    function readRegexp() {
      nextUntilUnescaped(source, "/");
      source.nextWhileMatches(/[gi]/);
      return {type: "regexp", style: "c-string"};
    }
    // Mutli-line comments are tricky. We want to return the newlines
    // embedded in them as regular newline tokens, and then continue
    // returning a comment token for every line of the comment. So
    // some state has to be saved (inside) to indicate whether we are
    // inside a /* */ sequence.
    function readMultilineComment(start){
      var newInside = "/*";
      var maybeEnd = (start == "*");
      while (true) {
        if (source.endOfLine())
          break;
        var next = source.next();
        if (next == "/" && maybeEnd){
          newInside = null;
          break;
        }
        maybeEnd = (next == "*");
      }
      setInside(newInside);
      return {type: "comment", style: "c-comment"};
    }
    function readOperator() {
      source.nextWhileMatches(isOperatorChar);
      return {type: "operator", style: "c-operator"};
    }
    function readString(quote) {
      var endBackSlash = nextUntilUnescaped(source, quote);
      setInside(endBackSlash ? quote : null);
      return {type: "string", style: "c-string"};
    }

    // Fetch the next token. Dispatches on first character in the
    // stream, or first two characters when the first is a slash.
    if (inside == "\"" || inside == "'")
      return readString(inside);
    var ch = source.next();
    if (inside == "/*")
      return readMultilineComment(ch);
    else if (ch == "\"" || ch == "'")
      return readString(ch);
    // with punctuation, the type of the token is the symbol itself
    else if (/[\[\]{}\(\),;\:\.]/.test(ch))
      return {type: ch, style: "c-punctuation"};
    else if (ch == "0" && (source.equals("x") || source.equals("X")))
      return readHexNumber();
    else if (/[0-9]/.test(ch))
      return readNumber();
    else if (ch == "/"){
      if (source.equals("*"))
      { source.next(); return readMultilineComment(ch); }
      else if (source.equals("/"))
      { nextUntilUnescaped(source, null); return {type: "comment", style: "c-comment"};}
      else if (regexp)
        return readRegexp();
      else
        return readOperator();
    }
    else if (ch == "#") {
        nextUntilUnescaped(source, null); return {type: "import", style: "c-import"};
    }
    else if (isOperatorChar.test(ch))
      return readOperator();
    else
      return readWord();
  }

  // The external interface to the tokenizer.
  return function(source, startState) {
    return tokenizer(source, startState || jsTokenState(false, true));
  };
})();
