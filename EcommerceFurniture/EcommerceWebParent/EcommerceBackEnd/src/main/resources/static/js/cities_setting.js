var buttonLoadForCities;
var dropDownCountriesForCities;
var dropDownCities;
var buttonAddCity;
var buttonUpdateCity;
var buttonDeleteCity;
var labelCityName;
var fieldCityName;

$(document).ready(function() {
    buttonLoadForCities = $("#buttonLoadCountriesForCities");
    dropDownCountriesForCities = $("#dropDownCountriesForCities");
    dropDownCities = $("#dropDownCities");
    buttonAddCity = $("#buttonAddCity");
    buttonUpdateCity = $("#buttonUpdateCity");
    buttonDeleteCity = $("#buttonDeleteCity");
    labelCityName = $("#labelCityName");
    fieldCityName = $("#fieldCityName");

    buttonAddCity.prop("disabled", true);

    buttonLoadForCities.click(function () {
       loadCountriesForCities();
    });

    dropDownCountriesForCities.on("change", function() {
       loadCitiesForCountry();

        buttonAddCity.prop("disabled", false);
    });

    dropDownCities.on("change", function (){
        changeFormCityToSelectedCity();
    });

    buttonAddCity.click(function() {
        if (buttonAddCity.val() == "Add") {
            addCity();
        } else {
            changeFromCityToNewCity();
        }
    });

    buttonUpdateCity.click(function() {
        updateCity();
    });

    buttonDeleteCity.click(function() {
        deleteCity();
    });

});

function addCity() {
    if (!validateCity()) {
        return;
    }
    url = contextPath + "cities/save";
    cityName = fieldCityName.val();

    selectedCountry = $("#dropDownCountriesForCities option:selected");
    countryId = selectedCountry.val();
    countryName = selectedCountry.text();

    jsonData = {name: cityName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(cityId) {
        selectNewlyAddedCity(cityId, cityName);
        showToastMessasge("The new city has been added");
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });

}

function updateCity() {
    if (!validateCity()) {
        return;
    }
    url = contextPath + "cities/save";

    cityId = dropDownCities.val();
    cityName = fieldCityName.val();

    selectedCountry = $("#dropDownCountriesForCities option:selected");
    countryId = selectedCountry.val();
    countryName = selectedCountry.text();

    jsonData = {id: cityId, name: cityName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(cityId) {
        $("#dropDownCities option:selected").text(cityName);
        showToastMessasge("The city has been updated");
        changeFromCityToNewCity();
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });

}

function validateCity() {
    formCity = document.getElementById("formCity");

    if (!formCity.checkValidity()) {
        formCity.reportValidity();
        return false;
    }
    return true;
}

function deleteCity() {
    cityId = dropDownCities.val();

    url = contextPath + "cities/delete/" + cityId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function() {
        $("#dropDownCities option[value='"+ cityId +"']").remove();
        changeFromCityToNewCity();
        showToastMessasge("The city has been deleted");
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });

}

function selectNewlyAddedCity(districtId, districtName) {
    $("<option>").val(districtId).text(districtName).appendTo(dropDownCities);

    $("#dropDownCities option[value='"+ districtId +"']").prop("selected", true);

    fieldCityName.val("").focus();
}


function changeFromCityToNewCity() {
    buttonAddCity.val("Add");
    labelCityName.text("City/Province Name:");

    buttonUpdateCity.prop("disabled", true);
    buttonDeleteCity.prop("disabled", true);

    fieldCityName.val("").focus();

}

function changeFormCityToSelectedCity() {
    buttonAddCity.prop("value", "New");
    buttonUpdateCity.prop("disabled", false);
    buttonDeleteCity.prop("disabled", false);

    labelCityName.text("Selected City/Province:");

    selectedcityName = $("#dropDownCities option:selected").text();
    fieldCityName.val(selectedcityName);

}

function loadCitiesForCountry() {
    selectedCountry = $("#dropDownCountriesForCities option:selected");
    countryId = selectedCountry.val();
    url = contextPath + "cities/list_by_country/" + countryId;

    $.get(url, function(responseJSON) {
        dropDownCities.empty();

        $.each(responseJSON, function (index, city) {
            $("<option>").val(city.id).text(city.name).appendTo(dropDownCities);
        });

    }).done(function () {
        changeFromCityToNewCity();
        showToastMessasge("All cities have been loaded for country " + selectedCountry.text());
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });
}

function loadCountriesForCities() {

    url = contextPath + "countries/list";
    dropDownCities.empty();
    fieldCityName.val("");

    buttonAddCity.prop("disabled", true);
    labelCityName.text("City/Province Name:");
    buttonUpdateCity.prop("disabled", true);
    buttonDeleteCity.prop("disabled", true);

    $.get(url, function(responseJSON) {
        dropDownCountriesForCities.empty();

        $.each(responseJSON, function (index, country) {
            $("<option>").val(country.id).text(country.name).appendTo(dropDownCountriesForCities);
        });

    }).done(function () {
        buttonLoadForCities.val("Refresh Country List");
        showToastMessasge("All countries have been loaded");
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });
}