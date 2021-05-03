const productsList = document.getElementById('productsList')
const searchBar = document.getElementById('searchInput')

const allProducts = [];

fetch("http://localhost:8000/products/api").then(response => response.json()).then(data => {
    for (let album of data) {
        allProducts.push(album);
    }
    displayAlbums(allProducts)
})


searchBar.addEventListener('keyup', (e) => {


    const searchingCharacters = searchBar.value.toLowerCase();

    if (searchingCharacters.length === 0) {
        displayAlbums(allProducts);
    }

    let filteredAlbums = allProducts.filter(album => {
        return album.name.toLowerCase().includes(searchingCharacters);
    });
    displayAlbums(filteredAlbums);
})


const displayAlbums = (albums) => {
    productsList.innerHTML = albums
        .map((a) => {
            return `<div class="col-md-4">

            <div class="product text-center">
            <a href="/shop/product/${a.id}">
            <img src="${a.imageUrl}" width="250" height="250">
            </a>
            
                <div class="text-center">
                    <h4>${a.name}</h4>
                    <h3>${a.price} BGN</h3>
                </div>
            </div>
        </div>`
        })
        .join('');

}



