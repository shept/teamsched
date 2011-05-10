
//----- global vars with JSTL access need to be defined in jsp ----
//	var xmlDoc = "${xml}";
//	var selections = ${selections};
//	var xmlNames = ${xmlNames};
//	var cssErrorClass = "selectBoxError";
//	var cssOkClass = "selectBoxOk";
//	var hint = "--- please select ---";
//	var hintKey = "";		// set no selection when hint clicked
//	var hintKey = "-1";		// set '-1' when hint clicked
//	var boxPrefix = "select";
//	var hidePrefix = "sheets";
//	var hidePostfix = ".issueId";
//	var minColIdx = 1;	// 0,1,... the first row that return a valid index

function isMSIExplorer()
{
	return (navigator.appName.indexOf("Microsoft") >= 0)
}

function getColumn(boxId)
{
	var index1 = boxId.lastIndexOf("[");
	var index2 = boxId.lastIndexOf("]");
	return(boxId.substring(index1+1, index2));
}

function getRow(boxId)
{
	var index1 = boxId.indexOf("[");
	var index2 = boxId.indexOf("]");
	return(boxId.substring(index1+1, index2));
}

function nextBoxId(currentBoxId)
{
	var index = currentBoxId.lastIndexOf("[");
	var count = getColumn(currentBoxId);
	var prefix = currentBoxId.substring(0, index+1);
	count++;
	return prefix+count+"]"
}

function previousBoxId(currentBoxId)
{
	var index = currentBoxId.lastIndexOf("[");
	var count = getColumn(currentBoxId);
	var prefix = currentBoxId.substring(0, index+1);
	count--;
	return prefix+count+"]"
}

function loadXML()
{
	var xml;
	
	if(window.ActiveXObject)
	{
		xml = new ActiveXObject("Microsoft.XMLDOM");
		xml.async=false;
		xml.loadXML(xmlDoc);
	}
	else if (document.implementation && document.implementation.createDocument)
	{
		var parser = new DOMParser();
		xml = parser.parseFromString(xmlDoc, "text/xml");
	}
	else
		{alert("Kann XML-String nicht laden");}
	
	return xml;
}

function getElements(currentBox)
{
	var xpath = "//"+xmlNames[getColumn(currentBox.id)]+"[@id="+currentBox.options[currentBox.options.selectedIndex].value+"]";
	var xml = loadXML();
	var elements = [];
	if (window.ActiveXObject) {
		elements = xml.selectNodes(xpath);
	} else if (window.XPathEvaluator) {
		var ev = new XPathEvaluator();
		var iterator = ev.evaluate(
			xpath,
			xml,
			null,
			XPathResult.ORDERED_NODE_ITERATOR_TYPE,
			null);
		var knoten;
		while (knoten = iterator.iterateNext()) {
			elements[elements.length] = knoten;
		}
	} else alert("Kann XPath nicht auswerten");
	return elements;
}

function getHiddenName(id)
{
	if (hidePrefix == "" || null == hidePrefix) {
		return hidePostfix;
	} else {
		return hidePrefix+"["+getRow(id)+"]"+hidePostfix;
	}
}


function handleCurrentBox(currentBox)
{
	var id = currentBox.id;
	var cssClass = "class";
	if (isMSIExplorer()) 
	{
		cssClass = "className";
	}
	if(currentBox.options.selectedIndex != -1)
	{
		// hidden = currentBox.form[hidePrefix+"["+getRow(id)+"]"+hidePostfix];
		var hiddenName = getHiddenName(id);
		var hidden = currentBox.form[hiddenName];

		var value = currentBox.options[currentBox.options.selectedIndex].value;
		if ((value == "") && (getColumn(id) != 0))
		{
			currentBox.setAttribute(cssClass, cssErrorClass);
		}
		else
		{
			currentBox.setAttribute(cssClass, cssOkClass);
			// Ignore value settings which are lower than the minimum allowed column
			if (getColumn(id) >= minColIdx) {
				hidden.value = value;
			} else {
				hidden.value = "";
			}
		}
	}
	else
	{
			currentBox.setAttribute(cssClass, cssOkClass);
	}
}

function handleNextBox(currentBox, nextBox, usePreSelection, showEmptyBox)
{
	var i;
	var nextId = nextBoxId(currentBox.id);
	// delete old content
	for (i=nextBox.length; i >= 0; i--) 
	{
		nextBox.options[i] = null;
	}
	if ((currentBox.options.selectedIndex == -1) || 
		(currentBox.options[currentBox.options.selectedIndex].value == ""))
	{	// nothing selected so we disable the next box
		nextBox.disabled=true;
	}
	else
	{	
        var elements = getElements(currentBox);
	
		// add content
		if (elements.length > 0 ) {
		  var childs = elements[0].childNodes;
		  if (childs.length <= 0)
		  {	// no content so disable next box
			nextBox.disabled=true;
		  }
		  else
		  {
			nextBox.disabled= currentBox.disabled;
			if ( showEmptyBox || (!usePreSelection) || (selections[getRow(nextId)][getColumn(nextId)] == null) )
			{
				nextBox.options[nextBox.length] = new Option(hint, hintKey, false, true);
			}
			
			for (i=0; i < childs.length; i++) 
			{
				var optKey = childs[i].getAttribute("id");
				if (usePreSelection)
				{
					// get and set selection from parent array 
					nextBox.options[nextBox.length] = new Option(childs[i].getAttribute("name"), optKey, (optKey == selections[getRow(nextId)][getColumn(nextId)]), (optKey == selections[getRow(nextId)][getColumn(nextId)]));
				}
				else
				{
					// no preselection
					nextBox.options[nextBox.length] = new Option(childs[i].getAttribute("name"), optKey, false, false);
				}
			}
		  }
		}
	}
}

function newSelect(currentBox, usePreSelection, showEmptyBox)
{
	var i;
	handleCurrentBox(currentBox);
	var nextId = nextBoxId(currentBox.id);
	if (currentBox.form[nextId] != null)
	{	// there is another box to fetch
		var nextBox = currentBox.form[nextId];
		handleNextBox(currentBox, nextBox, usePreSelection, showEmptyBox);
		// fetch boxes recursively
		newSelect(nextBox, usePreSelection, showEmptyBox);
	}	
}

function loadSelect(myForm, usePreselection, showEmptyBox)
{
	var i; 
	var ii;
	var xml = loadXML(xmlDoc);
	var root = xml.documentElement;
	var childs = root.childNodes;
	
	for (i=0; i < selections.length; i++)
	{
		var boxId = boxPrefix+"["+i+"][0]";
		var box = myForm[boxId];
		if (box != null) {
			if (selections[i][0] == null)
			{
				// no selection, show -- please select --
				box.options[box.length] = new Option(hint, hintKey, false, true);
			}

			// iterate across root childs (entries of first column
			for (ii = 0; ii < childs.length; ii++)
			{
				var optKey = childs[ii].getAttribute("id");
				// need line for IE6 -- problems with safari
				// doesn't hurt firefox and would work without in IE7
				if (isMSIExplorer() && (optKey == selections[i][0])) {
					box.focus();	// because IE 6 problems
				}
				box.options[box.length] = new Option(childs[ii].getAttribute("name"), optKey, (optKey == selections[i][0]), (optKey == selections[i][0]));
			}
			newSelect(box, usePreselection, showEmptyBox);
		}
	}
}

