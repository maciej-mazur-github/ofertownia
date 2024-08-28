const Item = ({ id, title, description }) => `
    <div class="row mt-2">
        <div class="col-md-3">
            <i class="fab fa-adversal fa-10x"></i>
        </div>
        <div class="col-md-9">
            <h5><a href="/offer.html?id=${id}" class="title">${title}</a></h5>
            <p class="description">${description}</p>
        </div>
    </div>
`;

const CategoryItem = ({ id, name, description, offers }) => `
    <div class="row mt-2">
        <div class="col-md-3">
            <i class="fab fa-adversal fa-10x"></i>
        </div>
        <div class="col-md-9">
            <h5 class="title">${name}</h5>
            <p class="offersNumber">Liczba ofert: ${offers}</p>
            <p class="description">${description}</p>
        </div>
    </div>
`;

const Empty = () => `
    <div class="row mt-2">
        <h2 class="mx-auto">Brak ogłoszeń</h2>
    </div>
`;

const Loader = () => `
    <div class="loader mx-auto"></div>
`;

const Error = () => `
    <div class="row mt-2">
        <h2 class="mx-auto">Błąd komunikacji z serwerem</h2>
    </div>
    <div class="row mt-2">
        <i class="far fa-frown fa-10x mx-auto"></i>
    </div>
`;

const OfferNotFound = () => `
    <div class="row mt-2">
        <h2 class="mx-auto">Ta oferta nie istnieje</h2>
    </div>
    <div class="row mt-2">
        <i class="far fa-frown fa-10x mx-auto"></i>
    </div>
`;

const Offer = (offer) => `
        <div class="jumbotron home-jumbo">
            <div class="container text-center text-white jumbo-container">
                <h1 class="display-3">${offer.title}</h1>
            </div>
        </div>
        <div class="row mt-2">
            <div class="col-md-4">
                <img class="img-fluid mx-auto d-block" src="${offer.imgUrl}">
            </div>
            <div class="col-md-8">
                <h5>${offer.price}zł</h5>
                <p>${offer.description}.</p>
            </div>
        </div>
`;

const Success = () => `
    <div class="row mt-2">
        <h2 class="mx-auto">Dane zapisano pomyślnie</h2>
    </div>
    <div class="row mt-2">
        <i class="far fa-smile fa-10x mx-auto"></i>
    </div>
`;

const renderOfferList = (url) => {
    const offerList = $('.container-main');
    offerList.html(Loader());
    $.ajax({
        url: url
    }).then(function(data) {
        if (data && data.length > 0) {
            offerList.html(data.map(Item).join(''));
        } else {
            offerList.html(Empty());
        }
    }).fail(function(err) {
        offerList.html(Error());
    });
};

const renderCategoryList = (url) => {
    const offerList = $('.container-main');
    offerList.html(Loader());
    $.ajax({
        url: url
    }).then(function(data) {
        if (data && data.length > 0) {
            offerList.html(data.map(CategoryItem).join(''));
        } else {
            offerList.html(Empty());
        }
    }).fail(function(err) {
        offerList.html(Error());
    });
};

const renderSingleOffer = () => {
    const idParam = getUrlParameter('id');
    const mainContainer = $('.container-main');
    $.ajax({
        url: `/api/offers/${idParam}`
    }).then(function(data) {
        console.log(data);
        mainContainer.html(Offer(data));
    }).fail(function(err) {
        mainContainer.html(OfferNotFound());
    });
};

const renderOfferCount = () => {
    const jumboContainer = $('.jumbo-container');
    $.ajax({
        url: '/api/offers/count'
    }).then(function(offers) {
        if (offers > 0) {
            jumboContainer.append($("<p></p>")
                .text(`Liczba ogłoszeń: ${offers}`));
        } else {
            jumboContainer.append($("<p></p>")
                .text('Brak ogłoszeń'));
        }
    });
};

const registerSearchForm = () => {
    $('#search-form').submit(function(e) {
        e.preventDefault();
        const searchText = $('#search-input').val();
        renderOfferList(`/api/offers?title=${searchText}`);
    });
};

const registerAddForm = () => {
    $('#add-form').submit(function(e) {
        const form = this;
        e.preventDefault();
        const formData = {};
        $.each(this, function(i, v){
            const input = $(v);
            formData[input.attr("name")] = input.val();
        });
        $.ajax({
            type: 'post',
            url: '/api/offers',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(formData),
        }).then(() => {
            console.log('success');
            $('.container-main').html(Success());
        }).fail(() => {
            $('#err-message').show();
        });
    });
};

const fillCategoriesInAddForm = () => {
    $.ajax({
        url: '/api/categories/names',
    }).then((data) => {
        console.log(data);
        $.each(data, (k, v) => {
            $('#categorySelect').append(
                $("<option></option>")
                .attr("value",v)
                .text(v));
        });
    }).fail(() => {
        $('#add-button').prop('disabled', 'true');
        $('#err-message').text('Nie udało się pobrać kategorii').show();
    });
};

const getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

$(document).ready(function() {
    const location = window.location.pathname;
    if(location === '/') {
        renderOfferList('/api/offers');
        renderOfferCount();
    } else if(location === '/search.html') {
        renderOfferList('/api/offers');
        registerSearchForm();
    } else if(location === '/add.html') {
        fillCategoriesInAddForm();
        registerAddForm();
    } else if(location === '/categories.html') {
        renderCategoryList('api/categories');
    } else if(location === '/offer.html') {
        renderSingleOffer();
    }
});