const SKU_SPLIT = '&gt;'

const skuParametersToSkuId = {};
const skuIdToProduct = {};

let minSkuFields = undefined;
let selectedFilterSku = undefined;

setStyles();

const observer = new MutationObserver(itemContent);

observer.observe(document.body, {
  childList: true,
  subtree: true,
  attributes: false
});

function itemContent() {
  const skuSelectionDiv = document.getElementById('skuSelection');
  const features = skuSelectionDiv.querySelectorAll('div.feature-item');

  if (!features) {
    return;
  }

  const jsonObj = getJsonProductInfo();

  fillSkuParametersDict(jsonObj);
  parseProductPackInfo(jsonObj);
  parseTitle(jsonObj);
  parseImageUrl(jsonObj);
  parseUrlProps(jsonObj);

  processSelectedProductChanges(features);

  setCartPanel();

  console.log(skuParametersToSkuId)
  console.log(skuIdToProduct)

  observer.disconnect();
}

function concatSkuProperties(props) {
  return props.join(SKU_SPLIT);
}

function splitSkuProperties(props) {
  return props.split(SKU_SPLIT);
}

// PARSE JSON INFO

function fillSkuParametersDict(jsonObj) {
  const skuInfoMap = jsonObj['result']?.['data']?.['Root']?.['fields']?.['dataJson']?.['skuModel']?.['skuInfoMap'];
  if (!skuInfoMap) {
    throw Error('Cannot parse skuInfoMap');
  };

  for (var concatProperties in skuInfoMap) {
    const skuParameters = splitSkuProperties(concatProperties);

    if (!minSkuFields || skuParameters.length <= minSkuFields) {
      minSkuFields = skuParameters.length;
    };

    const skuInfo = skuInfoMap[concatProperties];
    const skuId = skuInfo['skuId'];

    let price = undefined;
    if (skuId) {
      const priceNum = skuInfo['price'];
      if (priceNum) {
        price = {
          value: parseFloat(priceNum),
          currency: 'CNY'
        }
      } else {
        const altPriceSource = (
          jsonObj['result']?.['data']?.['Root']?.['fields']?.['dataJson']
            ?.['orderParamModel']?.['orderParam']?.['skuParam']?.['skuRangePrices']
        )
        const firstPrice = altPriceSource?.[0]?.['price']
        if (firstPrice) {
          const priceVal = parseInt(firstPrice)
          if (priceVal != NaN) {
            price = {
              value: priceVal,
              currency: 'CNY'
            }
          }
        }
      }

      skuParametersToSkuId[concatProperties] = skuId;
      skuIdToProduct[skuId] = {
        skuId: skuId,
        skuParameters: skuParameters,
        price: price,
        itemCount: 0
      };
    }
  };
}

function parseTitle(jsonObj) {
  const title = jsonObj['result']?.['data']?.['productTitle']?.['fields']?.['title']
  if (!title) {
    return;
  }

  for (var skuId in skuIdToProduct) {
    const product = skuIdToProduct[skuId];
    product['title'] = title;
  };
}

function parseImageUrl(jsonObj) {
  const skuProps = jsonObj['result']?.['data']?.['Root']?.['fields']?.['dataJson']?.['skuModel']?.['skuProps'];

  if (!skuProps) {
    throw Error('Cannot parse skuProps');
  };

  skuProps.forEach(element => {
    const props = element['value']
    if (props) {
      props.forEach(property => {
        if (property) {
          const name = property['name'];
          const imageUrl = property['imageUrl'];
          if (imageUrl) {
            for (var skuId in skuIdToProduct) {
              var product = skuIdToProduct[skuId];
              if (product['skuParameters'].includes(name)) {
                product['imageUrl'] = imageUrl;
              }
            }
          }
        }
      });
    }
  });

  return;
}

function parseUrlProps() {
  const url = window.location.href;

  const match = url.match(/offer\/(\d+)/);
  const externalId = match ? match[1] : undefined;

  for (var skuId in skuIdToProduct) {
    const product = skuIdToProduct[skuId];
    product['productUrl'] = url;
    product['externalId'] = externalId;
  }

  return;
}

