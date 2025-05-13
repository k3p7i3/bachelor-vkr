const productData = {
  externalId: undefined,
  skuId: undefined,
  skuParameters: {},
  marketplace: "TaoBao",
  productUrl: undefined,
  imageUrl: undefined,
  title: undefined,
  price: undefined,
  itemCount: 1
};

setStyles();

const observer = new MutationObserver(taoBaoItemContent);

observer.observe(document.body, {
  childList: true,
  subtree: true,
  attributes: false
});


function taoBaoItemContent() {
  const purchasePanel = document.getElementById('purchasePanel');

  if (!purchasePanel) return;

  const skuData = purchasePanel.querySelector('div[class*="--SkuContent--_4079867"');
  if (!skuData) return;

  stylePurchasePanel(purchasePanel);

  const footWrap = purchasePanel.querySelector('div[class*="--footWrap--_35ff5bf"');
  const buttonDiv = createButtonElement(productData);
  footWrap.appendChild(buttonDiv);

  const addToCartButton = buttonDiv.firstChild;

  setTimeout(() => {getProductInfo(purchasePanel, skuData, addToCartButton)}, 200);

  skuData.childNodes.forEach(element => {
    const options = [...element.childNodes[1].childNodes];
    options.forEach(option => {
      option.addEventListener('click', () => {
        setTimeout(() => {getProductInfo(purchasePanel, skuData, addToCartButton)}, 300)
      })
    });
  });

  setItemCountProcessing(purchasePanel);

  observer.disconnect();
}

/* UPDATE/GET PRODUCT DATA */

function getProductInfo(purchasePanel, skuContent, button) {
  updateSelectedOptions(skuContent);
  updateProductUrlParams();
  updateProductImage();
  updateProductTitle(purchasePanel);
  updateProductPrice(purchasePanel);

  if (!productData.skuId) {
    button.disabled = true;
  } else {
    button.disabled = false;
  }
}

function updateSelectedOptions(skuContent) {
  const selectedDict  = {};

  skuContent.childNodes.forEach(element => {
    const title = element.childNodes[0].firstChild.innerText;

    const selected = element.childNodes[1]
      .querySelector('[class*="--isSelected--be9dcb21"]')
      ?.querySelector('span')?.innerText;

    selectedDict[title] = selected;
  });
  
  productData.skuParameters = selectedDict;
}

function updateProductImage() {
  setTimeout(() => {
    const imageWrapper = document.querySelector('div[class*="--mainPicWrap--b82041ed"]');
    const imageUrl = imageWrapper.firstChild.getAttribute('src');
    productData.imageUrl = imageUrl;
  }, 500)
}

function updateProductTitle(purchasePanel) {
  const titleWrapper = purchasePanel.querySelector('#tbpc-detail-item-title');
  const title = titleWrapper.querySelector('h1').innerText;
  productData.title = title;
}

function updateProductPrice(purchasePanel) {
  const price = purchasePanel.querySelector('div[class*="--highlightPrice--fea17cf4"]');
  const currency = price.querySelector('span[class*="--symbol--_81697cc"]').innerText;
  const value = price.querySelector('span[class*="--text--_4c1ce7d"]').innerText;

  if (currency == '¥') {
    productData.price = {
      value: parseFloat(value),
      currency: 'CNY'
    }
  }
}

function updateProductUrlParams() {
  const url = new URL(window.location.href);
  
  const externalId = url.searchParams.get("id");
  const skuId = url.searchParams.get("skuId");
  const productUrl = window.location.href;

  productData.externalId = externalId;
  productData.skuId = skuId;
  productData.productUrl = productUrl;
}


/* ITEM COUNT */

function setItemCountProcessing(purchasePanel) {
  const countWrapper = purchasePanel.querySelector('div[class*="--countWrapper--_4e4372f"]');
  const minusBtn = countWrapper.firstChild;
  const plusBtn = countWrapper.lastChild;
  const value = countWrapper.querySelector('input');

  minusBtn.addEventListener('click', () => {
    setTimeout(() => {calcItemCount(value)}, 300)
  });

  plusBtn.addEventListener('click', () => {
    setTimeout(() => {calcItemCount(value)}, 300)
  });


  value.addEventListener('change', (event) => {
    calcItemCount(event.target)
  });
}

function calcItemCount(inputCount) {
  productData.itemCount = inputCount.value;
}

/* STYLING */

function stylePurchasePanel(purchasePanel) {
  purchasePanel.style.border = '2px solid #ff781f';
  purchasePanel.style.borderRadius = '10px';
  purchasePanel.style.padding = '15px';
  const originalBuyDiv = purchasePanel.querySelector('[class*="--Actions--f3e90071"]');
  originalBuyDiv.style.display = 'none';
}

/* COMMON */

function setStyles() {
  const style = document.createElement('style');
  style.textContent = `
    .custom-add-to-cart {
      background-color: #4CAF50;
      color: white;
      padding: 10px 20px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      fontSize: 18px;
      font-family: system-ui, -apple-system, sans-serif;
      margin: 10px;
      line-height: 1.5;
    }

    .custom-add-to-cart:disabled {
      background-color:rgb(158, 158, 158);
    }

    .extension-toast {
      position: absolute;
      top: 20px;
      right: 20px;
      padding: 15px 20px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      max-width: 300px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.15);
      z-index: 99999;
      transform: translateY(100px);
      opacity: 0;
      animation: extension-toast-slide-in 0.3s ease-out forwards;
      font-family: system-ui, -apple-system, sans-serif;
    }

    .extension-toast-error {
      background: #FFF0F0;
      border-left: 4px solid #FF4D4F;
      color: #FF4D4F;
    }

    .extension-toast-success {
      background: #F6FFED;
      border-left: 4px solid #52C41A;
      color: #52C41A;
    }

    .extension-toast-icon {
      margin-right: 12px;
      font-size: 18px;
    }

    .extension-toast-message {
      flex: 1;
      font-size: 14px;
      line-height: 1.5;
    }

    .extension-toast-close {
      margin-left: 10px;
      cursor: pointer;
      font-size: 16px;
      opacity: 0.7;
    }

    .extension-toast-fadeout {
      animation: extension-toast-fade-out 0.3s ease-in forwards;
    }

    @keyframes extension-toast-slide-in {
      to { transform: translateY(0); opacity: 1; }
    }

    @keyframes extension-toast-fade-out {
      to { transform: translateY(-50px); opacity: 0; }
    }
  `;

  document.head.appendChild(style);
};

function showToast(message, type = 'error') {
  const toast = document.createElement('div');
  toast.className = `extension-toast extension-toast-${type}`;
  toast.innerHTML = `
    <div class="extension-toast-icon">
      ${type === 'error' ? '⚠️' : '✓'}
    </div>
    <div class="extension-toast-message">${message}</div>
    <div class="extension-toast-close">&times;</div>
  `;
  
  document.body.appendChild(toast);
  
  setTimeout(() => {
    toast.classList.add('extension-toast-fadeout');
    setTimeout(() => toast.remove(), 300);
  }, 5000);
  
  toast.querySelector('.extension-toast-close').addEventListener('click', () => {
    toast.remove();
  });
};

function createButtonElement(productData) {
  const buttonDiv = document.createElement('div');
  const button = document.createElement('button');
  button.textContent = 'Добавить в корзину';
  button.className = 'custom-add-to-cart';

  button.addEventListener('click', function() {
    chrome.runtime.sendMessage({
      action: 'addToCartTaobao',
      product: productData
    }, function (response) {
      showToast(response.message, response.status);
    });
  });

  buttonDiv.appendChild(button);
  return buttonDiv;
}

