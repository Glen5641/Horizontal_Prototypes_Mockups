//******************************************************************************

// Modify this file to generate HTML for the accordion items on your main page.
// See https://www.w3schools.com/js/ for reference and help.

//******************************************************************************

//******************************************************************************

// Inlined version of our XML data for use by loadXMLDataInline().
// See data.xml for the same XML in a format that is easier to read.

var inlineXML = "<DATA><ITEM><ID>0001</ID><IMDB>tt0338013</IMDB><TITLE>Eternal Sunshine Of The Spotless Mind</TITLE><GENRE>Science Fiction</GENRE><DESCRIPTION>A couple undergo a procedure to erase each other from their memories when their relationship turns sour, but it is only through the process of loss that they discover what they had to begin with.</DESCRIPTION><YEAR>2004</YEAR><DURATION>1:47h</DURATION></ITEM><ITEM><ID>0002</ID><IMDB>tt0241527</IMDB><TITLE>Harry Potter And The Sorcerer's Stone</TITLE><GENRE>Fantasy</GENRE><DESCRIPTION>Rescued from the outrageous neglect of his aunt and uncle, a young boy with a great destiny proves his worth while attending Hogwarts School of Witchcraft and Wizardry.</DESCRIPTION><YEAR>2001</YEAR><DURATION>2:32h</DURATION></ITEM><ITEM><ID>0003</ID><IMDB>tt0446029</IMDB><TITLE>Scott Pilgrim Vs. The World</TITLE><GENRE>Fantasy</GENRE><DESCRIPTION>Scott Pilgrim must defeat his new girlfriend's seven evil exes in order to win her heart.</DESCRIPTION><YEAR>2010</YEAR><DURATION>1:52h</DURATION></ITEM><ITEM><ID>0004</ID><IMDB>tt0379786</IMDB><TITLE>Serenity</TITLE><GENRE>Science Fiction</GENRE><DESCRIPTION>The crew of the ship Serenity tries to evade an assassin sent to recapture one of their number, who is telepathic.</DESCRIPTION><YEAR>2005</YEAR><DURATION>1:59h</DURATION></ITEM></DATA>";

//******************************************************************************

// We would normally use an HTTP request to fetch the XML data from a file on a
// remote server. Access control disallows that on local files. As a workaround,
// we inline the XML as a JavaScript string that we can parse directly.

// Function to fetch XML over HTTP. We're NOT using this one, but worth seeing.
// See https://www.w3schools.com/xml/xml_http.asp for more information.
function loadXMLDataHttp() {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      loadXMLData(this.responseXML);
    }
  };
  xhttp.open("GET", "data.xml", true);
  xhttp.send();
}

// Function to fetch XML inline. We ARE using this one.
// See https://www.w3schools.com/xml/xml_parser.asp for more information.
function loadXMLDataInline() {
  var parser = new DOMParser;
  var xml = parser.parseFromString(inlineXML, "text/xml");
  loadXMLData(xml);
}

// Function to process XML into HTML and event handlers.
function loadXMLData(xml) {
  var items = xml.getElementsByTagName("ITEM");
  var years = xml.getElementsByTagName("YEAR");
  var genres = xml.getElementsByTagName("GENRE");

  var page = window.location.pathname.split("/").pop();
  if(page == "main.html"){
    insertAccordion(items);
    activateAccordion(items);
    insertGenreFilter(genres);
    insertYearFilter(years);
    activateYearFilter();
    activateGenreFilter();
  }
  else {
    var parts = page.split(".");
    insertMovieData(items, parts[0]);
  }
}

// Convenience function to extract the value of a tag from the XML.
// See https://www.w3schools.com/xml/ for more information.
function getItemTagValue(item, tag) {
  return item.getElementsByTagName(tag)[0].childNodes[0].nodeValue;
}



function insertMovieData(items, id) {
  var s = "";

  // Loop through the ITEMs in the data
  var i = 0;
  var itemID;
  for (i = 0; i < items.length; i++) {
    itemID = getItemTagValue(items[i], "ID");
    if(id == itemID)
      break;
  }

  var itemTitle = getItemTagValue(items[i], "TITLE");
  var itemYear = getItemTagValue(items[i], "YEAR");
  var itemID = getItemTagValue(items[i], "ID");
  var itemURL = getItemTagValue(items[i], "IMDB");
  var itemGenre = getItemTagValue(items[i], "GENRE");
  var itemDescription = getItemTagValue(items[i], "DESCRIPTION");
  var itemDuration = getItemTagValue(items[i], "DURATION");

  s += "<div class=\"leftData\">";
  s += "<h5>" + itemTitle + "</h5>";
  s += "<h1>" + itemGenre + "</h1>";
  s += "<p>" + itemDescription + "</p>";
  s += "<h2>Year Released: " + itemYear + "</h2>";
  s += "<h3>Duration: " + itemDuration + "</h3>";
  s += "<a class=\"returnhome\" href=\"main.html\">Return Home</a>";
  s += "<a class=\"checkout\" href=\"https://www.imdb.com/title/" + itemURL + "/\">Check out on IMDB</a>";
  s += "</div>";

  s += "<div class=\"rightData\">";
  s += "<a href=\"https://www.imdb.com/title/" + itemURL + "/\">"
  s += "<img src=\"images/" + itemID + ".jpg\" class=\"movieImg\"></img>";
  s += "</a>";
  s += "</div>";
  s += "<br/>";

  // Insert the built HTML into the id=accordion <div> in main.html.
  document.getElementById("movie-data").innerHTML = s;
}


