function menuMaker(key, data){
    if(key == 'navigation'){
        menuMaker_navigation(data);
    }
    else if(key == 'footer'){
        menuMaker_footer(data);
    }
}

function menuMaker_navigation(data){
    let nav = document.getElementById("navigation");

    let arr = [];

    menuMaker_navigation_method(arr, data);
    let text = "";
    for(let i = 0; i < arr.length; i++){
        text += arr[i];
        nav.innerHTML = text;
    }
}

function menuMaker_navigation_method(arr, data){
    for(let i = 0; i < data.length; i++){
        let item = data[i];
        if(item.childMenu == null){
            menuMaker_navigation_default(arr, item);
        }else{
            menuMaker_navigation_depth(arr, item);
        }
    }
}

function menuMaker_navigation_default(arr, item){
    arr.push(
        '<li class="nav-item"><a class="nav-link" aria-current="page" href="' + item.menuUrl + '">'+ item.menuNm + '</a></li>');
}

function menuMaker_navigation_depth(arr, item){
    arr.push(
        '<li class="nav-item"><a class="nav-link justify-content-between d-flex flex-row" data-bs-toggle="collapse" href="' +
        '#' + item.menuNm +
        '" role="button" aria-expanded="false" aria-controls="' +
        item.menuNm +
        '"><div>' +
        item.menuNm +
        '</div><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-caret-down" viewBox="0 0 16 16">' +
        '<path d="M3.204 5h9.592L8 10.481 3.204 5zm-.753.659 4.796 5.48a1 1 0 0 0 1.506 0l4.796-5.48c.566-.647.106-1.659-.753-1.659H3.204a1 1 0 0 0-.753 1.659z"/>' +
        '</svg></a></li><li class="nav-item collapse" id="' +
        item.menuNm +
        '"><ul class="navbar-nav justify-content-end flex-grow-1 pe-3 ms-3">');
    menuMaker_navigation_method(arr, item.childMenu);

    arr.push('</ul></li>');
}

function menuMaker_footer(data){
    let nav = document.getElementById("footer");

    let arr = [];

    menuMaker_footer_method(arr, data);
    let text = "";
    for(let i = 0; i < arr.length; i++){
        text += arr[i];
        nav.innerHTML = text;
    }
}

function menuMaker_footer_method(arr, data){
    for(let i = 0; i < data.length; i++){
        let item = data[i];

        menuMaker_footer_default(arr, item);
    }
}

function menuMaker_footer_default(arr, item){
    arr.push(
        '<a class="btn btn-outline-secondary btn-custom" onclick="' + item.menuUrl + '">' + item.menuNm + '</a>'
    );
}