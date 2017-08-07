import React from 'react';
import ReactDOM from 'react-dom';

// UI-Blocks
import Application from './ui-blocks/application/application';

// Service Worker
import registerServiceWorker from './registerServiceWorker';


ReactDOM.render(<Application />, document.getElementById('root'));
registerServiceWorker();