// Function to build HTML from the XML data and insert it into the id=accordion
// <div> element in main.html. Creates a pair of <div>s for each data item: one
// for a summary and one for details.
function insertAccordion(items) {
  var s = "";

  // Loop through the ITEMs in the data
  for (var i = 0; i < items.length; i++) {
    var itemID = getItemTagValue(items[i], "ID");
    var overallID = "overall-" + itemID;
    var summaryID = "summary-" + itemID;
    var detailsID = "details-" + itemID;
    var itemYear = "year" + getItemTagValue(items[i], "YEAR");
    var itemGenre = "genre" + getItemTagValue(items[i], "GENRE");

    // Open a <div> block for the item overall.
    // (Use #itemID in an href to link to this <div> on the main page.)
    s += "<div class=\"item-overall " + itemGenre + " " + itemYear + "\" id=\"" + overallID + "\">";

    // Build a <div> block for the item summary.
    s += "<div class=\"item-summary\" id=\"" + summaryID + "\">";
    s += buildItemSummary(items[i]);
    s += "</div>";

    // Build a <div> block for the item details.
    s += "<div class=\"item-details\" id=\"" + detailsID + "\">";
    s += buildItemDetails(items[i]);
    s += "</div>";

    // Close the <div> block for the item overall.
    s += "</div>";
  }

  // Insert the built HTML into the id=accordion <div> in main.html.
  document.getElementById("items").innerHTML = s;
}

// After inserting the <div>s, add a click event listener to each summary to
// show/hide the corresponding details. Also add hooks to expand/collapse all.
function activateAccordion(items)  {

  // Again, loop through the ITEMs in the data
  for (var i = 0; i < items.length; i++) {
    var itemID = getItemTagValue(items[i], "ID");
    var summaryID = "summary-" + itemID;

    // Add a click listener to the item summary. When clicked, toggle the
    // sibling element, which we've set up to be the corresponding details.
    $("#" + summaryID).click(function(){
      for(var j = 0; j < items.length; j++){
        $("#summary-" + getItemTagValue(items[j], "ID")).next().slideUp();
        if($(this).is($("#summary-" + getItemTagValue(items[j], "ID")))){
          $(this).css("font-size", "2vw");
          $(this).css("background-color", "#ddd");
        } else {
          $("#summary-" + getItemTagValue(items[j], "ID")).css('font-size', '1.4vw');
          $("#summary-" + getItemTagValue(items[j], "ID")).css('background-color', '#bbb');
        }
      }
      $(this).next().slideDown();
    });
    if(i == 0) {
      $("#" + summaryID).next().slideDown();
      $("#" + summaryID).css("font-size", "2vw");
      $("#" + summaryID).css("background-color", "#ddd");
    } else {
      $("#" + summaryID).next().slideUp();
    }
  }

  // Event listeners for elements to expand/collapse all items.
  $("#show-all").click(function(){ $("[id^='details-']").slideDown(); });
  $("#hide-all").click(function(){ $("[id^='details-']").slideUp(); });
}

// Function to build HTML to display the summary of a data item. Complete this
// to implement the "collapsed items" aspect of the main page specification.
function buildItemSummary(item) {

  var s = "";

  // Here is an example to get you started...
  var itemTitle = getItemTagValue(item, "TITLE");
  var itemYear = getItemTagValue(item, "YEAR");

  // ...and append it, followed by a line break.
  s += itemTitle + " (" + itemYear + ")<br\>";

  return s;
}

// Function to build HTML to display the details of a data item. Complete this
// to implement the "expanded items" aspect of the main page specification.
function buildItemDetails(item)
{
  var s = "";

  // ...read some values from the XML...
  var itemID = getItemTagValue(item, "ID");
  var itemURL = getItemTagValue(item, "IMDB");
  var itemGenre = getItemTagValue(item, "GENRE");
  var itemDescription = getItemTagValue(item, "DESCRIPTION");
  var itemDuration = getItemTagValue(item, "DURATION");

  // Build String
  s += "<div style=\"display: grid; margin-bottom: 0;\">";
    s += "<div style=\"grid-row-start: 1; grid-row-end: 2; grid-column-start: 1; grid-column-end: 2;\"><h1>" + itemGenre + "</h1>";
      s += "<p>" + itemDescription + "</p>";
      s += "<h2>" + itemDuration + "</h2></div>";
    s += "<div style=\"grid-row-start: 1; grid-row-end: 2; grid-column-start: 2; grid-column-end: 3;\">";
      s += "<a href=\"" + itemID + ".html\"><img src=\"images/" + itemID + ".jpg\"></img>" + "</a></div>" + "<br/>";
  s += "</div>";

  return s;
}

