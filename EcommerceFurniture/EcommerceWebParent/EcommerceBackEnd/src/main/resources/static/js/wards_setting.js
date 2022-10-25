var btnLoadAllCountry;
var listCountries;
var listCities;
var listDistricts;
var listWards;
var buttonAddWard;
var buttonUpdateWard;
var buttonDeleteWard;
var labelWardName;
var fieldWardName;

$(document).ready(function() {
    btnLoadAllCountry = $("#btnLoadAllCountry");
    listCountries = $("#listCountries");
    listCities = $("#listCities");
    listDistricts = $("#listDistricts");
    listWards = $("#listWards");
    buttonAddWard = $("#buttonAddWard");
    buttonUpdateWard = $("#buttonUpdateWard");
    buttonDeleteWard = $("#buttonDeleteWard");
    labelWardName = $("#labelWardName");
    fieldWardName = $("#fieldWardName");

    buttonAddWard.prop("disabled", true);

    btnLoadAllCountry.click(function () {
       loadAllCountries();
    });

    listCountries.on("change", function() {
       loadAllCities();
    });

    listCities.on("change", function (){
        loadAllDistricts();
    });

    listDistricts.on("change", function (){
        loadAllWards();
        buttonAddWard.val("Add");
        buttonAddWard.prop("disabled", false);
        fieldWardName.val("").focus();
    });

    listWards.on("change", function (){
        changeFormWardToSelectedWard();
    });


    buttonAddWard.click(function() {
        if (buttonAddWard.val() == "Add") {
            addNewWard();
        } else {
            changeWardToNewWard();
        }
    });

    buttonUpdateWard.click(function() {
        updateExistWard();
    });

    buttonDeleteWard.click(function() {
        deleteExistWard();
    });

});

function addNewWard() {
    if (!validateWard()) {
        return;
    }
    url = contextPath + "wards/save";
    wardName = fieldWardName.val();

    selectedDistrict= $("#listDistricts option:selected");
    districtId = listDistricts.val();
    districtName = listDistricts.text();

    jsonData = {name: wardName, district: {id: districtId, name: districtName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(wardId) {
        selectNewlyAddedWard(wardId, wardName);
        showToastMessasge("The new ward has been added");
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });

}

function updateExistWard() {
    if (!validateWard()) {
        return;
    }
    url = contextPath + "wards/save";

    wardId = listDistricts.val();
    wardName = fieldWardName.val();

    listWards = $("#listWards option:selected");
    districtId = listDistricts.val();
    districtName = listDistricts.text();

    jsonData = {id: wardId, name: wardName, district: {id: districtId, name: districtName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(wardId) {
        $("#listWards option:selected").text(wardName);
        showToastMessasge("The ward has been updated");
        changeWardToNewWard();
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });

}

function validateWard() {
    formWard = document.getElementById("formWard");

    if (!formWard.checkValidity()) {
        formWard.reportValidity();
        return false;
    }
    return true;
}

function deleteExistWard() {
    wardId = listWards.val();

    url = contextPath + "wards/delete/" + wardId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function() {
        $("#listWards option[value='"+ wardId +"']").remove();
        changeWardToNewWard();
        showToastMessasge("The ward has been deleted");
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });

}

function selectNewlyAddedWard(wardId, wardName) {
    $("<option>").val(wardId).text(wardName).appendTo(listWards);

    $("#listWards option[value='"+ wardId +"']").prop("selected", true);

    fieldWardName.val("").focus();
}


function changeWardToNewWard() {

    labelWardName.text("Ward Name:");

    buttonUpdateWard.prop("disabled", true);
    buttonDeleteWard.prop("disabled", true);

}

function changeFormWardToSelectedWard() {
    buttonAddWard.prop("value", "New");
    buttonUpdateWard.prop("disabled", false);
    buttonDeleteWard.prop("disabled", false);

    labelWardName.text("Selected Ward:");

    selectedWardName = $("#listWards option:selected").text();
    fieldWardName.val(selectedWardName);

}


function loadAllCountries() {
    url = contextPath + "countries/list";
    listCities.empty();
    listDistricts.empty();
    listWards.empty();
    fieldWardName.val("");
    buttonAddWard.prop("disabled", true);
    buttonUpdateWard.prop("disabled", true);
    buttonDeleteWard.prop("disabled", true);

    $.get(url, function(responseJSON) {
        listCountries.empty();

        $.each(responseJSON, function (index, country) {
            $("<option>").val(country.id).text(country.name).appendTo(listCountries);
        });

    }).done(function () {
        btnLoadAllCountry.val("Refresh Country List");
        showToastMessasge("All countries have been loaded");
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });
}

function loadAllCities() {
    selectedCountry = $("#listCountries option:selected");
    countryId = selectedCountry.val();
    url = contextPath + "cities/list_by_country/" + countryId;

    $.get(url, function(responseJSON) {
        listCities.empty();

        $.each(responseJSON, function (index, city) {
            $("<option>").val(city.id).text(city.name).appendTo(listCities);
        });

    }).done(function () {
        changeWardToNewWard();
        showToastMessasge("All cities have been loaded for country " + selectedCountry.text());
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });
}



function loadAllDistricts() {
    selectedCity = $("#listCities option:selected");
    cityId = listCities.val();
    url = contextPath + "districts/list_by_city/" + cityId;
    $.get(url, function(responseJSON) {
        listDistricts.empty();
        $.each(responseJSON, function (index, district) {
            $("<option>").val(district.id).text(district.name).appendTo(listDistricts);
        });

    }).done(function () {
        changeWardToNewWard();
        showToastMessasge("All wards have been loaded for city " + selectedCity.text());
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });
}

function loadAllWards() {
    selectedDistrict = $("#listDistricts option:selected");
    districtId = listDistricts.val();
    url = contextPath + "wards/list_by_district/" + districtId;
    $.get(url, function(responseJSON) {
        listWards.empty();
        $.each(responseJSON, function (index, ward) {
            $("<option>").val(ward.id).text(ward.name).appendTo(listWards);
        });

    }).done(function () {
        changeWardToNewWard();
        showToastMessasge("All wards have been loaded for city " + selectedDistrict.text());
    }).fail(function () {
        showToastMessasge("ERROR: Could not connect to server or server encountered an error");
    });
}