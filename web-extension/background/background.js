
chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
  console.log('received', request);
  if (request.action === 'addToCartTaobao') {
      handleAddToCartTaobaoRequest(request, sendResponse);
  }

  if (request.action === "addToCart1688") {
    handleAddToCart1688Request(request, sendResponse);
  }
  return true;
});

// TAOBAO

function mapTaobaoToServerSchema(product, userId) {
  const body = {
    userId: userId,
    count: product.itemCount,
    product: {
      externalId: product.externalId,
      skuId: product.skuId,
      skuParameters: Object.values(product.skuParameters),
      marketplace: product.marketplace,
      productUrl: product.productUrl,
      imageUrl: product.imageUrl,
      title: product.title,
      price: product.price,
    }
  }
  return body;
};

async function addProductToCartRequest(addProduct) {

  await makeAuthorizedRequest(
    url = "http://localhost:8092/api/product/cart/add",
    options = {
      'method': 'post',
      'body': JSON.stringify(addProduct)
    }
  )
};


async function handleAddToCartTaobaoRequest(request, sendResponse) {
  chrome.storage.local.get(['userId'], function (result) {
    const userId = result.userId;
    if (!userId) {
      sendResponse({status: 'error', message: 'Введите персональный код в окно расширения'});
    } else {
      const body = mapTaobaoToServerSchema(request.product, userId);

      addProductToCartRequest(body)
        .then(() => {
          sendResponse({status: 'success', message: 'Товар успешно добавлен в корзину'})
        })
        .catch(() => {
          sendResponse({status: 'error', message: 'Не получилось добавить товар в корзину'})
        });
    }
  });
}

// 1688

function map1688ToServerSchema(products, userId) {
  const bodyProducts = products
    .map((product) => (
      {
        product: {
          isCustom: false,
          externalId: product.externalId,
          skuId: product.skuId,
          skuParameters: product.skuParameters,
          marketplace: "1688",
          productUrl: product.productUrl,
          imageUrl: product.imageUrl,
          title: product.title,
          price: product.price,
          weight: product.weight,
          boxVolume: product.boxVolume,
          volume: product.volume
        },
        count: parseInt(product.itemCount)
      }
    ))
    .filter((it) => it.count > 0);

  const body = {
    userId: userId,
    products: bodyProducts
  }
  return body;
};


async function addProductsToCartRequest(addProducts) {
  await makeAuthorizedRequest(
    url = "http://localhost:8092/api/product/cart/add/list",
    options = {
      'method': 'post',
      'body': JSON.stringify(addProducts)
    }
  )
};

async function handleAddToCart1688Request(request, sendResponse) {
  chrome.storage.local.get(['userId'], function (result) {
    const userId = result.userId;
    if (!userId) {
      sendResponse({status: 'error', message: 'Введите персональный код в окно расширения'});
    } else {
      const body = map1688ToServerSchema(request.products, userId);

      console.log(body);

      if (body.products.length == 0) {
        sendResponse({status: 'error', message: 'Выберите количество товара'});
        return;
      }

      addProductsToCartRequest(body)
        .then(() => {
          sendResponse({status: 'success', message: 'Товары успешно добавлены в корзину'})
        })
        .catch(() => {
          sendResponse({status: 'error', message: 'Не получилось добавить товары в корзину'})
        });
    }
  });
}

// auth

const OAUTH_CONFIG = {
  tokenEndpoint: "http://localhost:8080/oauth2/token",
  clientId: "extension",
  clientSecret: "ext-secret",
  scopes: "read"
};

async function storeTokens(tokens) {
  await chrome.storage.local.set({
    accessToken: tokens.access_token,
    expiresAt: Date.now() + (tokens.expires_in * 1000)
  });
}

async function getAccessToken() {
  const { accessToken, expiresAt } = await chrome.storage.local.get(['accessToken', 'expiresAt']);

  if (accessToken && expiresAt && Date.now() < expiresAt) {
    return accessToken;
  }
  
  const response = await fetch(OAUTH_CONFIG.tokenEndpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'Authorization': `Basic ${btoa(`${OAUTH_CONFIG.clientId}:${OAUTH_CONFIG.clientSecret}`)}`
    },
    body: new URLSearchParams({
      grant_type: 'client_credentials',
      scope: OAUTH_CONFIG.scopes
    })
  });
  
  if (!response.ok) {
    throw new Error(`Token request failed: ${response.status}`);
  }
  
  const tokens = await response.json();
  await storeTokens(tokens);
  return tokens.access_token;
}

async function makeAuthorizedRequest(url, options = {}) {
  try {
    const accessToken = await getAccessToken();

    console.log(accessToken)
    
    const defaultOptions = {
      headers: {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
      }
    };
    
    const mergedOptions = { ...defaultOptions, ...options };
    const response = await fetch(url, mergedOptions);

    console.log(response)
    
    if (!response.ok) {
      throw new Error(`Request failed: ${response.status}`);
    }
    
    return response.json();
  } catch (error) {
    console.error('Authorized request failed:', error);
    throw error;
  }
};