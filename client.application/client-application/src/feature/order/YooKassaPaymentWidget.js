import {useCallback, useEffect} from 'react';

let isLoading = false;
let isInited = false;

export default function YooWidget({config, onComplete, onSuccess, onFail, onModalClose}) {
  const isModal = config?.customization?.modal;
  let checkout = undefined;
  
  const destroy = () => {
    checkout.destroy();
    isInited = false;
  }

  const initializeWidget = useCallback(
    () => {
      isInited = true;
      isLoading = false;
      if (!isModal) {
        const userErrorCallBack = config.error_callback;
        config.error_callback = (error) => {
          userErrorCallBack(error);
          isInited = false;
        };
      }
      checkout = new window.YooMoneyCheckoutWidget(config);
      checkout.on('complete', (result) => {
        onComplete && onComplete();
        destroy();
      });
      onSuccess && checkout.on('success', (result) => {
        onSuccess();
        destroy();
      });
      onFail && checkout.on('fail', (result) => {
        onFail();
        destroy();
      });

      if (config.customization?.modal) {
        checkout.on('modal_close', (result) => {
          onModalClose && onModalClose();
          destroy();
        });
      }
      checkout.render(config.customization?.modal ? undefined : 'payment-form');

    }, [config, onSuccess]);

  useEffect(() => {
    if (!isLoading && !isInited) {
      if (!window.YooMoneyCheckoutWidget) {
        isLoading = true;
        const script = document.createElement('script');
        script.src = 'https://yookassa.ru/checkout-widget/v1/checkout-widget.js';
        script.async = true;
        script.onload = initializeWidget;
        document.head.appendChild(script);
        return () => {
          document.head.removeChild(script);
        };
      } else {
        initializeWidget();
      }
    }

    return () => {
      if (!isInited && !isModal) {
        destroy();
      }
    }
  });

  return <>
    {isModal ? '' : <div id="payment-form"/>}
  </>;
}