// Function to build HTML from the XML data and insert it into the id=filter
// <div> element in main.html. Creates a <form> with checkbox <input>s for each
// unique year in the data.
function insertYearFilter(years) {

  var s = "";
  var y = new Array();

  for (var i = 0; i < years.length; i++) {
    y.push(years[i].childNodes[0].nodeValue);
  }

    // Sort and remove duplicates
  y = y.sort().filter(function(v, i, a) {
    return ((i == a.length - 1) || (a[i + 1] != v));
  });

  s += "<form>";

  // Loop through the (sorted, unique) year values extracted from the data
  for (var i = 0; i < y.length; i++) {
    // Build a <input> checkbox for the year.
    s += "<input type=\"checkbox\" value=\"year" + y[i] +
      "\" checked> " + y[i] + "<br/>";
  }

  s += "</form>";

  // Insert the built HTML into the id=accordion <div> in main.html.
  document.getElementById("yearFilter").innerHTML = s;
}

// Function to build HTML from the XML data and insert it into the id=filter
// <div> element in main.html. Creates a <form> with checkbox <input>s for each
// unique year in the data.
function insertGenreFilter(genres) {

  var s = "";
  var y = new Array();

  for (var i = 0; i < genres.length; i++) {
    y.push(genres[i].childNodes[0].nodeValue);
  }

    // Sort and remove duplicates
  y = y.sort().filter(function(v, i, a) {
    return ((i == a.length - 1) || (a[i + 1] != v));
  });

  s += "<form>";

  // Loop through the (sorted, unique) year values extracted from the data
  for (var i = 0; i < y.length; i++) {
    // Build a <input> checkbox for the year.
    s += "<input type=\"checkbox\" value=\"genre" + y[i] +
      "\" checked> " + y[i] + "<br/>";
  }

  s += "</form>";

  // Insert the built HTML into the id=accordion <div> in main.html.
  document.getElementById("genreFilter").innerHTML = s;
}

// After inserting the <form>, add a click event listener to each checkbox to
// filter the corresponding items in the accordion by year..
function activateYearFilter() {

  // Inverted for multiple Filters
  $("div#yearFilter").find("input:checkbox").on("click", function() {
    $("[id^='overall-']").show();
    $("div#genreFilter").find('input:not(:checked)').each(function() {
      $("[class*='" + $(this).val() + "']").hide();
    });
    $("div#yearFilter").find('input:not(:checked)').each(function() {
      $("[class*='" + $(this).val() + "']").hide();
    });
  });
}

// After inserting the <form>, add a click event listener to each checkbox to
// filter the corresponding items in the accordion by year..
function activateGenreFilter() {

  // Inverted for multiple Filters
  $("div#genreFilter").find("input:checkbox").on("click", function() {
    $("[id^='overall-']").show();
    $("div#genreFilter").find('input:not(:checked)').each(function() {
      $("[class*='" + $(this).val() + "']").hide();
    });
    $("div#yearFilter").find('input:not(:checked)').each(function() {
      $("[class*='" + $(this).val() + "']").hide();
    });
  });
}

// Structure Modals to vis when clicked, invis when clicked off
function setModalToggles(){

  // Default
  document.getElementById("genreFilterMenu").style.visibility = "hidden";
  document.getElementById("yearFilterMenu").style.visibility = "hidden";

  // Click on Doc
  $(document).on("click", function () {
    document.getElementById("genreFilterMenu").style.visibility = "hidden";
    document.getElementById("yearFilterMenu").style.visibility = "hidden";
  });

  // Click on Genre Button
  $("#genreButton").on("click", function(event){
    event.stopPropagation();
    document.getElementById("yearFilterMenu").style.visibility = "hidden";
    if(document.getElementById("genreFilterMenu").style.visibility == "hidden")
      document.getElementById("genreFilterMenu").style.visibility = "visible";
    else
      document.getElementById("genreFilterMenu").style.visibility = "hidden";
  });

  // Click on Year Button
  $("#yearButton").on("click", function(event){
    event.stopPropagation();
    document.getElementById("genreFilterMenu").style.visibility = "hidden";
    if(document.getElementById("yearFilterMenu").style.visibility == "hidden")
      document.getElementById("yearFilterMenu").style.visibility = "visible";
    else
      document.getElementById("yearFilterMenu").style.visibility = "hidden";
  });

  // Stop any events in case
  $('div#genreFilter, div#yearFilter').on("click", function(event){
    event.stopPropagation();
  });
}

//******************************************************************************