function parseProductPackInfo(jsonObj) {
  const productInfo = jsonObj['result']?.['data']?.['productPackInfo']?.['fields']

  if (!productInfo) {
    throw Error('Cannot parse skuInfoMap');
  }

  let generalWeight = productInfo['unitWeight'];
  if (generalWeight == 0) {
    generalWeight  = undefined;
  }

  const pieceWeightScale = productInfo['pieceWeightScale']?.['pieceWeightScaleInfo']
  if (!pieceWeightScale) {
    if (generalWeight) {
      for (var skuId in skuIdToProduct) {
        const product = skuIdToProduct[skuId];
        product['weight'] = {
          value: generalWeight,
          unit: 'KILOGRAM'
        };
      }
    }
    return;
  }

  pieceWeightScale.forEach(element => {
    const product = skuIdToProduct[element['skuId']];
    if (product) {

      const weight = element['weight'];
      if (weight && weight != 0) {
        product['weight'] = {
          value: weight,
          unit: 'GRAM'
        }
      };

      const volume = element['volume'];
      if (volume && volume != 0) {
        product['volume'] = {
          value: volume,
          unit: 'MILLILITER'
        };
      };

      const length = element['length']
      const width = element['width']
      const height = element['height']
      if (
        length && length != 0
        && width && width != 0
        && height && height != 0
      ) {
        product['boxVolume'] = {
          length: length,
          width: width,
          height: height,
          unit: "CENTIMETRE"
        }
      };
    };
  });

  return;
}


function getJsonProductInfo() {
  const footer = document.querySelector('ali-footer');

  if (!footer) {
    return;
  }

  const script = footer.nextElementSibling.innerHTML;
  const position = script.indexOf("window.contextPath,") + 19
  const plainText = script.slice(position, -3);

  const fixedText = plainText.replace(/([{,]\s*)(\d+)(\s*:)/g, '$1"$2"$3');

  const jsonObject = JSON.parse(fixedText);
  console.log('try find script', jsonObject);
  return jsonObject;
};

// PARSE HTML INFO

function processSelectedProductChanges(featureItems) {
  setTimeout(() => {
    featureItems.forEach(featureItem => {
      processSelectedFilterSku(featureItem);
      processSkuWithItemCount(featureItem);
    });
  }, 300);
};

function processSelectedFilterSku(featureItem) {
  const transverseFilter = featureItem.querySelector('div.transverse-filter');
  if (!transverseFilter) {
    return;
  };

  const options = transverseFilter.querySelectorAll('button');

  options.forEach(option => {
    trySetSelectedSku(option);

    option.addEventListener('click', () => {
      setTimeout(() => {trySetSelectedSku(option)}, 300)
    })
  });
};

function trySetSelectedSku(option) {
  if (option.className.includes("active")) {
    const skuName = option.querySelector('span.label-name').innerHTML;
    selectedFilterSku = skuName;
  };
};

function processSkuWithItemCount(featureItem) {
  const expandViewList = featureItem.querySelector('div.expand-view-list');
  if (!expandViewList) {
    return;
  };

  const options = expandViewList.querySelectorAll('div.expand-view-item');
  options.forEach((option) => {
    const skuName = option.querySelector('span.item-label').getAttribute('title');
    const itemCountDiv = option.querySelector('span.item-input-number');

    addListenersToInputNumber(skuName, itemCountDiv);
  });
};

function addListenersToInputNumber(skuName, itemCountDiv) {
  const minusButton = itemCountDiv.querySelector('span.anticon-minus');
  const plusButton = itemCountDiv.querySelector('span.anticon-plus');
  const numberInput = itemCountDiv.querySelector('input.ant-input-number-input');

  minusButton.addEventListener('click', () => {
    updateItemCount(skuName, numberInput)
  });
  plusButton.addEventListener('click', () => {
    updateItemCount(skuName, numberInput)
  });
  numberInput.addEventListener('change', () => {
    updateItemCount(skuName, numberInput)
  });
};

function updateItemCount(skuName, numberInput) {
  setTimeout(() => {
    const value = numberInput.value;

    const props = minSkuFields < 2 ? skuName : concatSkuProperties([selectedFilterSku, skuName]);
    const skuId = skuParametersToSkuId[props];
    if (!skuId) { 
      return
    };
    const product = skuIdToProduct[skuId];
    if (!product) {
      return
    };

    product.itemCount = value || 0;
  }, 500);
}


/* CHANGE HTML */

function setCartPanel() {
  const cart = document.getElementById("cart").childNodes[0];
  cart.style.border = '2px solid #ff781f';
  cart.style.borderRadius = '10px';

  const submitOrderDiv = document.getElementById("submitOrder");
  const actionButtonsBlock = submitOrderDiv.querySelector("div.action-button-list")
  actionButtonsBlock.style.display = 'none';

  const button = createButtonElement();
  submitOrderDiv.appendChild(button);
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

function createButtonElement() {
  const buttonDiv = document.createElement('div');
  const button = document.createElement('button');
  button.textContent = 'Добавить в корзину';
  button.className = 'custom-add-to-cart';

  button.addEventListener('click', function() {
    chrome.runtime.sendMessage({
      action: 'addToCart1688',
      products: Object.values(skuIdToProduct)
    }, function (response) {
      showToast(response.message, response.status);
    });
  });

  buttonDiv.appendChild(button);
  return buttonDiv;
}