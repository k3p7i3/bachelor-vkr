
import { Router } from './Router';
import { Provider } from 'react-redux';
import AppTheme from './AppTheme';

import store from './store';

function App() {
  return (
      <Provider store={store}>
        <AppTheme>
          <Router/>
        </AppTheme>
      </Provider>
  );
}

export default App;
