var btnLoadCountry;
var selectCountries;
var selectCities;
var selectDistricts;
var buttonAddDistrict;
var buttonUpdateDistrict;
var buttonDeleteDistrict;
var labelDistrictName;
var fieldDistrictName;

$(document).ready(function() {
    btnLoadCountry = $("#btnLoadCountry");
    selectCountries = $("#selectCountries");
    selectCities = $("#selectCities");
    selectDistricts = $("#selectDistricts");
    buttonAddDistrict = $("#buttonAddDistrict");
    buttonUpdateDistrict = $("#buttonUpdateDistrict");
    buttonDeleteDistrict = $("#buttonDeleteDistrict");
    labelDistrictName = $("#labelDistrictName");
    fieldDistrictName = $("#fieldDistrictName");

    buttonAddDistrict.prop("disabled", true);

    btnLoadCountry.click(function () {
       loadCountries();
    });

    selectCountries.on("change", function() {
       loadCities();
    });

    selectCities.on("change", function (){
        loadDistricts();
        fieldDistrictName.val("").focus();
        buttonAddDistrict.prop("disabled", false);
        buttonAddDistrict.val("Add");
    });


    selectDistricts.on("change", function (){
        changeFormDistrictToSelectedDistrict();

    });


    buttonAddDistrict.click(function() {
        if (buttonAddDistrict.val() == "Add") {
            addNewDistrict();
        } else {
            changeDistrictToNewDistrict();
        }
    });

    buttonUpdateDistrict.click(function() {
        updateExistDistrict();
    });

    buttonDeleteDistrict.click(function() {
        deleteExistDistrict();
    });

});

function addNewDistrict() {
    if (!validateDistrict()) {

        return;
    }
    url = contextPath + "districts/save";
    districtName = fieldDistrictName.val();

    selectedCity = $("#selectCities option:selected");
    cityId = selectCities.val();
    cityName = selectCities.text();

    jsonData = {name: districtName, city: {id: cityId, name: cityName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(districtId) {
        selectNewlyAddedDistrict(districtId, districtName);
        showToastMessasge("The new district has been added");
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });

}

function updateExistDistrict() {
    if (!validateDistrict()) {
        return;
    }
    url = contextPath + "districts/save";

    districtId = selectDistricts.val();
    districtName = fieldDistrictName.val();

    selectDistricts = $("#selectDistricts option:selected");
    cityId = selectCities.val();
    cityName = selectCities.text();

    jsonData = {id: districtId, name: districtName, city: {id: cityId, name: cityName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(cityId) {
        $("#selectDistricts option:selected").text(districtName);
        showToastMessasge("The district has been updated");
        changeDistrictToNewDistrict();
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });

}

function validateDistrict() {
    formDistrict = document.getElementById("formDistrict");

    if (!formDistrict.checkValidity()) {
        formDistrict.reportValidity();
        return false;
    }
    return true;
}

function deleteExistDistrict() {
    districtId = selectDistricts.val();

    url = contextPath + "districts/delete/" + districtId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function() {
        $("#selectDistricts option[value='"+ districtId +"']").remove();
        changeDistrictToNewDistrict();
        showToastMessasge("The district has been deleted");
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });

}

function selectNewlyAddedDistrict(districtId, districtName) {
    $("<option>").val(districtId).text(districtName).appendTo(selectDistricts);

    $("#selectDistricts option[value='"+ districtId +"']").prop("selected", true);

    fieldDistrictName.val("").focus();
}


function changeDistrictToNewDistrict() {
    labelDistrictName.text("District Name:");

    buttonUpdateDistrict.prop("disabled", true);
    buttonDeleteDistrict.prop("disabled", true);
}

function changeFormDistrictToSelectedDistrict() {
    buttonAddDistrict.prop("value", "New");
    buttonUpdateDistrict.prop("disabled", false);
    buttonDeleteDistrict.prop("disabled", false);

    labelDistrictName.text("Selected District:");

    selecteddictrictName = $("#selectDistricts option:selected").text();
    fieldDistrictName.val(selecteddictrictName);

}

function loadCities() {
    selectedCountry = $("#selectCountries option:selected");
    countryId = selectedCountry.val();
    url = contextPath + "cities/list_by_country/" + countryId;

    $.get(url, function(responseJSON) {
        selectCities.empty();

        $.each(responseJSON, function (index, city) {
            $("<option>").val(city.id).text(city.name).appendTo(selectCities);
        });

    }).done(function () {
        changeDistrictToNewDistrict();
        showToastMessasge("All cities have been loaded for country " + selectedCountry.text());
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });
}

function loadCountries() {
    url = contextPath + "countries/list";
    selectCities.empty();
    selectDistricts.empty();
    fieldDistrictName.val("");
    labelDistrictName.text("District Name:");
    buttonAddDistrict.prop("disabled", true);
    buttonUpdateDistrict.prop("disabled", true);
    buttonDeleteDistrict.prop("disabled", true);
    $.get(url, function(responseJSON) {
        selectCountries.empty();

        $.each(responseJSON, function (index, country) {
            $("<option>").val(country.id).text(country.name).appendTo(selectCountries);
        });

    }).done(function () {
        btnLoadCountry.val("Refresh Country List");
        showToastMessasge("All countries have been loaded");
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });
}

function loadDistricts() {
    selectedCity = $("#selectCities option:selected");
    cityId = selectCities.val();
    url = contextPath + "districts/list_by_city/" + cityId;
    $.get(url, function(responseJSON) {
        selectDistricts.empty();
        $.each(responseJSON, function (index, ward) {
            $("<option>").val(ward.id).text(ward.name).appendTo(selectDistricts);
        });

    }).done(function () {
        changeDistrictToNewDistrict();
        showToastMessasge("All wards have been loaded for city " + selectedCountry.text());
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });
}